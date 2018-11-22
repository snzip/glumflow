package org.thingsboard.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.common.data.App;
import org.thingsboard.server.common.data.App;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.AppId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.BaseEntity;
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
@Table(name = "apps")
public final class AppEntity extends BaseSqlEntity<App> implements SearchTextEntity<App>  {

    @Column(name = ModelConstants.DEVICE_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.DEVICE_TYPE_PROPERTY)
    private String type;

    @Column(name = ModelConstants.DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Type(type = "json")
    @Column(name = ModelConstants.DEVICE_ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    public AppEntity() {
        super();
    }

    public AppEntity(App app) {
        if (app.getId() != null) {
            this.setId(app.getId().getId());
        }
        if (app.getTenantId() != null) {
            this.tenantId = toString(app.getTenantId().getId());
        }
//        if (app.getCustomerId() != null) {
//            this.customerId = toString(app.getCustomerId().getId());
//        }
        this.name = app.getName();
        this.type = app.getType();
        this.additionalInfo = app.getAdditionalInfo();
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
    public App toData() {
        App app = new App(new AppId(getId()));
        app.setCreatedTime(UUIDs.unixTimestamp(getId()));
        if (tenantId != null) {
            app.setTenantId(new TenantId(toUUID(tenantId)));
        }
//        if (customerId != null) {
//            app.setCustomerId(new CustomerId(toUUID(customerId)));
//        }
        app.setName(name);
        app.setType(type);
        app.setAdditionalInfo(additionalInfo);
        return app;
    }
}
