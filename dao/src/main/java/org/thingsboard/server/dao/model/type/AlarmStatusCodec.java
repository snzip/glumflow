package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.alarm.AlarmStatus;

public class AlarmStatusCodec extends EnumNameCodec<AlarmStatus> {

    public AlarmStatusCodec() {
        super(AlarmStatus.class);
    }

}
