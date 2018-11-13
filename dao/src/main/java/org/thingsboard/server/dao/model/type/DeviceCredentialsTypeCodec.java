package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.security.DeviceCredentialsType;

public class DeviceCredentialsTypeCodec extends EnumNameCodec<DeviceCredentialsType> {

    public DeviceCredentialsTypeCodec() {
        super(DeviceCredentialsType.class);
    }

}
