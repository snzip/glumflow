package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.alarm.AlarmSeverity;

public class AlarmSeverityCodec extends EnumNameCodec<AlarmSeverity> {

    public AlarmSeverityCodec() {
        super(AlarmSeverity.class);
    }

}
