package org.thingsboard.server.dao.customer;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.util.Optional;

public interface CustomerService {

    Customer findCustomerById(CustomerId customerId);

    Optional<Customer> findCustomerByTenantIdAndTitle(TenantId tenantId, String title);

    ListenableFuture<Customer> findCustomerByIdAsync(CustomerId customerId);

    Customer saveCustomer(Customer customer);

    void deleteCustomer(CustomerId customerId);

    Customer findOrCreatePublicCustomer(TenantId tenantId);

    TextPageData<Customer> findCustomersByTenantId(TenantId tenantId, TextPageLink pageLink);

    void deleteCustomersByTenantId(TenantId tenantId);

}
