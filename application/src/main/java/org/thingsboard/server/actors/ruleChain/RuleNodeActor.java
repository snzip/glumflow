package org.thingsboard.server.actors.ruleChain;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.service.ComponentActor;
import org.thingsboard.server.actors.service.ContextBasedCreator;
import org.thingsboard.server.common.data.id.RuleChainId;
import org.thingsboard.server.common.data.id.RuleNodeId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.plugin.ComponentLifecycleMsg;

@Slf4j
public class RuleNodeActor extends ComponentActor<RuleNodeId, RuleNodeActorMessageProcessor> {

    private final RuleChainId ruleChainId;

    private RuleNodeActor(ActorSystemContext systemContext, TenantId tenantId, RuleChainId ruleChainId, RuleNodeId ruleNodeId) {
        super(systemContext, tenantId, ruleNodeId);
        this.ruleChainId = ruleChainId;
        setProcessor(new RuleNodeActorMessageProcessor(tenantId, ruleChainId, ruleNodeId, systemContext,
                context().parent(), context().self()));
    }

    @Override
    protected boolean process(TbActorMsg msg) {
        switch (msg.getMsgType()) {
            case COMPONENT_LIFE_CYCLE_MSG:
                onComponentLifecycleMsg((ComponentLifecycleMsg) msg);
                break;
            case RULE_CHAIN_TO_RULE_MSG:
                onRuleChainToRuleNodeMsg((RuleChainToRuleNodeMsg) msg);
                break;
            case RULE_TO_SELF_ERROR_MSG:
                onRuleNodeToSelfErrorMsg((RuleNodeToSelfErrorMsg) msg);
                break;
            case RULE_TO_SELF_MSG:
                onRuleNodeToSelfMsg((RuleNodeToSelfMsg) msg);
                break;
            case STATS_PERSIST_TICK_MSG:
                onStatsPersistTick(id);
                break;
            default:
                return false;
        }
        return true;
    }

    private void onRuleNodeToSelfMsg(RuleNodeToSelfMsg msg) {
        log.debug("[{}] Going to process rule msg: {}", id, msg.getMsg());
        try {
            processor.onRuleToSelfMsg(msg);
            increaseMessagesProcessedCount();
        } catch (Exception e) {
            logAndPersist("onRuleMsg", e);
        }
    }

    private void onRuleChainToRuleNodeMsg(RuleChainToRuleNodeMsg msg) {
        log.debug("[{}] Going to process rule msg: {}", id, msg.getMsg());
        try {
            processor.onRuleChainToRuleNodeMsg(msg);
            increaseMessagesProcessedCount();
        } catch (Exception e) {
            logAndPersist("onRuleMsg", e);
        }
    }

    private void onRuleNodeToSelfErrorMsg(RuleNodeToSelfErrorMsg msg) {
        logAndPersist("onRuleMsg", ActorSystemContext.toException(msg.getError()));
    }

    public static class ActorCreator extends ContextBasedCreator<RuleNodeActor> {
        private static final long serialVersionUID = 1L;

        private final TenantId tenantId;
        private final RuleChainId ruleChainId;
        private final RuleNodeId ruleNodeId;

        public ActorCreator(ActorSystemContext context, TenantId tenantId, RuleChainId ruleChainId, RuleNodeId ruleNodeId) {
            super(context);
            this.tenantId = tenantId;
            this.ruleChainId = ruleChainId;
            this.ruleNodeId = ruleNodeId;

        }

        @Override
        public RuleNodeActor create() throws Exception {
            return new RuleNodeActor(context, tenantId, ruleChainId, ruleNodeId);
        }
    }

    @Override
    protected long getErrorPersistFrequency() {
        return systemContext.getRuleNodeErrorPersistFrequency();
    }

}
