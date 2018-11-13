package org.thingsboard.server.dao.model;

import lombok.Data;
import org.thingsboard.server.common.data.UUIDConverter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Created by ashvayka on 13.07.17.
 */
@Data
@MappedSuperclass
public abstract class BaseSqlEntity<D> implements BaseEntity<D> {

    @Id
    @Column(name = ModelConstants.ID_PROPERTY)
    protected String id;

    @Override
    public UUID getId() {
        if (id == null) {
            return null;
        }
        return UUIDConverter.fromString(id);
    }

    public void setId(UUID id) {
        this.id = UUIDConverter.fromTimeUUID(id);
    }

    protected UUID toUUID(String src){
        return UUIDConverter.fromString(src);
    }

    protected String toString(UUID timeUUID){
        return UUIDConverter.fromTimeUUID(timeUUID);
    }

}
