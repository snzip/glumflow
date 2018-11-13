package org.thingsboard.server.dao.rule;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.id.RuleChainId;
import org.thingsboard.server.common.data.id.RuleNodeId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.relation.EntityRelation;
import org.thingsboard.server.common.data.rule.RuleChain;
import org.thingsboard.server.common.data.rule.RuleChainMetaData;
import org.thingsboard.server.common.data.rule.RuleNode;

import java.util.List;

/**
 * Created by igor on 3/12/18.
 */
public interface RuleChainService {

    RuleChain saveRuleChain(RuleChain ruleChain);

    boolean setRootRuleChain(RuleChainId ruleChainId);

    RuleChainMetaData saveRuleChainMetaData(RuleChainMetaData ruleChainMetaData);

    RuleChainMetaData loadRuleChainMetaData(RuleChainId ruleChainId);

    RuleChain findRuleChainById(RuleChainId ruleChainId);

    RuleNode findRuleNodeById(RuleNodeId ruleNodeId);

    ListenableFuture<RuleChain> findRuleChainByIdAsync(RuleChainId ruleChainId);

    ListenableFuture<RuleNode> findRuleNodeByIdAsync(RuleNodeId ruleNodeId);

    RuleChain getRootTenantRuleChain(TenantId tenantId);

    List<RuleNode> getRuleChainNodes(RuleChainId ruleChainId);

    List<EntityRelation> getRuleNodeRelations(RuleNodeId ruleNodeId);

    TextPageData<RuleChain> findTenantRuleChains(TenantId tenantId, TextPageLink pageLink);

    void deleteRuleChainById(RuleChainId ruleChainId);

    void deleteRuleChainsByTenantId(TenantId tenantId);

}
