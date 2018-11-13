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
public interface AttributesDao {

    ListenableFuture<Optional<AttributeKvEntry>> find(EntityId entityId, String attributeType, String attributeKey);

    ListenableFuture<List<AttributeKvEntry>> find(EntityId entityId, String attributeType, Collection<String> attributeKey);

    ListenableFuture<List<AttributeKvEntry>> findAll(EntityId entityId, String attributeType);

    ListenableFuture<Void> save(EntityId entityId, String attributeType, AttributeKvEntry attribute);

    ListenableFuture<List<Void>> removeAll(EntityId entityId, String attributeType, List<String> keys);
}
