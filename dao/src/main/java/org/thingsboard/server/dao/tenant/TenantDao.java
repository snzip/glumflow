package org.thingsboard.server.dao.tenant;

import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.Dao;

import java.util.List;

public interface TenantDao extends Dao<Tenant> {

    /**
     * Save or update tenant object
     *
     * @param tenant the tenant object
     * @return saved tenant object
     */
    Tenant save(Tenant tenant);
    
    /**
     * Find tenants by region and page link.
     * 
     * @param region the region
     * @param pageLink the page link
     * @return the list of tenant objects
     */
    List<Tenant> findTenantsByRegion(String region, TextPageLink pageLink);
    
}
