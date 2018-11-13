package org.thingsboard.server.dao.sql.audit;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.thingsboard.server.dao.model.sql.AuditLogEntity;

public interface AuditLogRepository extends CrudRepository<AuditLogEntity, String>, JpaSpecificationExecutor<AuditLogEntity> {

}
