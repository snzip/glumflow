package org.thingsboard.server.service.cluster.discovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.common.msg.cluster.ServerType;

import javax.annotation.PostConstruct;

import static org.thingsboard.server.utils.MiscUtils.missingProperty;

/**
 * @author Andrew Shvayka
 */
@Service
@Slf4j
public class CurrentServerInstanceService implements ServerInstanceService {

    @Value("${rpc.bind_host}")
    private String rpcHost;
    @Value("${rpc.bind_port}")
    private Integer rpcPort;

    private ServerInstance self;

    @PostConstruct
    public void init() {
        Assert.hasLength(rpcHost, missingProperty("rpc.bind_host"));
        Assert.notNull(rpcPort, missingProperty("rpc.bind_port"));
        self = new ServerInstance(new ServerAddress(rpcHost, rpcPort, ServerType.CORE));
        log.info("Current server instance: [{};{}]", self.getHost(), self.getPort());
    }

    @Override
    public ServerInstance getSelf() {
        return self;
    }
}
