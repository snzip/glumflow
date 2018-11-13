package org.thingsboard.server.service.telemetry.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.service.telemetry.TelemetryFeature;

import java.util.Map;

@Data
@AllArgsConstructor
public class Subscription {

    private final SubscriptionState sub;
    private final boolean local;
    private ServerAddress server;
    private long startTime;
    private long endTime;

    public Subscription(SubscriptionState sub, boolean local, ServerAddress server) {
        this(sub, local, server, 0L, 0L);
    }

    public String getWsSessionId() {
        return getSub().getWsSessionId();
    }

    public int getSubscriptionId() {
        return getSub().getSubscriptionId();
    }

    public EntityId getEntityId() {
        return getSub().getEntityId();
    }

    public TelemetryFeature getType() {
        return getSub().getType();
    }

    public String getScope() {
        return getSub().getScope();
    }

    public boolean isAllKeys() {
        return getSub().isAllKeys();
    }

    public Map<String, Long> getKeyStates() {
        return getSub().getKeyStates();
    }

    public void setKeyState(String key, long ts) {
        getSub().getKeyStates().put(key, ts);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "sub=" + sub +
                ", local=" + local +
                ", server=" + server +
                '}';
    }
}
