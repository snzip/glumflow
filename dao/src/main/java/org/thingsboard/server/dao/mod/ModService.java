package org.thingsboard.server.dao.mod;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.mod.ModSearchQuery;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.util.List;

public interface ModService {
    
    Mod findModById(ModId modId);

    ListenableFuture<Mod> findModByIdAsync(ModId modId);

    Mod findModByTenantIdAndName(TenantId tenantId, String name);

    Mod saveMod(Mod mod);

    Mod assignModToCustomer(ModId modId, CustomerId customerId);

    Mod unassignModFromCustomer(ModId modId);

    void deleteMod(ModId modId);

    TextPageData<Mod> findModsByTenantId(TenantId tenantId, TextPageLink pageLink);

    TextPageData<Mod> findModsByTenantIdAndType(TenantId tenantId, String type, TextPageLink pageLink);

    ListenableFuture<List<Mod>> findModsByTenantIdAndIdsAsync(TenantId tenantId, List<ModId> modIds);

    void deleteModsByTenantId(TenantId tenantId);

    TextPageData<Mod> findModsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink);

    TextPageData<Mod> findModsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, String type, TextPageLink pageLink);

    ListenableFuture<List<Mod>> findModsByTenantIdCustomerIdAndIdsAsync(TenantId tenantId, CustomerId customerId, List<ModId> modIds);

    void unassignCustomerMods(TenantId tenantId, CustomerId customerId);

    ListenableFuture<List<Mod>> findModsByQuery(ModSearchQuery query);

    ListenableFuture<List<EntitySubtype>> findModTypesByTenantId(TenantId tenantId);

}
