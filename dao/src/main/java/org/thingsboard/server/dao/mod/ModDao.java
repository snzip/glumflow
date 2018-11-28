package org.thingsboard.server.dao.mod;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.Dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface ModDao.
 *
 */
public interface ModDao extends Dao<Mod> {

    /**
     * Save or update mod object
     *
     * @param mod the mod object
     * @return saved mod object
     */
    Mod save(Mod mod);

    /**
     * Find mods by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of mod objects
     */
    List<Mod> findModsByTenantId(UUID tenantId, TextPageLink pageLink);

    /**
     * Find mods by tenantId, type and page link.
     *
     * @param tenantId the tenantId
     * @param type the type
     * @param pageLink the page link
     * @return the list of mod objects
     */
    List<Mod> findModsByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink);

    /**
     * Find mods by tenantId and mods Ids.
     *
     * @param tenantId the tenantId
     * @param modIds the mod Ids
     * @return the list of mod objects
     */
    ListenableFuture<List<Mod>> findModsByTenantIdAndIdsAsync(UUID tenantId, List<UUID> modIds);

    /**
     * Find mods by tenantId, customerId and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param pageLink the page link
     * @return the list of mod objects
     */
    List<Mod> findModsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink);

    /**
     * Find mods by tenantId, customerId, type and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param type the type
     * @param pageLink the page link
     * @return the list of mod objects
     */
    List<Mod> findModsByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink);


    /**
     * Find mods by tenantId, customerId and mods Ids.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param modIds the mod Ids
     * @return the list of mod objects
     */
    ListenableFuture<List<Mod>> findModsByTenantIdCustomerIdAndIdsAsync(UUID tenantId, UUID customerId, List<UUID> modIds);

    /**
     * Find mods by tenantId and mod name.
     *
     * @param tenantId the tenantId
     * @param name the mod name
     * @return the optional mod object
     */
    Optional<Mod> findModByTenantIdAndName(UUID tenantId, String name);

    /**
     * Find tenants mod types.
     *
     * @return the list of tenant mod type objects
     */
    ListenableFuture<List<EntitySubtype>> findTenantModTypesAsync(UUID tenantId);
}
