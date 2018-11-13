package org.thingsboard.server.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;

/**
 * Created by ashvayka on 25.09.18.
 */
public interface TbKafkaPartitioner<T> extends Partitioner {

    int partition(String topic, String key, T value, byte[] encodedValue, List<PartitionInfo> partitions);

}
