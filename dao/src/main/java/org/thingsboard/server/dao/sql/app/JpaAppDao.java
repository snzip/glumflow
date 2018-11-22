package org.thingsboard.server.dao.sql.app;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.App;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.UUIDConverter;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.app.AppDao;
import org.thingsboard.server.dao.model.sql.AppEntity;
import org.thingsboard.server.dao.sql.JpaAbstractSearchTextDao;
import org.thingsboard.server.dao.sql.app.AppRepository;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.*;

import static org.thingsboard.server.common.data.UUIDConverter.fromTimeUUID;
import static org.thingsboard.server.common.data.UUIDConverter.fromTimeUUIDs;
import static org.thingsboard.server.dao.model.ModelConstants.NULL_UUID_STR;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
@Component
@SqlDao
public class JpaAppDao extends JpaAbstractSearchTextDao<AppEntity, App> implements AppDao {

    @Autowired
    private AppRepository appRepository;

    @Override
    protected Class<AppEntity> getEntityClass() {
        return AppEntity.class;
    }

    @Override
    protected CrudRepository<AppEntity, String> getCrudRepository() {
        return appRepository;
    }

    @Override
    public List<App> findAppsByTenantId(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                appRepository.findByTenantId(
                        fromTimeUUID(tenantId),
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                        new PageRequest(0, pageLink.getLimit())));
    }

    @Override
    public ListenableFuture<List<App>> findAppsByTenantIdAndIdsAsync(UUID tenantId, List<UUID> appIds) {
        return service.submit(() -> DaoUtil.convertDataList(appRepository.findAppsByTenantIdAndIdIn(UUIDConverter.fromTimeUUID(tenantId), fromTimeUUIDs(appIds))));
    }

    @Override
    public List<App> findAppsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink) {
        return null;
//        DaoUtil.convertDataList(
//                appRepository.findByTenantIdAndCustomerId(
//                        fromTimeUUID(tenantId),
//                        fromTimeUUID(customerId),
//                        Objects.toString(pageLink.getTextSearch(), ""),
//                        pageLink.getIdOffset() == null ? NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
//                        new PageRequest(0, pageLink.getLimit())));
    }

//    @Override
//    public ListenableFuture<List<App>> findAppsByTenantIdCustomerIdAndIdsAsync(UUID tenantId, UUID customerId, List<UUID> appIds) {
//        return service.submit(() -> DaoUtil.convertDataList(
//                appRepository.findAppsByTenantIdAndCustomerIdAndIdIn(fromTimeUUID(tenantId), fromTimeUUID(customerId), fromTimeUUIDs(appIds))));
//    }

    @Override
    public Optional<App> findAppByTenantIdAndName(UUID tenantId, String name) {
        App app = DaoUtil.getData(appRepository.findByTenantIdAndName(fromTimeUUID(tenantId), name));
        return Optional.ofNullable(app);
    }

    @Override
    public List<App> findAppsByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                appRepository.findByTenantIdAndType(
                        fromTimeUUID(tenantId),
                        type,
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                        new PageRequest(0, pageLink.getLimit())));
    }

    @Override
    public List<App> findAppsByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink) {
        return null;
//        DaoUtil.convertDataList(
//                appRepository.findByTenantIdAndCustomerIdAndType(
//                        fromTimeUUID(tenantId),
//                        fromTimeUUID(customerId),
//                        type,
//                        Objects.toString(pageLink.getTextSearch(), ""),
//                        pageLink.getIdOffset() == null ? NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
//                        new PageRequest(0, pageLink.getLimit())));
    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findTenantAppTypesAsync(UUID tenantId) {
        return service.submit(() -> convertTenantAppTypesToDto(tenantId, appRepository.findTenantAppTypes(fromTimeUUID(tenantId))));
    }

    private List<EntitySubtype> convertTenantAppTypesToDto(UUID tenantId, List<String> types) {
        List<EntitySubtype> list = Collections.emptyList();
        if (types != null && !types.isEmpty()) {
            list = new ArrayList<>();
            for (String type : types) {
                list.add(new EntitySubtype(new TenantId(tenantId), EntityType.DEVICE, type));
            }
        }
        return list;
    }
}
