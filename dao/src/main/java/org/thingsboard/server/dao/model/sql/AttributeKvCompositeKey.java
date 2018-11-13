package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thingsboard.server.common.data.EntityType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeKvCompositeKey implements Serializable {
    private EntityType entityType;
    private String entityId;
    private String attributeType;
    private String attributeKey;
}
