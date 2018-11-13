package org.thingsboard.server.service.script;

import org.thingsboard.server.gen.js.JsInvokeProtos;
import org.thingsboard.server.kafka.TbKafkaEncoder;

/**
 * Created by ashvayka on 25.09.18.
 */
public class RemoteJsRequestEncoder implements TbKafkaEncoder<JsInvokeProtos.RemoteJsRequest> {
    @Override
    public byte[] encode(JsInvokeProtos.RemoteJsRequest value) {
        return value.toByteArray();
    }
}
