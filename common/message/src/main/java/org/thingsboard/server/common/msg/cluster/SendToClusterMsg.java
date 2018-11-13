package org.thingsboard.server.common.msg.cluster;

import lombok.Data;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.msg.MsgType;
import org.thingsboard.server.common.msg.TbActorMsg;

@Data
public class SendToClusterMsg implements TbActorMsg {

    private TbActorMsg msg;
    private EntityId entityId;

    public SendToClusterMsg(EntityId entityId, TbActorMsg msg) {
        this.entityId = entityId;
        this.msg = msg;
    }


    @Override
    public MsgType getMsgType() {
        return MsgType.SEND_TO_CLUSTER_MSG;
    }
}
