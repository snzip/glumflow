package org.thingsboard.server.dao.model.sql;


import com.datastax.driver.core.utils.UUIDs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.thingsboard.server.common.data.UUIDConverter;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.id.WidgetsBundleId;
import org.thingsboard.server.common.data.widget.WidgetsBundle;
import org.thingsboard.server.dao.model.BaseSqlEntity;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.model.SearchTextEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = ModelConstants.WIDGETS_BUNDLE_COLUMN_FAMILY_NAME)
public final class WidgetsBundleEntity extends BaseSqlEntity<WidgetsBundle> implements SearchTextEntity<WidgetsBundle> {

    @Column(name = ModelConstants.WIDGETS_BUNDLE_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.WIDGETS_BUNDLE_ALIAS_PROPERTY)
    private String alias;

    @Column(name = ModelConstants.WIDGETS_BUNDLE_TITLE_PROPERTY)
    private String title;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    public WidgetsBundleEntity() {
        super();
    }

    public WidgetsBundleEntity(WidgetsBundle widgetsBundle) {
        if (widgetsBundle.getId() != null) {
            this.setId(widgetsBundle.getId().getId());
        }
        if (widgetsBundle.getTenantId() != null) {
            this.tenantId = UUIDConverter.fromTimeUUID(widgetsBundle.getTenantId().getId());
        }
        this.alias = widgetsBundle.getAlias();
        this.title = widgetsBundle.getTitle();
    }

    @Override
    public String getSearchTextSource() {
        return title;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public WidgetsBundle toData() {
        WidgetsBundle widgetsBundle = new WidgetsBundle(new WidgetsBundleId(UUIDConverter.fromString(id)));
        widgetsBundle.setCreatedTime(UUIDs.unixTimestamp(UUIDConverter.fromString(id)));
        if (tenantId != null) {
            widgetsBundle.setTenantId(new TenantId(UUIDConverter.fromString(tenantId)));
        }
        widgetsBundle.setAlias(alias);
        widgetsBundle.setTitle(title);
        return widgetsBundle;
    }
}
