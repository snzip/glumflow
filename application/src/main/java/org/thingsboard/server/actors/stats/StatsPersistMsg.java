package org.thingsboard.server.actors.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;

@AllArgsConstructor
@Getter
@ToString
public final class StatsPersistMsg {
    private long messagesProcessed;
    private long errorsOccurred;
    private TenantId tenantId;
    private EntityId entityId;
}
