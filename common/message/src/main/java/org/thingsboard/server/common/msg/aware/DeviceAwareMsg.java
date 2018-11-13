package org.thingsboard.server.common.msg.aware;

import org.thingsboard.server.common.data.id.DeviceId;

public interface DeviceAwareMsg {

    DeviceId getDeviceId();
}
