package org.thingsboard.server.dao.audit;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.thingsboard.server.common.data.BaseData;
import org.thingsboard.server.common.data.HasName;
import org.thingsboard.server.common.data.audit.ActionType;
import org.thingsboard.server.common.data.audit.AuditLog;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.id.UUIDBased;
import org.thingsboard.server.common.data.id.UserId;
import org.thingsboard.server.common.data.page.TimePageData;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "audit_log", value = "enabled", havingValue = "false")
public class DummyAuditLogServiceImpl implements AuditLogService {

    @Override
    public TimePageData<AuditLog> findAuditLogsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TimePageLink pageLink) {
        return new TimePageData<>(null, pageLink);
    }

    @Override
    public TimePageData<AuditLog> findAuditLogsByTenantIdAndUserId(TenantId tenantId, UserId userId, TimePageLink pageLink) {
        return new TimePageData<>(null, pageLink);
    }

    @Override
    public TimePageData<AuditLog> findAuditLogsByTenantIdAndEntityId(TenantId tenantId, EntityId entityId, TimePageLink pageLink) {
        return new TimePageData<>(null, pageLink);
    }

    @Override
    public TimePageData<AuditLog> findAuditLogsByTenantId(TenantId tenantId, TimePageLink pageLink) {
        return new TimePageData<>(null, pageLink);
    }

    @Override
    public <E extends HasName, I extends EntityId> ListenableFuture<List<Void>> logEntityAction(TenantId tenantId, CustomerId customerId, UserId userId, String userName, I entityId, E entity, ActionType actionType, Exception e, Object... additionalInfo) {
        return null;
    }

}
