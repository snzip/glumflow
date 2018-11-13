package org.thingsboard.server.dao.device;

import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.security.DeviceCredentials;

public interface DeviceCredentialsService {

    DeviceCredentials findDeviceCredentialsByDeviceId(DeviceId deviceId);

    DeviceCredentials findDeviceCredentialsByCredentialsId(String credentialsId);

    DeviceCredentials updateDeviceCredentials(DeviceCredentials deviceCredentials);

    DeviceCredentials createDeviceCredentials(DeviceCredentials deviceCredentials);

    void deleteDeviceCredentials(DeviceCredentials deviceCredentials);
}
