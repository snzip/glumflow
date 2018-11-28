package org.thingsboard.server.dao.sql.mod;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.dao.model.sql.ModEntity;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
@SqlDao
public interface ModRepository extends CrudRepository<ModEntity, String> {


    @Query("SELECT d FROM ModEntity d WHERE d.tenantId = :tenantId " +
            "AND d.customerId = :customerId " +
            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:searchText, '%')) " +
            "AND d.id > :idOffset ORDER BY d.id")
    List<ModEntity> findByTenantIdAndCustomerId(@Param("tenantId") String tenantId,
                                                   @Param("customerId") String customerId,
                                                   @Param("searchText") String searchText,
                                                   @Param("idOffset") String idOffset,
                                                   Pageable pageable);

    @Query("SELECT d FROM ModEntity d WHERE d.tenantId = :tenantId " +
            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND d.id > :idOffset ORDER BY d.id")
    List<ModEntity> findByTenantId(@Param("tenantId") String tenantId,
                                      @Param("textSearch") String textSearch,
                                      @Param("idOffset") String idOffset,
                                      Pageable pageable);

    @Query("SELECT d FROM ModEntity d WHERE d.tenantId = :tenantId " +
            "AND d.type = :type " +
            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND d.id > :idOffset ORDER BY d.id")
    List<ModEntity> findByTenantIdAndType(@Param("tenantId") String tenantId,
                                             @Param("type") String type,
                                             @Param("textSearch") String textSearch,
                                             @Param("idOffset") String idOffset,
                                             Pageable pageable);

    @Query("SELECT d FROM ModEntity d WHERE d.tenantId = :tenantId " +
            "AND d.customerId = :customerId " +
            "AND d.type = :type " +
            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND d.id > :idOffset ORDER BY d.id")
    List<ModEntity> findByTenantIdAndCustomerIdAndType(@Param("tenantId") String tenantId,
                                                          @Param("customerId") String customerId,
                                                          @Param("type") String type,
                                                          @Param("textSearch") String textSearch,
                                                          @Param("idOffset") String idOffset,
                                                          Pageable pageable);

    @Query("SELECT DISTINCT d.type FROM ModEntity d WHERE d.tenantId = :tenantId")
    List<String> findTenantModTypes(@Param("tenantId") String tenantId);

    ModEntity findByTenantIdAndName(String tenantId, String name);

    List<ModEntity> findModsByTenantIdAndCustomerIdAndIdIn(String tenantId, String customerId, List<String> modIds);

    List<ModEntity> findModsByTenantIdAndIdIn(String tenantId, List<String> modIds);
}
