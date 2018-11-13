package org.thingsboard.server.actors.device;

import akka.actor.ActorRef;
import lombok.Data;
import org.thingsboard.server.common.msg.MsgType;
import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.TbMsg;

/**
 * Created by ashvayka on 15.03.18.
 */
@Data
public final class DeviceActorToRuleEngineMsg implements TbActorMsg {

    private final ActorRef callbackRef;
    private final TbMsg tbMsg;

    @Override
    public MsgType getMsgType() {
        return MsgType.DEVICE_ACTOR_TO_RULE_ENGINE_MSG;
    }
}
