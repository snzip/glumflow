package org.thingsboard.server.actors.rpc;

import lombok.Data;
import org.thingsboard.server.common.msg.cluster.ServerAddress;

import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
@Data
public final class RpcSessionConnectedMsg {

    private final ServerAddress remoteAddress;
    private final UUID id;
}
