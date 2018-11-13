package org.thingsboard.server.dao.model.nosql;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.SearchTextEntity;
import org.thingsboard.server.dao.model.type.JsonCodec;

import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_ADDITIONAL_INFO_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_COLUMN_FAMILY_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_CUSTOMER_ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_NAME_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_TENANT_ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DEVICE_TYPE_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.SEARCH_TEXT_PROPERTY;

@Table(name = DEVICE_COLUMN_FAMILY_NAME)
@EqualsAndHashCode
@ToString
public final class DeviceEntity implements SearchTextEntity<Device> {

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;
    
    @PartitionKey(value = 1)
    @Column(name = DEVICE_TENANT_ID_PROPERTY)
    private UUID tenantId;

    @PartitionKey(value = 2)
    @Column(name = DEVICE_CUSTOMER_ID_PROPERTY)
    private UUID customerId;

    @PartitionKey(value = 3)
    @Column(name = DEVICE_TYPE_PROPERTY)
    private String type;

    @Column(name = DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = SEARCH_TEXT_PROPERTY)
    private String searchText;
    
    @Column(name = DEVICE_ADDITIONAL_INFO_PROPERTY, codec = JsonCodec.class)
    private JsonNode additionalInfo;

    public DeviceEntity() {
        super();
    }

    public DeviceEntity(Device device) {
        if (device.getId() != null) {
            this.id = device.getId().getId();
        }
        if (device.getTenantId() != null) {
            this.tenantId = device.getTenantId().getId();
        }
        if (device.getCustomerId() != null) {
            this.customerId = device.getCustomerId().getId();
        }
        this.name = device.getName();
        this.type = device.getType();
        this.additionalInfo = device.getAdditionalInfo();
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JsonNode additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    @Override
    public String getSearchTextSource() {
        return getName();
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
    public String getSearchText() {
        return searchText;
    }

    @Override
    public Device toData() {
        Device device = new Device(new DeviceId(id));
        device.setCreatedTime(UUIDs.unixTimestamp(id));
        if (tenantId != null) {
            device.setTenantId(new TenantId(tenantId));
        }
        if (customerId != null) {
            device.setCustomerId(new CustomerId(customerId));
        }
        device.setName(name);
        device.setType(type);
        device.setAdditionalInfo(additionalInfo);
        return device;
    }

}