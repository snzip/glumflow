package org.thingsboard.server.actors.device;

import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.service.ContextBasedCreator;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.TenantId;

public class DeviceActorCreator extends ContextBasedCreator<DeviceActor> {
    private static final long serialVersionUID = 1L;

    private final TenantId tenantId;
    private final DeviceId deviceId;

    public DeviceActorCreator(ActorSystemContext context, TenantId tenantId, DeviceId deviceId) {
        super(context);
        this.tenantId = tenantId;
        this.deviceId = deviceId;
    }

    @Override
    public DeviceActor create() {
        return new DeviceActor(context, tenantId, deviceId);
    }
}
