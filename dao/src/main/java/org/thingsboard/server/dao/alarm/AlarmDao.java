package org.thingsboard.server.dao.alarm;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.alarm.Alarm;
import org.thingsboard.server.common.data.alarm.AlarmInfo;
import org.thingsboard.server.common.data.alarm.AlarmQuery;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.Dao;

import java.util.List;
import java.util.UUID;

/**
 * Created by ashvayka on 11.05.17.
 */
public interface AlarmDao extends Dao<Alarm> {

    ListenableFuture<Alarm> findLatestByOriginatorAndType(TenantId tenantId, EntityId originator, String type);

    ListenableFuture<Alarm> findAlarmByIdAsync(UUID key);

    Alarm save(Alarm alarm);

    ListenableFuture<List<AlarmInfo>> findAlarms(AlarmQuery query);
}
