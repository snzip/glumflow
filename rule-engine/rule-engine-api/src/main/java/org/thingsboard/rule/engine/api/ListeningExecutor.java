package org.thingsboard.rule.engine.api;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public interface ListeningExecutor extends Executor {

    <T> ListenableFuture<T> executeAsync(Callable<T> task);

}
