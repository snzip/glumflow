package org.thingsboard.server.service.telemetry;

import org.thingsboard.rule.engine.api.RuleEngineTelemetryService;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.service.telemetry.sub.SubscriptionState;

/**
 * Created by ashvayka on 27.03.18.
 */
public interface TelemetrySubscriptionService extends RuleEngineTelemetryService {

    void addLocalWsSubscription(String sessionId, EntityId entityId, SubscriptionState sub);

    void cleanupLocalWsSessionSubscriptions(TelemetryWebSocketSessionRef sessionRef, String sessionId);

    void removeSubscription(String sessionId, int cmdId);

    void onNewRemoteSubscription(ServerAddress serverAddress, byte[] data);

    void onRemoteSubscriptionUpdate(ServerAddress serverAddress, byte[] bytes);

    void onRemoteSubscriptionClose(ServerAddress serverAddress, byte[] bytes);

    void onRemoteSessionClose(ServerAddress serverAddress, byte[] bytes);

    void onRemoteAttributesUpdate(ServerAddress serverAddress, byte[] bytes);

    void onRemoteTsUpdate(ServerAddress serverAddress, byte[] bytes);

    void onClusterUpdate();
}
