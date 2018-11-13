package org.thingsboard.server.dao.sql.attributes;

import org.springframework.data.repository.CrudRepository;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.dao.model.sql.AttributeKvCompositeKey;
import org.thingsboard.server.dao.model.sql.AttributeKvEntity;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;

@SqlDao
public interface AttributeKvRepository extends CrudRepository<AttributeKvEntity, AttributeKvCompositeKey> {

    List<AttributeKvEntity> findAllByEntityTypeAndEntityIdAndAttributeType(EntityType entityType,
                                                                           String entityId,
                                                                           String attributeType);
}

