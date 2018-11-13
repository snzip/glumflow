package org.thingsboard.rule.engine.delay;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.rule.engine.api.RuleNode;
import org.thingsboard.rule.engine.api.TbContext;
import org.thingsboard.rule.engine.api.TbNode;
import org.thingsboard.rule.engine.api.TbNodeConfiguration;
import org.thingsboard.rule.engine.api.TbNodeException;
import org.thingsboard.rule.engine.api.util.TbNodeUtils;
import org.thingsboard.server.common.data.plugin.ComponentType;
import org.thingsboard.server.common.msg.TbMsg;
import org.thingsboard.server.common.msg.TbMsgMetaData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.thingsboard.rule.engine.api.TbRelationTypes.FAILURE;
import static org.thingsboard.rule.engine.api.TbRelationTypes.SUCCESS;

@Slf4j
@RuleNode(
        type = ComponentType.ACTION,
        name = "delay",
        configClazz = TbMsgDelayNodeConfiguration.class,
        nodeDescription = "Delays incoming message",
        nodeDetails = "Delays messages for configurable period.",
        icon = "pause",
        uiResources = {"static/rulenode/rulenode-core-config.js"},
        configDirective = "tbActionNodeMsgDelayConfig"
)

public class TbMsgDelayNode implements TbNode {

    private static final String TB_MSG_DELAY_NODE_MSG = "TbMsgDelayNodeMsg";

    private TbMsgDelayNodeConfiguration config;
    private long delay;
    private Map<UUID, TbMsg> pendingMsgs;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, TbMsgDelayNodeConfiguration.class);
        this.delay = TimeUnit.SECONDS.toMillis(config.getPeriodInSeconds());
        this.pendingMsgs = new HashMap<>();
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) {
        if (msg.getType().equals(TB_MSG_DELAY_NODE_MSG)) {
            TbMsg pendingMsg = pendingMsgs.remove(UUID.fromString(msg.getData()));
            if (pendingMsg != null) {
                ctx.tellNext(pendingMsg, SUCCESS);
            }
        } else {
            if(pendingMsgs.size() < config.getMaxPendingMsgs()) {
                pendingMsgs.put(msg.getId(), msg);
                TbMsg tickMsg = ctx.newMsg(TB_MSG_DELAY_NODE_MSG, ctx.getSelfId(), new TbMsgMetaData(), msg.getId().toString());
                ctx.tellSelf(tickMsg, delay);
            } else {
                ctx.tellNext(msg, FAILURE, new RuntimeException("Max limit of pending messages reached!"));
            }
        }
    }

    @Override
    public void destroy() {
        pendingMsgs.clear();
    }
}
