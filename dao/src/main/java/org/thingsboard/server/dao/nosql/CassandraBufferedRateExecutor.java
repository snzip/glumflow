package org.thingsboard.server.dao.nosql;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.google.common.util.concurrent.SettableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thingsboard.server.dao.util.AbstractBufferedRateExecutor;
import org.thingsboard.server.dao.util.AsyncTaskContext;
import org.thingsboard.server.dao.util.NoSqlAnyDao;

import javax.annotation.PreDestroy;

/**
 * Created by ashvayka on 24.10.18.
 */
@Component
@Slf4j
@NoSqlAnyDao
public class CassandraBufferedRateExecutor extends AbstractBufferedRateExecutor<CassandraStatementTask, ResultSetFuture, ResultSet> {

    public CassandraBufferedRateExecutor(
            @Value("${cassandra.query.buffer_size}") int queueLimit,
            @Value("${cassandra.query.concurrent_limit}") int concurrencyLimit,
            @Value("${cassandra.query.permit_max_wait_time}") long maxWaitTime,
            @Value("${cassandra.query.dispatcher_threads:2}") int dispatcherThreads,
            @Value("${cassandra.query.callback_threads:2}") int callbackThreads,
            @Value("${cassandra.query.poll_ms:50}") long pollMs) {
        super(queueLimit, concurrencyLimit, maxWaitTime, dispatcherThreads, callbackThreads, pollMs);
    }

    @Scheduled(fixedDelayString = "${cassandra.query.rate_limit_print_interval_ms}")
    public void printStats() {
        log.info("Permits queueSize [{}] totalAdded [{}] totalLaunched [{}] totalReleased [{}] totalFailed [{}] totalExpired [{}] totalRejected [{}] currBuffer [{}] ",
                getQueueSize(),
                totalAdded.getAndSet(0), totalLaunched.getAndSet(0), totalReleased.getAndSet(0),
                totalFailed.getAndSet(0), totalExpired.getAndSet(0), totalRejected.getAndSet(0),
                concurrencyLevel.get());
    }

    @PreDestroy
    public void stop() {
        super.stop();
    }

    @Override
    protected SettableFuture<ResultSet> create() {
        return SettableFuture.create();
    }

    @Override
    protected ResultSetFuture wrap(CassandraStatementTask task, SettableFuture<ResultSet> future) {
        return new TbResultSetFuture(future);
    }

    @Override
    protected ResultSetFuture execute(AsyncTaskContext<CassandraStatementTask, ResultSet> taskCtx) {
        CassandraStatementTask task = taskCtx.getTask();
        return task.getSession().executeAsync(task.getStatement());
    }

}
