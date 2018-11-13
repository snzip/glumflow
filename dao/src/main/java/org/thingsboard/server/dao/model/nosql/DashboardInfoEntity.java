package org.thingsboard.server.dao.model.nosql;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.thingsboard.server.common.data.DashboardInfo;
import org.thingsboard.server.common.data.ShortCustomerInfo;
import org.thingsboard.server.common.data.id.DashboardId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.SearchTextEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.DASHBOARD_ASSIGNED_CUSTOMERS_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DASHBOARD_COLUMN_FAMILY_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.DASHBOARD_TENANT_ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.DASHBOARD_TITLE_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.SEARCH_TEXT_PROPERTY;

@Table(name = DASHBOARD_COLUMN_FAMILY_NAME)
@EqualsAndHashCode
@ToString
@Slf4j
public class DashboardInfoEntity implements SearchTextEntity<DashboardInfo> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JavaType assignedCustomersType =
            objectMapper.getTypeFactory().constructCollectionType(HashSet.class, ShortCustomerInfo.class);

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;

    @PartitionKey(value = 1)
    @Column(name = DASHBOARD_TENANT_ID_PROPERTY)
    private UUID tenantId;

    @Column(name = DASHBOARD_TITLE_PROPERTY)
    private String title;

    @Column(name = SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Column(name = DASHBOARD_ASSIGNED_CUSTOMERS_PROPERTY)
    private String assignedCustomers;

    public DashboardInfoEntity() {
        super();
    }

    public DashboardInfoEntity(DashboardInfo dashboardInfo) {
        if (dashboardInfo.getId() != null) {
            this.id = dashboardInfo.getId().getId();
        }
        if (dashboardInfo.getTenantId() != null) {
            this.tenantId = dashboardInfo.getTenantId().getId();
        }
        this.title = dashboardInfo.getTitle();
        if (dashboardInfo.getAssignedCustomers() != null) {
            try {
                this.assignedCustomers = objectMapper.writeValueAsString(dashboardInfo.getAssignedCustomers());
            } catch (JsonProcessingException e) {
                log.error("Unable to serialize assigned customers to string!", e);
            }
        }
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedCustomers() {
        return assignedCustomers;
    }

    public void setAssignedCustomers(String assignedCustomers) {
        this.assignedCustomers = assignedCustomers;
    }

    @Override
    public String getSearchTextSource() {
        return getTitle();
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    @Override
    public DashboardInfo toData() {
        DashboardInfo dashboardInfo = new DashboardInfo(new DashboardId(id));
        dashboardInfo.setCreatedTime(UUIDs.unixTimestamp(id));
        if (tenantId != null) {
            dashboardInfo.setTenantId(new TenantId(tenantId));
        }
        dashboardInfo.setTitle(title);
        if (!StringUtils.isEmpty(assignedCustomers)) {
            try {
                dashboardInfo.setAssignedCustomers(objectMapper.readValue(assignedCustomers, assignedCustomersType));
            } catch (IOException e) {
                log.warn("Unable to parse assigned customers!", e);
            }
        }
        return dashboardInfo;
    }

}