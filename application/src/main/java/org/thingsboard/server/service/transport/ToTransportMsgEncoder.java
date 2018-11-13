package org.thingsboard.server.service.transport;

import org.thingsboard.server.gen.transport.TransportProtos.ToTransportMsg;
import org.thingsboard.server.kafka.TbKafkaEncoder;

/**
 * Created by ashvayka on 05.10.18.
 */
public class ToTransportMsgEncoder implements TbKafkaEncoder<ToTransportMsg> {
    @Override
    public byte[] encode(ToTransportMsg value) {
        return value.toByteArray();
    }
}
