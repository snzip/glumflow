package org.thingsboard.server.service.rpc;

import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.common.msg.rpc.ToDeviceRpcRequest;

import java.util.function.Consumer;

/**
 * Created by ashvayka on 16.04.18.
 */
public interface DeviceRpcService {

    void processRestAPIRpcRequestToRuleEngine(ToDeviceRpcRequest request, Consumer<FromDeviceRpcResponse> responseConsumer);

    void processResponseToServerSideRPCRequestFromRuleEngine(ServerAddress requestOriginAddress, FromDeviceRpcResponse response);

    void forwardServerSideRPCRequestToDeviceActor(ToDeviceRpcRequest request, Consumer<FromDeviceRpcResponse> responseConsumer);

    void processResponseToServerSideRPCRequestFromDeviceActor(FromDeviceRpcResponse response);

    void processResponseToServerSideRPCRequestFromRemoteServer(ServerAddress serverAddress, byte[] data);

    void sendReplyToRpcCallFromDevice(TenantId tenantId, DeviceId deviceId, int requestId, String body);
}
