package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thingsboard.server.common.data.EntityType;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsKvCompositeKey implements Serializable {

    @Transient
    private static final long serialVersionUID = -4089175869616037523L;

    private EntityType entityType;
    private String entityId;
    private String key;
    private long ts;
}