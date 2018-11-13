package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thingsboard.server.common.data.UUIDConverter;
import org.thingsboard.server.common.data.relation.EntityRelation;

import javax.persistence.Transient;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RelationCompositeKey implements Serializable {

    @Transient
    private static final long serialVersionUID = -4089175869616037592L;

    private String fromId;
    private String fromType;
    private String toId;
    private String toType;
    private String relationType;
    private String relationTypeGroup;

    public RelationCompositeKey(EntityRelation relation) {
        this.fromId = UUIDConverter.fromTimeUUID(relation.getFrom().getId());
        this.fromType = relation.getFrom().getEntityType().name();
        this.toId = UUIDConverter.fromTimeUUID(relation.getTo().getId());
        this.toType = relation.getTo().getEntityType().name();
        this.relationType = relation.getType();
        this.relationTypeGroup = relation.getTypeGroup().name();
    }
}
