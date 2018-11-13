package org.thingsboard.server.dao.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.thingsboard.server.common.data.security.DeviceCredentials;
import org.thingsboard.server.dao.device.DeviceCredentialsService;

import java.lang.reflect.Method;

public class PreviousDeviceCredentialsIdKeyGenerator implements KeyGenerator {

    private static final String NOT_VALID_DEVICE = "notValidDeviceCredentialsId";

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        DeviceCredentialsService deviceCredentialsService = (DeviceCredentialsService) o;
        DeviceCredentials deviceCredentials = (DeviceCredentials) objects[0];
        if (deviceCredentials.getDeviceId() != null) {
            DeviceCredentials oldDeviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(deviceCredentials.getDeviceId());
            if (oldDeviceCredentials != null) {
                return oldDeviceCredentials.getCredentialsId();
            }
        }
        return NOT_VALID_DEVICE;
    }
}
