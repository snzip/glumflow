package org.thingsboard.server.dao.app;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thingsboard.server.common.data.*;
import org.thingsboard.server.common.data.id.AppId;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.relation.EntityRelation;
import org.thingsboard.server.common.data.relation.EntitySearchDirection;
import org.thingsboard.server.dao.customer.CustomerDao;
import org.thingsboard.server.dao.entity.AbstractEntityService;
import org.thingsboard.server.dao.entityview.EntityViewService;
import org.thingsboard.server.dao.exception.DataValidationException;
import org.thingsboard.server.dao.service.DataValidator;
import org.thingsboard.server.dao.service.PaginatedRemover;
import org.thingsboard.server.dao.tenant.TenantDao;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.thingsboard.server.common.data.CacheConstants.DEVICE_CACHE;
import static org.thingsboard.server.dao.DaoUtil.toUUIDs;
import static org.thingsboard.server.dao.service.Validator.*;

//import org.thingsboard.server.common.data.app.AppSearchQuery;

@Service
@Slf4j
public class AppServiceImpl extends AbstractEntityService implements AppService {

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";
    public static final String INCORRECT_PAGE_LINK = "Incorrect page link ";
    public static final String INCORRECT_CUSTOMER_ID = "Incorrect customerId ";
    public static final String INCORRECT_DEVICE_ID = "Incorrect appId ";
    @Autowired
    private AppDao appDao;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private EntityViewService entityViewService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public App findAppById(AppId appId) {
        log.trace("Executing findAppById [{}]", appId);
        validateId(appId, INCORRECT_DEVICE_ID + appId);
        return appDao.findById(appId.getId());
    }

    @Override
    public ListenableFuture<App> findAppByIdAsync(AppId appId) {
        log.trace("Executing findAppById [{}]", appId);
        validateId(appId, INCORRECT_DEVICE_ID + appId);
        return appDao.findByIdAsync(appId.getId());
    }

    @Cacheable(cacheNames = DEVICE_CACHE, key = "{#tenantId, #name}")
    @Override
    public App findAppByTenantIdAndName(TenantId tenantId, String name) {
        log.trace("Executing findAppByTenantIdAndName [{}][{}]", tenantId, name);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        Optional<App> appOpt = appDao.findAppByTenantIdAndName(tenantId.getId(), name);
        return appOpt.orElse(null);
    }

    @CacheEvict(cacheNames = DEVICE_CACHE, key = "{#app.tenantId, #app.name}")
    @Override
    public App saveApp(App app) {
        log.trace("Executing saveApp [{}]", app);
        appValidator.validate(app);
        App savedApp = appDao.save(app);
//        if (app.getId() == null) {
//            AppCredentials appCredentials = new AppCredentials();
//            appCredentials.setAppId(new AppId(savedApp.getUuidId()));
//            appCredentials.setCredentialsType(AppCredentialsType.ACCESS_TOKEN);
//            appCredentials.setCredentialsId(RandomStringUtils.randomAlphanumeric(20));
//            appCredentialsService.createAppCredentials(appCredentials);
//        }
        return savedApp;
    }

    @Override
    public App assignAppToCustomer(AppId appId, CustomerId customerId) {
        App app = findAppById(appId);
//        app.setCustomerId(customerId);
        return saveApp(app);
    }

    @Override
    public App unassignAppFromCustomer(AppId appId) {
        App app = findAppById(appId);
//        app.setCustomerId(null);
        return saveApp(app);
    }

    @Override
    public void deleteApp(AppId appId) {
        log.trace("Executing deleteApp [{}]", appId);
        validateId(appId, INCORRECT_DEVICE_ID + appId);

        App app = appDao.findById(appId.getId());
        try {
            List<EntityView> entityViews = entityViewService.findEntityViewsByTenantIdAndEntityIdAsync(app.getTenantId(), appId).get();
            if (entityViews != null && !entityViews.isEmpty()) {
                throw new DataValidationException("Can't delete app that is assigned to entity views!");
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("Exception while finding entity views for appId [{}]", appId, e);
            throw new RuntimeException("Exception while finding entity views for appId [" + appId + "]", e);
        }

//        AppCredentials appCredentials = appCredentialsService.findAppCredentialsByAppId(appId);
//        if (appCredentials != null) {
//            appCredentialsService.deleteAppCredentials(appCredentials);
//        }
        deleteEntityRelations(appId);

        List<Object> list = new ArrayList<>();
        list.add(app.getTenantId());
        list.add(app.getName());
        Cache cache = cacheManager.getCache(DEVICE_CACHE);
        cache.evict(list);

        appDao.removeById(appId.getId());
    }

    @Override
    public TextPageData<App> findAppsByTenantId(TenantId tenantId, TextPageLink pageLink) {
        log.trace("Executing findAppsByTenantId, tenantId [{}], pageLink [{}]", tenantId, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<App> apps = appDao.findAppsByTenantId(tenantId.getId(), pageLink);
        return new TextPageData<>(apps, pageLink);
    }

    @Override
    public TextPageData<App> findAppsByTenantIdAndType(TenantId tenantId, String type, TextPageLink pageLink) {
        log.trace("Executing findAppsByTenantIdAndType, tenantId [{}], type [{}], pageLink [{}]", tenantId, type, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateString(type, "Incorrect type " + type);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<App> apps = appDao.findAppsByTenantIdAndType(tenantId.getId(), type, pageLink);
        return new TextPageData<>(apps, pageLink);
    }

    @Override
    public ListenableFuture<List<App>> findAppsByTenantIdAndIdsAsync(TenantId tenantId, List<AppId> appIds) {
        log.trace("Executing findAppsByTenantIdAndIdsAsync, tenantId [{}], appIds [{}]", tenantId, appIds);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateIds(appIds, "Incorrect appIds " + appIds);
        return appDao.findAppsByTenantIdAndIdsAsync(tenantId.getId(), toUUIDs(appIds));
    }


    @Override
    public void deleteAppsByTenantId(TenantId tenantId) {
        log.trace("Executing deleteAppsByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        tenantAppsRemover.removeEntities(tenantId);
    }

    @Override
    public TextPageData<App> findAppsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink) {
        log.trace("Executing findAppsByTenantIdAndCustomerId, tenantId [{}], customerId [{}], pageLink [{}]", tenantId, customerId, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<App> apps = appDao.findAppsByTenantIdAndCustomerId(tenantId.getId(), customerId.getId(), pageLink);
        return new TextPageData<>(apps, pageLink);
    }

    @Override
    public TextPageData<App> findAppsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, String type, TextPageLink pageLink) {
        log.trace("Executing findAppsByTenantIdAndCustomerIdAndType, tenantId [{}], customerId [{}], type [{}], pageLink [{}]", tenantId, customerId, type, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validateString(type, "Incorrect type " + type);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<App> apps = appDao.findAppsByTenantIdAndCustomerIdAndType(tenantId.getId(), customerId.getId(), type, pageLink);
        return new TextPageData<>(apps, pageLink);
    }

//    @Override
//    public ListenableFuture<List<App>> findAppsByTenantIdCustomerIdAndIdsAsync(TenantId tenantId, CustomerId customerId, List<AppId> appIds) {
//        log.trace("Executing findAppsByTenantIdCustomerIdAndIdsAsync, tenantId [{}], customerId [{}], appIds [{}]", tenantId, customerId, appIds);
//        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
//        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
//        validateIds(appIds, "Incorrect appIds " + appIds);
//        return appDao.findAppsByTenantIdCustomerIdAndIdsAsync(tenantId.getId(),
//                customerId.getId(), toUUIDs(appIds));
//    }

    @Override
    public void unassignCustomerApps(TenantId tenantId, CustomerId customerId) {
        log.trace("Executing unassignCustomerApps, tenantId [{}], customerId [{}]", tenantId, customerId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        new CustomerAppsUnassigner(tenantId).removeEntities(customerId);
    }

//    @Override
//    public ListenableFuture<List<App>> findAppsByQuery(AppSearchQuery query) {
//        ListenableFuture<List<EntityRelation>> relations = relationService.findByQuery(query.toEntitySearchQuery());
//        ListenableFuture<List<App>> apps = Futures.transformAsync(relations, r -> {
//            EntitySearchDirection direction = query.toEntitySearchQuery().getParameters().getDirection();
//            List<ListenableFuture<App>> futures = new ArrayList<>();
//            for (EntityRelation relation : r) {
//                EntityId entityId = direction == EntitySearchDirection.FROM ? relation.getTo() : relation.getFrom();
//                if (entityId.getEntityType() == EntityType.DEVICE) {
//                    futures.add(findAppByIdAsync(new AppId(entityId.getId())));
//                }
//            }
//            return Futures.successfulAsList(futures);
//        });
//
//        apps = Futures.transform(apps, new Function<List<App>, List<App>>() {
//            @Nullable
//            @Override
//            public List<App> apply(@Nullable List<App> appList) {
//                return appList == null ? Collections.emptyList() : appList.stream().filter(app -> query.getAppTypes().contains(app.getType())).collect(Collectors.toList());
//            }
//        });
//
//        return apps;
//    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findAppTypesByTenantId(TenantId tenantId) {
        log.trace("Executing findAppTypesByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        ListenableFuture<List<EntitySubtype>> tenantAppTypes = appDao.findTenantAppTypesAsync(tenantId.getId());
        return Futures.transform(tenantAppTypes,
                (Function<List<EntitySubtype>, List<EntitySubtype>>) appTypes -> {
                    appTypes.sort(Comparator.comparing(EntitySubtype::getType));
                    return appTypes;
                });
    }

    private DataValidator<App> appValidator =
            new DataValidator<App>() {

                @Override
                protected void validateCreate(App app) {
                    appDao.findAppByTenantIdAndName(app.getTenantId().getId(), app.getName()).ifPresent(
                            d -> {
                                throw new DataValidationException("App with such name already exists!");
                            }
                    );
                }

                @Override
                protected void validateUpdate(App app) {
                    appDao.findAppByTenantIdAndName(app.getTenantId().getId(), app.getName()).ifPresent(
                            d -> {
                                if (!d.getUuidId().equals(app.getUuidId())) {
                                    throw new DataValidationException("App with such name already exists!");
                                }
                            }
                    );
                }

                @Override
                protected void validateDataImpl(App app) {
                    if (StringUtils.isEmpty(app.getType())) {
                        throw new DataValidationException("App type should be specified!");
                    }
                    if (StringUtils.isEmpty(app.getName())) {
                        throw new DataValidationException("App name should be specified!");
                    }
                    if (app.getTenantId() == null) {
                        throw new DataValidationException("App should be assigned to tenant!");
                    } else {
                        Tenant tenant = tenantDao.findById(app.getTenantId().getId());
                        if (tenant == null) {
                            throw new DataValidationException("App is referencing to non-existent tenant!");
                        }
                    }
//                    if (app.getCustomerId() == null) {
//                        app.setCustomerId(new CustomerId(NULL_UUID));
//                    } else if (!app.getCustomerId().getId().equals(NULL_UUID)) {
//                        Customer customer = customerDao.findById(app.getCustomerId().getId());
//                        if (customer == null) {
//                            throw new DataValidationException("Can't assign app to non-existent customer!");
//                        }
//                        if (!customer.getTenantId().getId().equals(app.getTenantId().getId())) {
//                            throw new DataValidationException("Can't assign app to customer from different tenant!");
//                        }
//                    }
                }
            };

    private PaginatedRemover<TenantId, App> tenantAppsRemover =
            new PaginatedRemover<TenantId, App>() {

                @Override
                protected List<App> findEntities(TenantId id, TextPageLink pageLink) {
                    return appDao.findAppsByTenantId(id.getId(), pageLink);
                }

                @Override
                protected void removeEntity(App entity) {
                    deleteApp(new AppId(entity.getUuidId()));
                }
            };

    private class CustomerAppsUnassigner extends PaginatedRemover<CustomerId, App> {

        private TenantId tenantId;

        CustomerAppsUnassigner(TenantId tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        protected List<App> findEntities(CustomerId id, TextPageLink pageLink) {
            return appDao.findAppsByTenantIdAndCustomerId(tenantId.getId(), id.getId(), pageLink);
        }

        @Override
        protected void removeEntity(App entity) {
            unassignAppFromCustomer(new AppId(entity.getUuidId()));
        }

    }
}
