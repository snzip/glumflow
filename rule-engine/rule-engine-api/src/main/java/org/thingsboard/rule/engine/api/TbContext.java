package org.thingsboard.rule.engine.api;

import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.RuleNodeId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.rule.RuleNode;
import org.thingsboard.server.common.msg.TbMsg;
import org.thingsboard.server.common.msg.TbMsgMetaData;
import org.thingsboard.server.dao.alarm.AlarmService;
import org.thingsboard.server.dao.asset.AssetService;
import org.thingsboard.server.dao.attributes.AttributesService;
import org.thingsboard.server.dao.customer.CustomerService;
import org.thingsboard.server.dao.device.DeviceService;
import org.thingsboard.server.dao.entityview.EntityViewService;
import org.thingsboard.server.dao.relation.RelationService;
import org.thingsboard.server.dao.rule.RuleChainService;
import org.thingsboard.server.dao.tenant.TenantService;
import org.thingsboard.server.dao.timeseries.TimeseriesService;
import org.thingsboard.server.dao.user.UserService;

import java.util.Set;

/**
 * Created by ashvayka on 13.01.18.
 */
public interface TbContext {

    void tellNext(TbMsg msg, String relationType);

    void tellNext(TbMsg msg, String relationType, Throwable th);

    void tellNext(TbMsg msg, Set<String> relationTypes);

    void tellSelf(TbMsg msg, long delayMs);

    void tellFailure(TbMsg msg, Throwable th);

    void updateSelf(RuleNode self);

    TbMsg newMsg(String type, EntityId originator, TbMsgMetaData metaData, String data);

    TbMsg transformMsg(TbMsg origMsg, String type, EntityId originator, TbMsgMetaData metaData, String data);

    RuleNodeId getSelfId();

    TenantId getTenantId();

    AttributesService getAttributesService();

    CustomerService getCustomerService();

    TenantService getTenantService();

    UserService getUserService();

    AssetService getAssetService();

    DeviceService getDeviceService();

    AlarmService getAlarmService();

    RuleChainService getRuleChainService();

    RuleEngineRpcService getRpcService();

    RuleEngineTelemetryService getTelemetryService();

    TimeseriesService getTimeseriesService();

    RelationService getRelationService();

    EntityViewService getEntityViewService();

    ListeningExecutor getJsExecutor();

    ListeningExecutor getMailExecutor();

    ListeningExecutor getDbCallbackExecutor();

    ListeningExecutor getExternalCallExecutor();

    MailService getMailService();

    ScriptEngine createJsScriptEngine(String script, String... argNames);

}
