package org.thingsboard.server.common.transport;

import org.thingsboard.server.common.data.Device;

public interface SessionMsgProcessor {

    void onDeviceAdded(Device device);

}
