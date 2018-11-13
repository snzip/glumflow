package org.thingsboard.server.dao.sql.asset;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.dao.model.sql.AssetEntity;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;

/**
 * Created by Valerii Sosliuk on 5/21/2017.
 */
@SqlDao
public interface AssetRepository extends CrudRepository<AssetEntity, String> {

    @Query("SELECT a FROM AssetEntity a WHERE a.tenantId = :tenantId " +
            "AND LOWER(a.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND a.id > :idOffset ORDER BY a.id")
    List<AssetEntity> findByTenantId(@Param("tenantId") String tenantId,
                                     @Param("textSearch") String textSearch,
                                     @Param("idOffset") String idOffset,
                                     Pageable pageable);

    @Query("SELECT a FROM AssetEntity a WHERE a.tenantId = :tenantId " +
            "AND a.customerId = :customerId " +
            "AND LOWER(a.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND a.id > :idOffset ORDER BY a.id")
    List<AssetEntity> findByTenantIdAndCustomerId(@Param("tenantId") String tenantId,
                                                  @Param("customerId") String customerId,
                                                  @Param("textSearch") String textSearch,
                                                  @Param("idOffset") String idOffset,
                                                  Pageable pageable);

    List<AssetEntity> findByTenantIdAndIdIn(String tenantId, List<String> assetIds);

    List<AssetEntity> findByTenantIdAndCustomerIdAndIdIn(String tenantId, String customerId, List<String> assetIds);

    AssetEntity findByTenantIdAndName(String tenantId, String name);

    @Query("SELECT a FROM AssetEntity a WHERE a.tenantId = :tenantId " +
            "AND a.type = :type " +
            "AND LOWER(a.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND a.id > :idOffset ORDER BY a.id")
    List<AssetEntity> findByTenantIdAndType(@Param("tenantId") String tenantId,
                                            @Param("type") String type,
                                            @Param("textSearch") String textSearch,
                                            @Param("idOffset") String idOffset,
                                            Pageable pageable);

    @Query("SELECT a FROM AssetEntity a WHERE a.tenantId = :tenantId " +
            "AND a.customerId = :customerId AND a.type = :type " +
            "AND LOWER(a.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND a.id > :idOffset ORDER BY a.id")
    List<AssetEntity> findByTenantIdAndCustomerIdAndType(@Param("tenantId") String tenantId,
                                                         @Param("customerId") String customerId,
                                                         @Param("type") String type,
                                                         @Param("textSearch") String textSearch,
                                                         @Param("idOffset") String idOffset,
                                                         Pageable pageable);

    @Query("SELECT DISTINCT a.type FROM AssetEntity a WHERE a.tenantId = :tenantId")
    List<String> findTenantAssetTypes(@Param("tenantId") String tenantId);

}
