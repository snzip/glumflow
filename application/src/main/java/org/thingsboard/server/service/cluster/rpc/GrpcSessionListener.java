package org.thingsboard.server.service.cluster.rpc;

import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

/**
 * @author Andrew Shvayka
 */
public interface GrpcSessionListener {

    void onConnected(GrpcSession session);

    void onDisconnected(GrpcSession session);

    void onReceiveClusterGrpcMsg(GrpcSession session, ClusterAPIProtos.ClusterMessage clusterMessage);

    void onError(GrpcSession session, Throwable t);
}
