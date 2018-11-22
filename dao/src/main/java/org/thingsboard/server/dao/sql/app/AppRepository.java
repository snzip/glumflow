package org.thingsboard.server.dao.sql.app;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thingsboard.server.dao.model.sql.AppEntity;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
@SqlDao
public interface AppRepository extends CrudRepository<AppEntity, String> {

//
//    @Query("SELECT d FROM AppEntity d WHERE d.tenantId = :tenantId " +
////            "AND d.customerId = :customerId " +
//            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:searchText, '%')) " +
//            "AND d.id > :idOffset ORDER BY d.id")
//    List<AppEntity> findByTenantIdAndCustomerId(@Param("tenantId") String tenantId,
////                                                   @Param("customerId") String customerId,
//                                                   @Param("searchText") String searchText,
//                                                   @Param("idOffset") String idOffset,
//                                                   Pageable pageable);

    @Query("SELECT d FROM AppEntity d WHERE d.tenantId = :tenantId " +
            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND d.id > :idOffset ORDER BY d.id")
    List<AppEntity> findByTenantId(@Param("tenantId") String tenantId,
                                      @Param("textSearch") String textSearch,
                                      @Param("idOffset") String idOffset,
                                      Pageable pageable);

    @Query("SELECT d FROM AppEntity d WHERE d.tenantId = :tenantId " +
            "AND d.type = :type " +
            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND d.id > :idOffset ORDER BY d.id")
    List<AppEntity> findByTenantIdAndType(@Param("tenantId") String tenantId,
                                             @Param("type") String type,
                                             @Param("textSearch") String textSearch,
                                             @Param("idOffset") String idOffset,
                                             Pageable pageable);
//
//    @Query("SELECT d FROM AppEntity d WHERE d.tenantId = :tenantId " +
////            "AND d.customerId = :customerId " +
//            "AND d.type = :type " +
//            "AND LOWER(d.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
//            "AND d.id > :idOffset ORDER BY d.id")
//    List<AppEntity> findByTenantIdAndCustomerIdAndType(@Param("tenantId") String tenantId,
//                                                          @Param("customerId") String customerId,
//                                                          @Param("type") String type,
//                                                          @Param("textSearch") String textSearch,
//                                                          @Param("idOffset") String idOffset,
//                                                          Pageable pageable);

    @Query("SELECT DISTINCT d.type FROM AppEntity d WHERE d.tenantId = :tenantId")
    List<String> findTenantAppTypes(@Param("tenantId") String tenantId);

    AppEntity findByTenantIdAndName(String tenantId, String name);

//    List<AppEntity> findAppsByTenantIdAndCustomerIdAndIdIn(String tenantId, String customerId, List<String> appIds);

    List<AppEntity> findAppsByTenantIdAndIdIn(String tenantId, List<String> appIds);
}
