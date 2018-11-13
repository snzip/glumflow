package org.thingsboard.server.service.rpc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.thingsboard.rule.engine.api.msg.ToDeviceActorNotificationMsg;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.msg.MsgType;
import org.thingsboard.server.common.msg.core.ToServerRpcResponseMsg;

/**
 * Created by ashvayka on 16.04.18.
 */
@ToString
@RequiredArgsConstructor
public class ToServerRpcResponseActorMsg implements ToDeviceActorNotificationMsg {

    @Getter
    private final TenantId tenantId;

    @Getter
    private final DeviceId deviceId;

    @Getter
    private final ToServerRpcResponseMsg msg;

    @Override
    public MsgType getMsgType() {
        return MsgType.SERVER_RPC_RESPONSE_TO_DEVICE_ACTOR_MSG;
    }
}
