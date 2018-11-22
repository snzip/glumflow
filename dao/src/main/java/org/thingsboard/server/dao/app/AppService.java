package org.thingsboard.server.dao.app;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.App;
import org.thingsboard.server.common.data.EntitySubtype;
//import org.thingsboard.server.common.data.App.AppSearchQuery;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.AppId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.util.List;

public interface AppService {

    App findAppById(AppId AppId);

    ListenableFuture<App> findAppByIdAsync(AppId AppId);

    App findAppByTenantIdAndName(TenantId tenantId, String name);

    App saveApp(App App);

    App assignAppToCustomer(AppId AppId, CustomerId customerId);

    App unassignAppFromCustomer(AppId AppId);

    void deleteApp(AppId AppId);

    TextPageData<App> findAppsByTenantId(TenantId tenantId, TextPageLink pageLink);

    TextPageData<App> findAppsByTenantIdAndType(TenantId tenantId, String type, TextPageLink pageLink);

    ListenableFuture<List<App>> findAppsByTenantIdAndIdsAsync(TenantId tenantId, List<AppId> AppIds);

    void deleteAppsByTenantId(TenantId tenantId);

    TextPageData<App> findAppsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink);

    TextPageData<App> findAppsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, String type, TextPageLink pageLink);

//    ListenableFuture<List<App>> findAppsByTenantIdCustomerIdAndIdsAsync(TenantId tenantId, CustomerId customerId, List<AppId> AppIds);

    void unassignCustomerApps(TenantId tenantId, CustomerId customerId);

//    ListenableFuture<List<App>> findAppsByQuery(AppSearchQuery query);

    ListenableFuture<List<EntitySubtype>> findAppTypesByTenantId(TenantId tenantId);

}
