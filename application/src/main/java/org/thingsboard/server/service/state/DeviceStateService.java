package org.thingsboard.server.service.state;

import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.msg.cluster.ServerAddress;

import java.util.Optional;

/**
 * Created by ashvayka on 01.05.18.
 */
public interface DeviceStateService {

    void onDeviceAdded(Device device);

    void onDeviceUpdated(Device device);

    void onDeviceDeleted(Device device);

    void onDeviceConnect(DeviceId deviceId);

    void onDeviceActivity(DeviceId deviceId);

    void onDeviceDisconnect(DeviceId deviceId);

    void onDeviceInactivityTimeoutUpdate(DeviceId deviceId, long inactivityTimeout);

    void onClusterUpdate();

    void onRemoteMsg(ServerAddress serverAddress, byte[] bytes);
}
