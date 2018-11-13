package org.thingsboard.server.kafka;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Created by ashvayka on 05.10.18.
 */
public interface TbKafkaHandler<Request, Response> {

    ListenableFuture<Response> handle(Request request);

}
