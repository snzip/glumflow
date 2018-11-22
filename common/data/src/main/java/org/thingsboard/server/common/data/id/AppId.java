package org.thingsboard.server.common.data.id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.thingsboard.server.common.data.EntityType;

import java.util.UUID;

public class AppId extends UUIDBased implements EntityId {

    private static final long serialVersionUID = 1L;

    @JsonCreator
    public AppId(@JsonProperty("id") UUID id) {
        super(id);
    }

    public static AppId fromString(String appId) {
        return new AppId(UUID.fromString(appId));
    }

    @JsonIgnore
    @Override
    public EntityType getEntityType() {
        return EntityType.APP;
    }
}
