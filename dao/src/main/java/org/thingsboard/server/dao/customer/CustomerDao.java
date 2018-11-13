package org.thingsboard.server.dao.customer;

import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.Dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface CustomerDao.
 */
public interface CustomerDao extends Dao<Customer> {

    /**
     * Save or update customer object
     *
     * @param customer the customer object
     * @return saved customer object
     */
    Customer save(Customer customer);
    
    /**
     * Find customers by tenant id and page link.
     *
     * @param tenantId the tenant id
     * @param pageLink the page link
     * @return the list of customer objects
     */
    List<Customer> findCustomersByTenantId(UUID tenantId, TextPageLink pageLink);

    /**
     * Find customers by tenantId and customer title.
     *
     * @param tenantId the tenantId
     * @param title the customer title
     * @return the optional customer object
     */
    Optional<Customer> findCustomersByTenantIdAndTitle(UUID tenantId, String title);
    
}
