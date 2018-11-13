package org.thingsboard.server.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ashvayka on 12.10.18.
 */
@Slf4j
@Component
public class TbNodeIdProvider {

    @Getter
    @Value("${cluster.node_id:#{null}}")
    private String nodeId;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(nodeId)) {
            try {
                nodeId = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                nodeId = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10);
            }
        }
        log.info("Current NodeId: {}", nodeId);
    }

}
