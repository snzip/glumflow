package org.thingsboard.server.transport.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslHandler;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.transport.TransportContext;
import org.thingsboard.server.common.transport.TransportService;
import org.thingsboard.server.kafka.TbNodeIdProvider;
import org.thingsboard.server.transport.mqtt.adaptors.MqttTransportAdaptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ashvayka on 04.10.18.
 */
@Slf4j
@ConditionalOnExpression("'${transport.type:null}'=='null' || ('${transport.type}'=='local' && '${transport.mqtt.enabled}'=='true')")
@Component
public class MqttTransportContext extends TransportContext {

    @Getter
    @Autowired(required = false)
    private MqttSslHandlerProvider sslHandlerProvider;

    @Getter
    @Autowired
    private MqttTransportAdaptor adaptor;

    @Getter
    @Value("${transport.mqtt.netty.max_payload_size}")
    private Integer maxPayloadSize;

    @Getter
    @Setter
    private SslHandler sslHandler;

}
