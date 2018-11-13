package org.thingsboard.server.common.msg.timeout;

import org.thingsboard.server.common.msg.MsgType;

/**
 * @author Andrew Shvayka
 */
public final class DeviceActorClientSideRpcTimeoutMsg extends TimeoutMsg<Integer> {

    public DeviceActorClientSideRpcTimeoutMsg(Integer id, long timeout) {
        super(id, timeout);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.DEVICE_ACTOR_CLIENT_SIDE_RPC_TIMEOUT_MSG;
    }
}
