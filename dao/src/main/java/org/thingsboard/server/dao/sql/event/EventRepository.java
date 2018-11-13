package org.thingsboard.server.dao.sql.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.dao.model.sql.EventEntity;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;

/**
 * Created by Valerii Sosliuk on 5/3/2017.
 */
@SqlDao
public interface EventRepository extends CrudRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {

    EventEntity findByTenantIdAndEntityTypeAndEntityIdAndEventTypeAndEventUid(String tenantId,
                                                                              EntityType entityType,
                                                                              String entityId,
                                                                              String eventType,
                                                                              String eventUid);

    EventEntity findByTenantIdAndEntityTypeAndEntityId(String tenantId,
                                                       EntityType entityType,
                                                       String entityId);

    @Query("SELECT e FROM EventEntity e WHERE e.tenantId = :tenantId AND e.entityType = :entityType " +
            "AND e.entityId = :entityId AND e.eventType = :eventType ORDER BY e.eventType DESC, e.id DESC")
    List<EventEntity> findLatestByTenantIdAndEntityTypeAndEntityIdAndEventType(
                                                    @Param("tenantId") String tenantId,
                                                    @Param("entityType") EntityType entityType,
                                                    @Param("entityId") String entityId,
                                                    @Param("eventType") String eventType,
                                                    Pageable pageable);

}
