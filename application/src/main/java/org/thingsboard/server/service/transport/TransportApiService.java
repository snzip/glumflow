package org.thingsboard.server.service.transport;

import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.kafka.TbKafkaHandler;

/**
 * Created by ashvayka on 05.10.18.
 */
public interface TransportApiService extends TbKafkaHandler<TransportProtos.TransportApiRequestMsg, TransportProtos.TransportApiResponseMsg> {
}
