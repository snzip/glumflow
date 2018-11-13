package org.thingsboard.server.transport.http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.transport.TransportContext;

import javax.annotation.PostConstruct;

/**
 * Created by ashvayka on 04.10.18.
 */
@Slf4j
@ConditionalOnExpression("'${transport.type:null}'=='null' || ('${transport.type}'=='local' && '${transport.http.enabled}'=='true')")
@Component
public class HttpTransportContext extends TransportContext {

    @Getter
    @Value("${transport.http.request_timeout}")
    private long defaultTimeout;

}
