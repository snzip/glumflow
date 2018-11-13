package org.thingsboard.server.dao.audit;

import lombok.Getter;
import org.thingsboard.server.common.data.page.TimePageLink;
import org.thingsboard.server.dao.model.nosql.AuditLogEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuditLogQueryCursor {
    @Getter
    private final UUID tenantId;
    @Getter
    private final List<AuditLogEntity> data;
    @Getter
    private final TimePageLink pageLink;

    private final List<Long> partitions;

    private int partitionIndex;
    private int currentLimit;

    public AuditLogQueryCursor(UUID tenantId, TimePageLink pageLink, List<Long> partitions) {
        this.tenantId = tenantId;
        this.partitions = partitions;
        this.partitionIndex = partitions.size() - 1;
        this.data = new ArrayList<>();
        this.currentLimit = pageLink.getLimit();
        this.pageLink = pageLink;
    }

    public boolean hasNextPartition() {
        return partitionIndex >= 0;
    }

    public boolean isFull() {
        return currentLimit <= 0;
    }

    public long getNextPartition() {
        long partition = partitions.get(partitionIndex);
        partitionIndex--;
        return partition;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public void addData(List<AuditLogEntity> newData) {
        currentLimit -= newData.size();
        data.addAll(newData);
    }
}
