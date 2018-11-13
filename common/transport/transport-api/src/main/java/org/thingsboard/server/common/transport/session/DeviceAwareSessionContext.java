package org.thingsboard.server.common.transport.session;

import lombok.Data;
import lombok.Getter;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.msg.session.SessionContext;
import org.thingsboard.server.gen.transport.TransportProtos.DeviceInfoProto;

import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
@Data
public abstract class DeviceAwareSessionContext implements SessionContext {

    @Getter
    protected final UUID sessionId;
    @Getter
    private volatile DeviceId deviceId;
    @Getter
    private volatile DeviceInfoProto deviceInfo;

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceInfo(DeviceInfoProto deviceInfo) {
        this.deviceInfo = deviceInfo;
        this.deviceId = new DeviceId(new UUID(deviceInfo.getDeviceIdMSB(), deviceInfo.getDeviceIdLSB()));
    }


    public boolean isConnected() {
        return deviceInfo != null;
    }
}
