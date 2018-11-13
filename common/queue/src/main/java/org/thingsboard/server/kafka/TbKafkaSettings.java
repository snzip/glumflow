package org.thingsboard.server.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * Created by ashvayka on 25.09.18.
 */
@Slf4j
@ConditionalOnProperty(prefix = "kafka", value = "enabled", havingValue = "true", matchIfMissing = false)
@Component
public class TbKafkaSettings {

    public static final String REQUEST_ID_HEADER = "requestId";
    public static final String RESPONSE_TOPIC_HEADER = "responseTopic";


    @Value("${kafka.bootstrap.servers}")
    private String servers;

    @Value("${kafka.acks}")
    private String acks;

    @Value("${kafka.retries}")
    private int retries;

    @Value("${kafka.batch.size}")
    private int batchSize;

    @Value("${kafka.linger.ms}")
    private long lingerMs;

    @Value("${kafka.buffer.memory}")
    private long bufferMemory;

    @Value("${kafka.other:#{null}}")
    private List<TbKafkaProperty> other;

    public Properties toProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        if(other != null){
            other.forEach(kv -> props.put(kv.getKey(), kv.getValue()));
        }
        return props;
    }
}
