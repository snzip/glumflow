package org.thingsboard.server.dao.dashboard;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.Dashboard;
import org.thingsboard.server.common.data.DashboardInfo;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.DashboardId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.page.TimePageData;
import org.thingsboard.server.common.data.page.TimePageLink;

public interface DashboardService {
    
    Dashboard findDashboardById(DashboardId dashboardId);

    ListenableFuture<Dashboard> findDashboardByIdAsync(DashboardId dashboardId);

    DashboardInfo findDashboardInfoById(DashboardId dashboardId);

    ListenableFuture<DashboardInfo> findDashboardInfoByIdAsync(DashboardId dashboardId);

    Dashboard saveDashboard(Dashboard dashboard);

    Dashboard assignDashboardToCustomer(DashboardId dashboardId, CustomerId customerId);

    Dashboard unassignDashboardFromCustomer(DashboardId dashboardId, CustomerId customerId);

    void deleteDashboard(DashboardId dashboardId);

    TextPageData<DashboardInfo> findDashboardsByTenantId(TenantId tenantId, TextPageLink pageLink);

    void deleteDashboardsByTenantId(TenantId tenantId);

    ListenableFuture<TimePageData<DashboardInfo>> findDashboardsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TimePageLink pageLink);

    void unassignCustomerDashboards(CustomerId customerId);

    void updateCustomerDashboards(CustomerId customerId);

}
