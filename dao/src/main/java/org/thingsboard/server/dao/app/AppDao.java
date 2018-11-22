package org.thingsboard.server.dao.app;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.App;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.Dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface AppDao.
 *
 */
public interface AppDao extends Dao<App> {

    /**
     * Save or update App object
     *
     * @param App the App object
     * @return saved App object
     */
    App save(App App);

    /**
     * Find Apps by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of App objects
     */
    List<App> findAppsByTenantId(UUID tenantId, TextPageLink pageLink);

    /**
     * Find Apps by tenantId, type and page link.
     *
     * @param tenantId the tenantId
     * @param type the type
     * @param pageLink the page link
     * @return the list of App objects
     */
    List<App> findAppsByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink);

    /**
     * Find Apps by tenantId and Apps Ids.
     *
     * @param tenantId the tenantId
     * @param AppIds the App Ids
     * @return the list of App objects
     */
    ListenableFuture<List<App>> findAppsByTenantIdAndIdsAsync(UUID tenantId, List<UUID> AppIds);

    /**
     * Find Apps by tenantId, customerId and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param pageLink the page link
     * @return the list of App objects
     */
    List<App> findAppsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink);

    /**
     * Find Apps by tenantId, customerId, type and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param type the type
     * @param pageLink the page link
     * @return the list of App objects
     */
    List<App> findAppsByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink);


    /**
     * Find Apps by tenantId, customerId and Apps Ids.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param AppIds the App Ids
     * @return the list of App objects
     */
//    ListenableFuture<List<App>> findAppsByTenantIdCustomerIdAndIdsAsync(UUID tenantId, UUID customerId, List<UUID> AppIds);

    /**
     * Find Apps by tenantId and App name.
     *
     * @param tenantId the tenantId
     * @param name the App name
     * @return the optional App object
     */
    Optional<App> findAppByTenantIdAndName(UUID tenantId, String name);

    /**
     * Find tenants App types.
     *
     * @return the list of tenant App type objects
     */
    ListenableFuture<List<EntitySubtype>> findTenantAppTypesAsync(UUID tenantId);
}
