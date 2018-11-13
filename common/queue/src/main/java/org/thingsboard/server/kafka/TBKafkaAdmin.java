package org.thingsboard.server.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by ashvayka on 24.09.18.
 */
public class TBKafkaAdmin {

    AdminClient client;

    public TBKafkaAdmin(TbKafkaSettings settings) {
        client = AdminClient.create(settings.toProps());
    }

    public CreateTopicsResult createTopic(NewTopic topic){
        return client.createTopics(Collections.singletonList(topic));
    }
}
