package org.thingsboard.server.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by ashvayka on 25.09.18.
 */
@Data
public class TbKafkaProperty {

    private String key;
    private String value;
}
