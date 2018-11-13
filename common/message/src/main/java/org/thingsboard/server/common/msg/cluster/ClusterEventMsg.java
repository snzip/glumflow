package org.thingsboard.server.common.msg.cluster;

import lombok.Data;
import org.thingsboard.server.common.msg.MsgType;
import org.thingsboard.server.common.msg.TbActorMsg;

/**
 * @author Andrew Shvayka
 */
@Data
public final class ClusterEventMsg implements TbActorMsg {

    private final ServerAddress serverAddress;
    private final boolean added;

    @Override
    public MsgType getMsgType() {
        return MsgType.CLUSTER_EVENT_MSG;
    }
}
