package org.thingsboard.rule.engine.api;

import org.thingsboard.server.common.data.id.DeviceId;
import java.util.function.Consumer;

/**
 * Created by ashvayka on 02.04.18.
 */
public interface RuleEngineRpcService {

    void sendRpcReply(DeviceId deviceId, int requestId, String body);

    void sendRpcRequest(RuleEngineDeviceRpcRequest request, Consumer<RuleEngineDeviceRpcResponse> consumer);

}
