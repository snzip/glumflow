package org.thingsboard.server.dao.attributes;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrew Shvayka
 */
public interface AttributesService {

    ListenableFuture<Optional<AttributeKvEntry>> find(EntityId entityId, String scope, String attributeKey);

    ListenableFuture<List<AttributeKvEntry>> find(EntityId entityId, String scope, Collection<String> attributeKeys);

    ListenableFuture<List<AttributeKvEntry>> findAll(EntityId entityId, String scope);

    ListenableFuture<List<Void>> save(EntityId entityId, String scope, List<AttributeKvEntry> attributes);

    ListenableFuture<List<Void>> removeAll(EntityId entityId, String scope, List<String> attributeKeys);
}
