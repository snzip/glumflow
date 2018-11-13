package org.thingsboard.rule.engine.filter;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.rule.engine.api.util.TbNodeUtils;
import org.thingsboard.rule.engine.api.*;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.plugin.ComponentType;
import org.thingsboard.server.common.msg.TbMsg;

@Slf4j
@RuleNode(
        type = ComponentType.FILTER,
        name = "originator type switch",
        configClazz = EmptyNodeConfiguration.class,
        relationTypes = {"Device", "Asset", "Tenant", "Customer", "User", "Dashboard", "Rule chain", "Rule node"},
        nodeDescription = "Route incoming messages by Message Originator Type",
        nodeDetails = "Routes messages to chain according to the originator type ('Device', 'Asset', etc.).",
        uiResources = {"static/rulenode/rulenode-core-config.js"},
        configDirective = "tbNodeEmptyConfig")
public class TbOriginatorTypeSwitchNode implements TbNode {

    EmptyNodeConfiguration config;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, EmptyNodeConfiguration.class);
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) throws TbNodeException {
        String relationType;
        EntityType originatorType = msg.getOriginator().getEntityType();
        switch (originatorType) {
            case TENANT:
                relationType = "Tenant";
                break;
            case CUSTOMER:
                relationType = "Customer";
                break;
            case USER:
                relationType = "User";
                break;
            case DASHBOARD:
                relationType = "Dashboard";
                break;
            case ASSET:
                relationType = "Asset";
                break;
            case DEVICE:
                relationType = "Device";
                break;
            case RULE_CHAIN:
                relationType = "Rule chain";
                break;
            case RULE_NODE:
                relationType = "Rule node";
                break;
            default:
                throw new TbNodeException("Unsupported originator type: " + originatorType);
        }
        ctx.tellNext(msg, relationType);
    }

    @Override
    public void destroy() {

    }
}
