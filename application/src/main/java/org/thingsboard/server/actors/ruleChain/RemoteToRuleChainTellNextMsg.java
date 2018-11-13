package org.thingsboard.server.actors.ruleChain;

import lombok.Data;
import org.thingsboard.server.common.data.id.RuleChainId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.msg.MsgType;
import org.thingsboard.server.common.msg.aware.RuleChainAwareMsg;
import org.thingsboard.server.common.msg.aware.TenantAwareMsg;

import java.io.Serializable;

/**
 * Created by ashvayka on 19.03.18.
 */
@Data
final class RemoteToRuleChainTellNextMsg extends RuleNodeToRuleChainTellNextMsg implements TenantAwareMsg, RuleChainAwareMsg {

    private static final long serialVersionUID = 2459605482321657447L;
    private final TenantId tenantId;
    private final RuleChainId ruleChainId;

    public RemoteToRuleChainTellNextMsg(RuleNodeToRuleChainTellNextMsg original, TenantId tenantId, RuleChainId ruleChainId) {
        super(original.getOriginator(), original.getRelationTypes(), original.getMsg());
        this.tenantId = tenantId;
        this.ruleChainId = ruleChainId;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.REMOTE_TO_RULE_CHAIN_TELL_NEXT_MSG;
    }

}
