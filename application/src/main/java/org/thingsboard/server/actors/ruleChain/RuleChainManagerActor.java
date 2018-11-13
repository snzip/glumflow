package org.thingsboard.server.actors.ruleChain;

import akka.actor.ActorRef;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.service.ContextAwareActor;
import org.thingsboard.server.actors.shared.rulechain.RuleChainManager;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.RuleChainId;
import org.thingsboard.server.dao.rule.RuleChainService;

/**
 * Created by ashvayka on 15.03.18.
 */
public abstract class RuleChainManagerActor extends ContextAwareActor {

    protected final RuleChainManager ruleChainManager;
    protected final RuleChainService ruleChainService;

    public RuleChainManagerActor(ActorSystemContext systemContext, RuleChainManager ruleChainManager) {
        super(systemContext);
        this.ruleChainManager = ruleChainManager;
        this.ruleChainService = systemContext.getRuleChainService();
    }

    protected void initRuleChains() {
        ruleChainManager.init(this.context());
    }

    protected ActorRef getEntityActorRef(EntityId entityId) {
        ActorRef target = null;
        switch (entityId.getEntityType()) {
            case RULE_CHAIN:
                target = ruleChainManager.getOrCreateActor(this.context(), (RuleChainId) entityId);
                break;
        }
        return target;
    }

    protected void broadcast(Object msg) {
        ruleChainManager.broadcast(msg);
    }
}
