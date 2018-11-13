package org.thingsboard.server.service.script;

import org.thingsboard.server.gen.js.JsInvokeProtos;
import org.thingsboard.server.kafka.TbKafkaDecoder;

import java.io.IOException;

/**
 * Created by ashvayka on 25.09.18.
 */
public class RemoteJsResponseDecoder implements TbKafkaDecoder<JsInvokeProtos.RemoteJsResponse> {

    @Override
    public JsInvokeProtos.RemoteJsResponse decode(byte[] data) throws IOException {
        return JsInvokeProtos.RemoteJsResponse.parseFrom(data);
    }
}
