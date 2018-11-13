package org.thingsboard.rule.engine.action;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.rule.engine.api.util.TbNodeUtils;
import org.thingsboard.rule.engine.api.*;
import org.thingsboard.server.common.data.plugin.ComponentType;
import org.thingsboard.server.common.msg.TbMsg;

import static org.thingsboard.rule.engine.api.util.DonAsynchron.withCallback;
import static org.thingsboard.rule.engine.api.TbRelationTypes.SUCCESS;

@Slf4j
@RuleNode(
        type = ComponentType.ACTION,
        name = "log",
        configClazz = TbLogNodeConfiguration.class,
        nodeDescription = "Log incoming messages using JS script for transformation Message into String",
        nodeDetails = "Transform incoming Message with configured JS function to String and log final value into Thingsboard log file. " +
                "Message payload can be accessed via <code>msg</code> property. For example <code>'temperature = ' + msg.temperature ;</code>. " +
                "Message metadata can be accessed via <code>metadata</code> property. For example <code>'name = ' + metadata.customerName;</code>.",
        uiResources = {"static/rulenode/rulenode-core-config.js"},
        configDirective = "tbActionNodeLogConfig",
        icon = "menu"
)

public class TbLogNode implements TbNode {

    private TbLogNodeConfiguration config;
    private ScriptEngine jsEngine;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, TbLogNodeConfiguration.class);
        this.jsEngine = ctx.createJsScriptEngine(config.getJsScript());
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) {
        ListeningExecutor jsExecutor = ctx.getJsExecutor();
        withCallback(jsExecutor.executeAsync(() -> jsEngine.executeToString(msg)),
                toString -> {
                    log.info(toString);
                    ctx.tellNext(msg, SUCCESS);
                },
                t -> ctx.tellFailure(msg, t));
    }

    @Override
    public void destroy() {
        if (jsEngine != null) {
            jsEngine.destroy();
        }
    }
}
