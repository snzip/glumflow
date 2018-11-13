package org.thingsboard.server.dao.timeseries;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.kv.DeleteTsKvQuery;
import org.thingsboard.server.common.data.kv.ReadTsKvQuery;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;

/**
 * @author Andrew Shvayka
 */
public interface TimeseriesDao {

    ListenableFuture<List<TsKvEntry>> findAllAsync(EntityId entityId, List<ReadTsKvQuery> queries);

    ListenableFuture<TsKvEntry> findLatest(EntityId entityId, String key);

    ListenableFuture<List<TsKvEntry>> findAllLatest(EntityId entityId);

    ListenableFuture<Void> save(EntityId entityId, TsKvEntry tsKvEntry, long ttl);

    ListenableFuture<Void> savePartition(EntityId entityId, long tsKvEntryTs, String key, long ttl);

    ListenableFuture<Void> saveLatest(EntityId entityId, TsKvEntry tsKvEntry);

    ListenableFuture<Void> remove(EntityId entityId, DeleteTsKvQuery query);

    ListenableFuture<Void> removeLatest(EntityId entityId, DeleteTsKvQuery query);

    ListenableFuture<Void> removePartition(EntityId entityId, DeleteTsKvQuery query);
}
