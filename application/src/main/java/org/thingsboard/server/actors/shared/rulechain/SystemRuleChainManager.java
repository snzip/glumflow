package org.thingsboard.server.actors.shared.rulechain;

import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.service.DefaultActorService;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.PageDataIterable.FetchFunction;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.rule.RuleChain;
import org.thingsboard.server.dao.model.ModelConstants;

import java.util.Collections;

public class SystemRuleChainManager extends RuleChainManager {

    public SystemRuleChainManager(ActorSystemContext systemContext) {
        super(systemContext);
    }

    @Override
    protected FetchFunction<RuleChain> getFetchEntitiesFunction() {
        return link -> new TextPageData<>(Collections.emptyList(), link);
    }

    @Override
    protected TenantId getTenantId() {
        return ModelConstants.SYSTEM_TENANT;
    }

    @Override
    protected String getDispatcherName() {
        return DefaultActorService.SYSTEM_RULE_DISPATCHER_NAME;
    }
}
