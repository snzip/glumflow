package org.thingsboard.server.actors.rpc;

import lombok.Data;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

/**
 * @author Andrew Shvayka
 */
@Data
public final class RpcBroadcastMsg {
    private final ClusterAPIProtos.ClusterMessage msg;
}
