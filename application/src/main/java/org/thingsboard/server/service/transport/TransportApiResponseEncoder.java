package org.thingsboard.server.service.transport;

import org.thingsboard.server.kafka.TbKafkaEncoder;

import org.thingsboard.server.gen.transport.TransportProtos.TransportApiResponseMsg;

/**
 * Created by ashvayka on 05.10.18.
 */
public class TransportApiResponseEncoder implements TbKafkaEncoder<TransportApiResponseMsg> {
    @Override
    public byte[] encode(TransportApiResponseMsg value) {
        return value.toByteArray();
    }
}
