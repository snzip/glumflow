package org.thingsboard.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.BaseSqlEntity;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.model.SearchTextEntity;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.DEVICE_COLUMN_FAMILY_NAME)
public final class ModEntity extends BaseSqlEntity<Mod> implements SearchTextEntity<Mod> {

    @Column(name = ModelConstants.DEVICE_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.DEVICE_CUSTOMER_ID_PROPERTY)
    private String customerId;

    @Column(name = ModelConstants.DEVICE_TYPE_PROPERTY)
    private String type;

    @Column(name = ModelConstants.DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Type(type = "json")
    @Column(name = ModelConstants.DEVICE_ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    public ModEntity() {
        super();
    }

    public ModEntity(Mod mod) {
        if (mod.getId() != null) {
            this.setId(mod.getId().getId());
        }
        if (mod.getTenantId() != null) {
            this.tenantId = toString(mod.getTenantId().getId());
        }
        if (mod.getCustomerId() != null) {
            this.customerId = toString(mod.getCustomerId().getId());
        }
        this.name = mod.getName();
        this.type = mod.getType();
        this.additionalInfo = mod.getAdditionalInfo();
    }

    @Override
    public String getSearchTextSource() {
        return name;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public Mod toData() {
        Mod mod = new Mod(new ModId(getId()));
        mod.setCreatedTime(UUIDs.unixTimestamp(getId()));
        if (tenantId != null) {
            mod.setTenantId(new TenantId(toUUID(tenantId)));
        }
        if (customerId != null) {
            mod.setCustomerId(new CustomerId(toUUID(customerId)));
        }
        mod.setName(name);
        mod.setType(type);
        mod.setAdditionalInfo(additionalInfo);
        return mod;
    }
}