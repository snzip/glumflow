package org.thingsboard.server.dao.entity;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.id.EntityId;

public interface EntityService {

    ListenableFuture<String> fetchEntityNameAsync(EntityId entityId);

    void deleteEntityRelations(EntityId entityId);

}
