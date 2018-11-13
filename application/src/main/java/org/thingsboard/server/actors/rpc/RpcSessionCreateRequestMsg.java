package org.thingsboard.server.actors.rpc;

import io.grpc.stub.StreamObserver;
import lombok.Data;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
@Data
public final class RpcSessionCreateRequestMsg {

    private final UUID msgUid;
    private final ServerAddress remoteAddress;
    private final StreamObserver<ClusterAPIProtos.ClusterMessage> responseObserver;

}
