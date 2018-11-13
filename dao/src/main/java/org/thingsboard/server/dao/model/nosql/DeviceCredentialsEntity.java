package org.thingsboard.server.dao.model.nosql;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.thingsboard.server.common.data.id.DeviceCredentialsId;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.security.DeviceCredentials;
import org.thingsboard.server.common.data.security.DeviceCredentialsType;
import org.thingsboard.server.dao.model.BaseEntity;
import org.thingsboard.server.dao.model.type.DeviceCredentialsTypeCodec;

import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_CREDENTIALS_COLUMN_FAMILY_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_CREDENTIALS_CREDENTIALS_ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_CREDENTIALS_CREDENTIALS_TYPE_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_CREDENTIALS_CREDENTIALS_VALUE_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_CREDENTIALS_DEVICE_ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.ID_PROPERTY;

@Table(name = DEVICE_CREDENTIALS_COLUMN_FAMILY_NAME)
@EqualsAndHashCode
@ToString
public final class DeviceCredentialsEntity implements BaseEntity<DeviceCredentials> {

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;
    
    @Column(name = DEVICE_CREDENTIALS_DEVICE_ID_PROPERTY)
    private UUID deviceId;
    
    @Column(name = DEVICE_CREDENTIALS_CREDENTIALS_TYPE_PROPERTY, codec = DeviceCredentialsTypeCodec.class)
    private DeviceCredentialsType credentialsType;

    @Column(name = DEVICE_CREDENTIALS_CREDENTIALS_ID_PROPERTY)
    private String credentialsId;

    @Column(name = DEVICE_CREDENTIALS_CREDENTIALS_VALUE_PROPERTY)
    private String credentialsValue;

    public DeviceCredentialsEntity() {
        super();
    }

    public DeviceCredentialsEntity(DeviceCredentials deviceCredentials) {
        if (deviceCredentials.getId() != null) {
            this.id = deviceCredentials.getId().getId();
        }
        if (deviceCredentials.getDeviceId() != null) {
            this.deviceId = deviceCredentials.getDeviceId().getId();
        }
        this.credentialsType = deviceCredentials.getCredentialsType();
        this.credentialsId = deviceCredentials.getCredentialsId();
        this.credentialsValue = deviceCredentials.getCredentialsValue(); 
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceCredentialsType getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(DeviceCredentialsType credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    public String getCredentialsValue() {
        return credentialsValue;
    }

    public void setCredentialsValue(String credentialsValue) {
        this.credentialsValue = credentialsValue;
    }

    @Override
    public DeviceCredentials toData() {
        DeviceCredentials deviceCredentials = new DeviceCredentials(new DeviceCredentialsId(id));
        deviceCredentials.setCreatedTime(UUIDs.unixTimestamp(id));
        if (deviceId != null) {
            deviceCredentials.setDeviceId(new DeviceId(deviceId));
        }
        deviceCredentials.setCredentialsType(credentialsType);
        deviceCredentials.setCredentialsId(credentialsId);
        deviceCredentials.setCredentialsValue(credentialsValue);
        return deviceCredentials;
    }

}