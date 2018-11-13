package org.thingsboard.rule.engine.api;

import com.google.common.util.concurrent.FutureCallback;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;
import java.util.Set;

/**
 * Created by ashvayka on 02.04.18.
 */
public interface RuleEngineTelemetryService {

    void saveAndNotify(EntityId entityId, List<TsKvEntry> ts, FutureCallback<Void> callback);

    void saveAndNotify(EntityId entityId, List<TsKvEntry> ts, long ttl, FutureCallback<Void> callback);

    void saveAndNotify(EntityId entityId, String scope, List<AttributeKvEntry> attributes, FutureCallback<Void> callback);

    void saveAttrAndNotify(EntityId entityId, String scope, String key, long value, FutureCallback<Void> callback);

    void saveAttrAndNotify(EntityId entityId, String scope, String key, String value, FutureCallback<Void> callback);

    void saveAttrAndNotify(EntityId entityId, String scope, String key, double value, FutureCallback<Void> callback);

    void saveAttrAndNotify(EntityId entityId, String scope, String key, boolean value, FutureCallback<Void> callback);

    void onSharedAttributesUpdate(TenantId tenantId, DeviceId deviceId, Set<AttributeKvEntry> attributes);

}
