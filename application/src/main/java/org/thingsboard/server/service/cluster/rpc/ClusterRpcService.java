package org.thingsboard.server.service.cluster.rpc;

import io.grpc.stub.StreamObserver;
import org.thingsboard.server.actors.rpc.RpcBroadcastMsg;
import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
public interface ClusterRpcService {

    void init(RpcMsgListener listener);

    void broadcast(RpcBroadcastMsg msg);

    void onSessionCreated(UUID msgUid, StreamObserver<ClusterAPIProtos.ClusterMessage> inputStream);

    void tell(ClusterAPIProtos.ClusterMessage message);

    void tell(ServerAddress serverAddress, TbActorMsg actorMsg);

    void tell(ServerAddress serverAddress, ClusterAPIProtos.MessageType msgType, byte[] data);
}
