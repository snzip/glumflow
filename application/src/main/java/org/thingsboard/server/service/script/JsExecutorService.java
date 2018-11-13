package org.thingsboard.server.service.script;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thingsboard.server.service.executors.AbstractListeningExecutor;

@Component
public class JsExecutorService extends AbstractListeningExecutor {

    @Value("${actors.rule.js_thread_pool_size}")
    private int jsExecutorThreadPoolSize;

    @Override
    protected int getThreadPollSize() {
        return jsExecutorThreadPoolSize;
    }

}
