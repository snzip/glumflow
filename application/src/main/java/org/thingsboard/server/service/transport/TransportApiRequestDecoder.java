package org.thingsboard.server.service.transport;

import org.thingsboard.server.gen.transport.TransportProtos.TransportApiRequestMsg;
import org.thingsboard.server.kafka.TbKafkaDecoder;

import java.io.IOException;

/**
 * Created by ashvayka on 05.10.18.
 */
public class TransportApiRequestDecoder implements TbKafkaDecoder<TransportApiRequestMsg> {
    @Override
    public TransportApiRequestMsg decode(byte[] data) throws IOException {
        return TransportApiRequestMsg.parseFrom(data);
    }
}
