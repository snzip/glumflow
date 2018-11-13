package org.thingsboard.server.service.transport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.thingsboard.server.gen.transport.TransportProtos.TransportApiRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.TransportApiResponseMsg;
import org.thingsboard.server.kafka.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ashvayka on 05.10.18.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "transport", value = "type", havingValue = "remote")
public class RemoteTransportApiService {

    @Value("${transport.remote.transport_api.requests_topic}")
    private String transportApiRequestsTopic;
    @Value("${transport.remote.transport_api.responses_topic}")
    private String transportApiResponsesTopic;
    @Value("${transport.remote.transport_api.max_pending_requests}")
    private int maxPendingRequests;
    @Value("${transport.remote.transport_api.request_timeout}")
    private long requestTimeout;
    @Value("${transport.remote.transport_api.request_poll_interval}")
    private int responsePollDuration;
    @Value("${transport.remote.transport_api.request_auto_commit_interval}")
    private int autoCommitInterval;

    @Autowired
    private TbKafkaSettings kafkaSettings;

    @Autowired
    private TbNodeIdProvider nodeIdProvider;

    @Autowired
    private TransportApiService transportApiService;

    private ExecutorService transportCallbackExecutor;

    private TbKafkaResponseTemplate<TransportApiRequestMsg, TransportApiResponseMsg> transportApiTemplate;

    @PostConstruct
    public void init() {
        this.transportCallbackExecutor = new ThreadPoolExecutor(0, 100, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

        TBKafkaProducerTemplate.TBKafkaProducerTemplateBuilder<TransportApiResponseMsg> responseBuilder = TBKafkaProducerTemplate.builder();
        responseBuilder.settings(kafkaSettings);
        responseBuilder.defaultTopic(transportApiResponsesTopic);
        responseBuilder.encoder(new TransportApiResponseEncoder());

        TBKafkaConsumerTemplate.TBKafkaConsumerTemplateBuilder<TransportApiRequestMsg> requestBuilder = TBKafkaConsumerTemplate.builder();
        requestBuilder.settings(kafkaSettings);
        requestBuilder.topic(transportApiRequestsTopic);
        requestBuilder.clientId(nodeIdProvider.getNodeId());
        requestBuilder.groupId("tb-node");
        requestBuilder.autoCommit(true);
        requestBuilder.autoCommitIntervalMs(autoCommitInterval);
        requestBuilder.decoder(new TransportApiRequestDecoder());

        TbKafkaResponseTemplate.TbKafkaResponseTemplateBuilder
                <TransportApiRequestMsg, TransportApiResponseMsg> builder = TbKafkaResponseTemplate.builder();
        builder.requestTemplate(requestBuilder.build());
        builder.responseTemplate(responseBuilder.build());
        builder.maxPendingRequests(maxPendingRequests);
        builder.requestTimeout(requestTimeout);
        builder.pollInterval(responsePollDuration);
        builder.executor(transportCallbackExecutor);
        builder.handler(transportApiService);
        transportApiTemplate = builder.build();
        transportApiTemplate.init();
    }

    @PreDestroy
    public void destroy() {
        if (transportApiTemplate != null) {
            transportApiTemplate.stop();
        }
        if (transportCallbackExecutor != null) {
            transportCallbackExecutor.shutdownNow();
        }
    }

}
