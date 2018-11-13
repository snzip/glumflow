package org.thingsboard.server.dao.sql.customer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.dao.model.sql.CustomerEntity;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
@SqlDao
public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {

    @Query("SELECT c FROM CustomerEntity c WHERE c.tenantId = :tenantId " +
            "AND LOWER(c.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND c.id > :idOffset ORDER BY c.id")
    List<CustomerEntity> findByTenantId(@Param("tenantId") String tenantId,
                                        @Param("textSearch") String textSearch,
                                        @Param("idOffset") String idOffset,
                                        Pageable pageable);

    CustomerEntity findByTenantIdAndTitle(String tenantId, String title);

}
