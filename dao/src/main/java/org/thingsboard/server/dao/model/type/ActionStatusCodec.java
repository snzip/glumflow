package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.audit.ActionStatus;

public class ActionStatusCodec extends EnumNameCodec<ActionStatus> {

    public ActionStatusCodec() {
        super(ActionStatus.class);
    }
}