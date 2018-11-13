package org.thingsboard.server.dao.alarm;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.alarm.Alarm;
import org.thingsboard.server.common.data.alarm.AlarmId;
import org.thingsboard.server.common.data.alarm.AlarmInfo;
import org.thingsboard.server.common.data.alarm.AlarmQuery;
import org.thingsboard.server.common.data.alarm.AlarmSearchStatus;
import org.thingsboard.server.common.data.alarm.AlarmSeverity;
import org.thingsboard.server.common.data.alarm.AlarmStatus;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TimePageData;

/**
 * Created by ashvayka on 11.05.17.
 */
public interface AlarmService {

    Alarm createOrUpdateAlarm(Alarm alarm);

    ListenableFuture<Boolean> ackAlarm(AlarmId alarmId, long ackTs);

    ListenableFuture<Boolean> clearAlarm(AlarmId alarmId, JsonNode details, long ackTs);

    ListenableFuture<Alarm> findAlarmByIdAsync(AlarmId alarmId);

    ListenableFuture<AlarmInfo> findAlarmInfoByIdAsync(AlarmId alarmId);

    ListenableFuture<TimePageData<AlarmInfo>> findAlarms(AlarmQuery query);

    AlarmSeverity findHighestAlarmSeverity(EntityId entityId, AlarmSearchStatus alarmSearchStatus,
                                           AlarmStatus alarmStatus);

    ListenableFuture<Alarm> findLatestByOriginatorAndType(TenantId tenantId, EntityId originator, String type);

}
