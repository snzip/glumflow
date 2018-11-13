package org.thingsboard.server.service.cluster.rpc;

import org.thingsboard.server.actors.rpc.RpcBroadcastMsg;
import org.thingsboard.server.actors.rpc.RpcSessionCreateRequestMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

/**
 * @author Andrew Shvayka
 */

public interface RpcMsgListener {
    void onReceivedMsg(ServerAddress remoteServer, ClusterAPIProtos.ClusterMessage msg);
    void onSendMsg(ClusterAPIProtos.ClusterMessage msg);
    void onRpcSessionCreateRequestMsg(RpcSessionCreateRequestMsg msg);
    void onBroadcastMsg(RpcBroadcastMsg msg);
}
