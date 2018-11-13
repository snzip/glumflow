package org.thingsboard.server.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thingsboard.server.service.executors.AbstractListeningExecutor;

@Component
public class MailExecutorService extends AbstractListeningExecutor {

    @Value("${actors.rule.mail_thread_pool_size}")
    private int mailExecutorThreadPoolSize;

    @Override
    protected int getThreadPollSize() {
        return mailExecutorThreadPoolSize;
    }

}
