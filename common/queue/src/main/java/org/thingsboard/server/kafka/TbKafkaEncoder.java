package org.thingsboard.server.kafka;

/**
 * Created by ashvayka on 25.09.18.
 */
public interface TbKafkaEncoder<T> {

    byte[] encode(T value);

}
