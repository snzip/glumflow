package org.thingsboard.server.dao.sql.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.UUIDConverter;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.customer.CustomerDao;
import org.thingsboard.server.dao.model.sql.CustomerEntity;
import org.thingsboard.server.dao.sql.JpaAbstractSearchTextDao;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.NULL_UUID_STR;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
@Component
@SqlDao
public class JpaCustomerDao extends JpaAbstractSearchTextDao<CustomerEntity, Customer> implements CustomerDao {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected Class<CustomerEntity> getEntityClass() {
        return CustomerEntity.class;
    }

    @Override
    protected CrudRepository<CustomerEntity, String> getCrudRepository() {
        return customerRepository;
    }

    @Override
    public List<Customer> findCustomersByTenantId(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(customerRepository.findByTenantId(
                UUIDConverter.fromTimeUUID(tenantId),
                Objects.toString(pageLink.getTextSearch(), ""),
                pageLink.getIdOffset() == null ? NULL_UUID_STR : UUIDConverter.fromTimeUUID(pageLink.getIdOffset()),
                new PageRequest(0, pageLink.getLimit())));
    }

    @Override
    public Optional<Customer> findCustomersByTenantIdAndTitle(UUID tenantId, String title) {
        Customer customer = DaoUtil.getData(customerRepository.findByTenantIdAndTitle(UUIDConverter.fromTimeUUID(tenantId), title));
        return Optional.ofNullable(customer);
    }
}
