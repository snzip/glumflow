package org.thingsboard.server.kafka;

import java.util.UUID;

public interface TbKafkaEnricher<T> {

    T enrich(T value, String responseTopic, UUID requestId);

}
