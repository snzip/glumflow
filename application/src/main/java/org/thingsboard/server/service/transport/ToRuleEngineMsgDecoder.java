package org.thingsboard.server.service.transport;

import org.thingsboard.server.gen.transport.TransportProtos.ToRuleEngineMsg;
import org.thingsboard.server.kafka.TbKafkaDecoder;

import java.io.IOException;

/**
 * Created by ashvayka on 05.10.18.
 */
public class ToRuleEngineMsgDecoder implements TbKafkaDecoder<ToRuleEngineMsg> {
    @Override
    public ToRuleEngineMsg decode(byte[] data) throws IOException {
        return ToRuleEngineMsg.parseFrom(data);
    }
}
