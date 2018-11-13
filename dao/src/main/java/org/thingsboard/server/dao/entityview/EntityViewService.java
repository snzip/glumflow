package org.thingsboard.server.dao.entityview;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.EntityView;
import org.thingsboard.server.common.data.entityview.EntityViewSearchQuery;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityViewId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.util.List;

/**
 * Created by Victor Basanets on 8/27/2017.
 */
public interface EntityViewService {

    EntityView saveEntityView(EntityView entityView);

    EntityView assignEntityViewToCustomer(EntityViewId entityViewId, CustomerId customerId);

    EntityView unassignEntityViewFromCustomer(EntityViewId entityViewId);

    void unassignCustomerEntityViews(TenantId tenantId, CustomerId customerId);

    EntityView findEntityViewById(EntityViewId entityViewId);

    EntityView findEntityViewByTenantIdAndName(TenantId tenantId, String name);

    TextPageData<EntityView> findEntityViewByTenantId(TenantId tenantId, TextPageLink pageLink);

    TextPageData<EntityView> findEntityViewByTenantIdAndType(TenantId tenantId, TextPageLink pageLink, String type);

    TextPageData<EntityView> findEntityViewsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink);

    TextPageData<EntityView> findEntityViewsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, TextPageLink pageLink, String type);

    ListenableFuture<List<EntityView>> findEntityViewsByQuery(EntityViewSearchQuery query);

    ListenableFuture<EntityView> findEntityViewByIdAsync(EntityViewId entityViewId);

    ListenableFuture<List<EntityView>> findEntityViewsByTenantIdAndEntityIdAsync(TenantId tenantId, EntityId entityId);

    void deleteEntityView(EntityViewId entityViewId);

    void deleteEntityViewsByTenantId(TenantId tenantId);

    ListenableFuture<List<EntitySubtype>> findEntityViewTypesByTenantId(TenantId tenantId);
}
