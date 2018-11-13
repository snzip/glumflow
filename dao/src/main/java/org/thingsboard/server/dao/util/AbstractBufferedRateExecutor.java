package org.thingsboard.server.dao.util;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ashvayka on 24.10.18.
 */
@Slf4j
public abstract class AbstractBufferedRateExecutor<T extends AsyncTask, F extends ListenableFuture<V>, V> implements BufferedRateExecutor<T, F> {

    private final long maxWaitTime;
    private final long pollMs;
    private final BlockingQueue<AsyncTaskContext<T, V>> queue;
    private final ExecutorService dispatcherExecutor;
    private final ExecutorService callbackExecutor;
    private final ScheduledExecutorService timeoutExecutor;
    private final int concurrencyLimit;

    protected final AtomicInteger concurrencyLevel = new AtomicInteger();
    protected final AtomicInteger totalAdded = new AtomicInteger();
    protected final AtomicInteger totalLaunched = new AtomicInteger();
    protected final AtomicInteger totalReleased = new AtomicInteger();
    protected final AtomicInteger totalFailed = new AtomicInteger();
    protected final AtomicInteger totalExpired = new AtomicInteger();
    protected final AtomicInteger totalRejected = new AtomicInteger();

    public AbstractBufferedRateExecutor(int queueLimit, int concurrencyLimit, long maxWaitTime, int dispatcherThreads, int callbackThreads, long pollMs) {
        this.maxWaitTime = maxWaitTime;
        this.pollMs = pollMs;
        this.concurrencyLimit = concurrencyLimit;
        this.queue = new LinkedBlockingDeque<>(queueLimit);
        this.dispatcherExecutor = Executors.newFixedThreadPool(dispatcherThreads);
        this.callbackExecutor = Executors.newFixedThreadPool(callbackThreads);
        this.timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
        for (int i = 0; i < dispatcherThreads; i++) {
            dispatcherExecutor.submit(this::dispatch);
        }
    }

    @Override
    public F submit(T task) {
        SettableFuture<V> settableFuture = create();
        F result = wrap(task, settableFuture);
        try {
            totalAdded.incrementAndGet();
            queue.add(new AsyncTaskContext<>(UUID.randomUUID(), task, settableFuture, System.currentTimeMillis()));
        } catch (IllegalStateException e) {
            totalRejected.incrementAndGet();
            settableFuture.setException(e);
        }
        return result;
    }

    public void stop() {
        if (dispatcherExecutor != null) {
            dispatcherExecutor.shutdownNow();
        }
        if (callbackExecutor != null) {
            callbackExecutor.shutdownNow();
        }
        if (timeoutExecutor != null) {
            timeoutExecutor.shutdownNow();
        }
    }

    protected abstract SettableFuture<V> create();

    protected abstract F wrap(T task, SettableFuture<V> future);

    protected abstract ListenableFuture<V> execute(AsyncTaskContext<T, V> taskCtx);

    private void dispatch() {
        log.info("Buffered rate executor thread started");
        while (!Thread.interrupted()) {
            int curLvl = concurrencyLevel.get();
            AsyncTaskContext<T, V> taskCtx = null;
            try {
                if (curLvl <= concurrencyLimit) {
                    taskCtx = queue.take();
                    final AsyncTaskContext<T, V> finalTaskCtx = taskCtx;
                    logTask("Processing", finalTaskCtx);
                    concurrencyLevel.incrementAndGet();
                    long timeout = finalTaskCtx.getCreateTime() + maxWaitTime - System.currentTimeMillis();
                    if (timeout > 0) {
                        totalLaunched.incrementAndGet();
                        ListenableFuture<V> result = execute(finalTaskCtx);
                        result = Futures.withTimeout(result, timeout, TimeUnit.MILLISECONDS, timeoutExecutor);
                        Futures.addCallback(result, new FutureCallback<V>() {
                            @Override
                            public void onSuccess(@Nullable V result) {
                                logTask("Releasing", finalTaskCtx);
                                totalReleased.incrementAndGet();
                                concurrencyLevel.decrementAndGet();
                                finalTaskCtx.getFuture().set(result);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                if (t instanceof TimeoutException) {
                                    logTask("Expired During Execution", finalTaskCtx);
                                } else {
                                    logTask("Failed", finalTaskCtx);
                                }
                                totalFailed.incrementAndGet();
                                concurrencyLevel.decrementAndGet();
                                finalTaskCtx.getFuture().setException(t);
                                log.debug("[{}] Failed to execute task: {}", finalTaskCtx.getId(), finalTaskCtx.getTask(), t);
                            }
                        }, callbackExecutor);
                    } else {
                        logTask("Expired Before Execution", finalTaskCtx);
                        totalExpired.incrementAndGet();
                        concurrencyLevel.decrementAndGet();
                        taskCtx.getFuture().setException(new TimeoutException());
                    }
                } else {
                    Thread.sleep(pollMs);
                }
            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
                if (taskCtx != null) {
                    log.debug("[{}] Failed to execute task: {}", taskCtx.getId(), taskCtx, e);
                    totalFailed.incrementAndGet();
                    concurrencyLevel.decrementAndGet();
                } else {
                    log.debug("Failed to queue task:", e);
                }
            }
        }
        log.info("Buffered rate executor thread stopped");
    }

    private void logTask(String action, AsyncTaskContext<T, V> taskCtx) {
        if (log.isTraceEnabled()) {
            log.trace("[{}] {} task: {}", taskCtx.getId(), action, taskCtx);
        } else {
            log.debug("[{}] {} task", taskCtx.getId(), action);
        }
    }

    protected int getQueueSize() {
        return queue.size();
    }
}
