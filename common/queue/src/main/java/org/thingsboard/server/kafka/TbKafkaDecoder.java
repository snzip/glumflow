package org.thingsboard.server.kafka;

import java.io.IOException;

/**
 * Created by ashvayka on 25.09.18.
 */
public interface TbKafkaDecoder<T> {

    T decode(byte[] data) throws IOException;

}
