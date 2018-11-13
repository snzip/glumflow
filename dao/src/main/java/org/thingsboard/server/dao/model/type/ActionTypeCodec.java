package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.audit.ActionType;

public class ActionTypeCodec extends EnumNameCodec<ActionType> {

    public ActionTypeCodec() {
        super(ActionType.class);
    }
}