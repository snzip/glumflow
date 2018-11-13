package org.thingsboard.server.actors.shared.rulechain;

import akka.actor.ActorContext;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.service.DefaultActorService;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.PageDataIterable.FetchFunction;
import org.thingsboard.server.common.data.rule.RuleChain;

public class TenantRuleChainManager extends RuleChainManager {

    private final TenantId tenantId;

    public TenantRuleChainManager(ActorSystemContext systemContext, TenantId tenantId) {
        super(systemContext);
        this.tenantId = tenantId;
    }

    @Override
    public void init(ActorContext context) {
        super.init(context);
    }

    @Override
    protected TenantId getTenantId() {
        return tenantId;
    }

    @Override
    protected String getDispatcherName() {
        return DefaultActorService.TENANT_RULE_DISPATCHER_NAME;
    }

    @Override
    protected FetchFunction<RuleChain> getFetchEntitiesFunction() {
        return link -> service.findTenantRuleChains(tenantId, link);
    }
}
