package org.thingsboard.server.kafka;

import java.util.UUID;

public interface TbKafkaRequestIdExtractor<T> {

    UUID extractRequestId(T value);

}
