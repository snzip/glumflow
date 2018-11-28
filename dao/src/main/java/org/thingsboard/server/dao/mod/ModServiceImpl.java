package org.thingsboard.server.dao.mod;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.EntityView;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.mod.ModSearchQuery;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.ModId;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.thingsboard.server.common.data.CacheConstants.DEVICE_CACHE;
import static org.thingsboard.server.dao.DaoUtil.toUUIDs;
import static org.thingsboard.server.dao.model.ModelConstants.NULL_UUID;
import static org.thingsboard.server.dao.service.Validator.validateId;
import static org.thingsboard.server.dao.service.Validator.validateIds;
import static org.thingsboard.server.dao.service.Validator.validatePageLink;
import static org.thingsboard.server.dao.service.Validator.validateString;

@Service
@Slf4j
public class ModServiceImpl extends AbstractEntityService implements ModService {

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";
    public static final String INCORRECT_PAGE_LINK = "Incorrect page link ";
    public static final String INCORRECT_CUSTOMER_ID = "Incorrect customerId ";
    public static final String INCORRECT_DEVICE_ID = "Incorrect modId ";
    @Autowired
    private ModDao modDao;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private EntityViewService entityViewService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Mod findModById(ModId modId) {
        log.trace("Executing findModById [{}]", modId);
        validateId(modId, INCORRECT_DEVICE_ID + modId);
        return modDao.findById(modId.getId());
    }

    @Override
    public ListenableFuture<Mod> findModByIdAsync(ModId modId) {
        log.trace("Executing findModById [{}]", modId);
        validateId(modId, INCORRECT_DEVICE_ID + modId);
        return modDao.findByIdAsync(modId.getId());
    }

    @Cacheable(cacheNames = DEVICE_CACHE, key = "{#tenantId, #name}")
    @Override
    public Mod findModByTenantIdAndName(TenantId tenantId, String name) {
        log.trace("Executing findModByTenantIdAndName [{}][{}]", tenantId, name);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        Optional<Mod> modOpt = modDao.findModByTenantIdAndName(tenantId.getId(), name);
        return modOpt.orElse(null);
    }

    @CacheEvict(cacheNames = DEVICE_CACHE, key = "{#mod.tenantId, #mod.name}")
    @Override
    public Mod saveMod(Mod mod) {
        log.trace("Executing saveMod [{}]", mod);
        modValidator.validate(mod);
        Mod savedMod = modDao.save(mod);
        return savedMod;
    }

    @Override
    public Mod assignModToCustomer(ModId modId, CustomerId customerId) {
        Mod mod = findModById(modId);
        mod.setCustomerId(customerId);
        return saveMod(mod);
    }

    @Override
    public Mod unassignModFromCustomer(ModId modId) {
        Mod mod = findModById(modId);
        mod.setCustomerId(null);
        return saveMod(mod);
    }

    @Override
    public void deleteMod(ModId modId) {
        log.trace("Executing deleteMod [{}]", modId);
        validateId(modId, INCORRECT_DEVICE_ID + modId);

        Mod mod = modDao.findById(modId.getId());
        try {
            List<EntityView> entityViews = entityViewService.findEntityViewsByTenantIdAndEntityIdAsync(mod.getTenantId(), modId).get();
            if (entityViews != null && !entityViews.isEmpty()) {
                throw new DataValidationException("Can't delete mod that is assigned to entity views!");
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("Exception while finding entity views for modId [{}]", modId, e);
            throw new RuntimeException("Exception while finding entity views for modId [" + modId + "]", e);
        }

        deleteEntityRelations(modId);

        List<Object> list = new ArrayList<>();
        list.add(mod.getTenantId());
        list.add(mod.getName());
        Cache cache = cacheManager.getCache(DEVICE_CACHE);
        cache.evict(list);

        modDao.removeById(modId.getId());
    }

    @Override
    public TextPageData<Mod> findModsByTenantId(TenantId tenantId, TextPageLink pageLink) {
        log.trace("Executing findModsByTenantId, tenantId [{}], pageLink [{}]", tenantId, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Mod> mods = modDao.findModsByTenantId(tenantId.getId(), pageLink);
        return new TextPageData<>(mods, pageLink);
    }

    @Override
    public TextPageData<Mod> findModsByTenantIdAndType(TenantId tenantId, String type, TextPageLink pageLink) {
        log.trace("Executing findModsByTenantIdAndType, tenantId [{}], type [{}], pageLink [{}]", tenantId, type, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateString(type, "Incorrect type " + type);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Mod> mods = modDao.findModsByTenantIdAndType(tenantId.getId(), type, pageLink);
        return new TextPageData<>(mods, pageLink);
    }

    @Override
    public ListenableFuture<List<Mod>> findModsByTenantIdAndIdsAsync(TenantId tenantId, List<ModId> modIds) {
        log.trace("Executing findModsByTenantIdAndIdsAsync, tenantId [{}], modIds [{}]", tenantId, modIds);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateIds(modIds, "Incorrect modIds " + modIds);
        return modDao.findModsByTenantIdAndIdsAsync(tenantId.getId(), toUUIDs(modIds));
    }


    @Override
    public void deleteModsByTenantId(TenantId tenantId) {
        log.trace("Executing deleteModsByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        tenantModsRemover.removeEntities(tenantId);
    }

    @Override
    public TextPageData<Mod> findModsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink) {
        log.trace("Executing findModsByTenantIdAndCustomerId, tenantId [{}], customerId [{}], pageLink [{}]", tenantId, customerId, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Mod> mods = modDao.findModsByTenantIdAndCustomerId(tenantId.getId(), customerId.getId(), pageLink);
        return new TextPageData<>(mods, pageLink);
    }

    @Override
    public TextPageData<Mod> findModsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, String type, TextPageLink pageLink) {
        log.trace("Executing findModsByTenantIdAndCustomerIdAndType, tenantId [{}], customerId [{}], type [{}], pageLink [{}]", tenantId, customerId, type, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validateString(type, "Incorrect type " + type);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Mod> mods = modDao.findModsByTenantIdAndCustomerIdAndType(tenantId.getId(), customerId.getId(), type, pageLink);
        return new TextPageData<>(mods, pageLink);
    }

    @Override
    public ListenableFuture<List<Mod>> findModsByTenantIdCustomerIdAndIdsAsync(TenantId tenantId, CustomerId customerId, List<ModId> modIds) {
        log.trace("Executing findModsByTenantIdCustomerIdAndIdsAsync, tenantId [{}], customerId [{}], modIds [{}]", tenantId, customerId, modIds);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validateIds(modIds, "Incorrect modIds " + modIds);
        return modDao.findModsByTenantIdCustomerIdAndIdsAsync(tenantId.getId(),
                customerId.getId(), toUUIDs(modIds));
    }

    @Override
    public void unassignCustomerMods(TenantId tenantId, CustomerId customerId) {
        log.trace("Executing unassignCustomerMods, tenantId [{}], customerId [{}]", tenantId, customerId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        new CustomerModsUnassigner(tenantId).removeEntities(customerId);
    }

    @Override
    public ListenableFuture<List<Mod>> findModsByQuery(ModSearchQuery query) {
        ListenableFuture<List<EntityRelation>> relations = relationService.findByQuery(query.toEntitySearchQuery());
        ListenableFuture<List<Mod>> mods = Futures.transformAsync(relations, r -> {
            EntitySearchDirection direction = query.toEntitySearchQuery().getParameters().getDirection();
            List<ListenableFuture<Mod>> futures = new ArrayList<>();
            for (EntityRelation relation : r) {
                EntityId entityId = direction == EntitySearchDirection.FROM ? relation.getTo() : relation.getFrom();
                if (entityId.getEntityType() == EntityType.DEVICE) {
                    futures.add(findModByIdAsync(new ModId(entityId.getId())));
                }
            }
            return Futures.successfulAsList(futures);
        });

        mods = Futures.transform(mods, new Function<List<Mod>, List<Mod>>() {
            @Nullable
            @Override
            public List<Mod> apply(@Nullable List<Mod> modList) {
                return modList == null ? Collections.emptyList() : modList.stream().filter(mod -> query.getModTypes().contains(mod.getType())).collect(Collectors.toList());
            }
        });

        return mods;
    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findModTypesByTenantId(TenantId tenantId) {
        log.trace("Executing findModTypesByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        ListenableFuture<List<EntitySubtype>> tenantModTypes = modDao.findTenantModTypesAsync(tenantId.getId());
        return Futures.transform(tenantModTypes,
                (Function<List<EntitySubtype>, List<EntitySubtype>>) modTypes -> {
                    modTypes.sort(Comparator.comparing(EntitySubtype::getType));
                    return modTypes;
                });
    }

    private DataValidator<Mod> modValidator =
            new DataValidator<Mod>() {

                @Override
                protected void validateCreate(Mod mod) {
                    modDao.findModByTenantIdAndName(mod.getTenantId().getId(), mod.getName()).ifPresent(
                            d -> {
                                throw new DataValidationException("Mod with such name already exists!");
                            }
                    );
                }

                @Override
                protected void validateUpdate(Mod mod) {
                    modDao.findModByTenantIdAndName(mod.getTenantId().getId(), mod.getName()).ifPresent(
                            d -> {
                                if (!d.getUuidId().equals(mod.getUuidId())) {
                                    throw new DataValidationException("Mod with such name already exists!");
                                }
                            }
                    );
                }

                @Override
                protected void validateDataImpl(Mod mod) {
                    if (StringUtils.isEmpty(mod.getType())) {
                        throw new DataValidationException("Mod type should be specified!");
                    }
                    if (StringUtils.isEmpty(mod.getName())) {
                        throw new DataValidationException("Mod name should be specified!");
                    }
                    if (mod.getTenantId() == null) {
                        throw new DataValidationException("Mod should be assigned to tenant!");
                    } else {
                        Tenant tenant = tenantDao.findById(mod.getTenantId().getId());
                        if (tenant == null) {
                            throw new DataValidationException("Mod is referencing to non-existent tenant!");
                        }
                    }
                    if (mod.getCustomerId() == null) {
                        mod.setCustomerId(new CustomerId(NULL_UUID));
                    } else if (!mod.getCustomerId().getId().equals(NULL_UUID)) {
                        Customer customer = customerDao.findById(mod.getCustomerId().getId());
                        if (customer == null) {
                            throw new DataValidationException("Can't assign mod to non-existent customer!");
                        }
                        if (!customer.getTenantId().getId().equals(mod.getTenantId().getId())) {
                            throw new DataValidationException("Can't assign mod to customer from different tenant!");
                        }
                    }
                }
            };

    private PaginatedRemover<TenantId, Mod> tenantModsRemover =
            new PaginatedRemover<TenantId, Mod>() {

                @Override
                protected List<Mod> findEntities(TenantId id, TextPageLink pageLink) {
                    return modDao.findModsByTenantId(id.getId(), pageLink);
                }

                @Override
                protected void removeEntity(Mod entity) {
                    deleteMod(new ModId(entity.getUuidId()));
                }
            };

    private class CustomerModsUnassigner extends PaginatedRemover<CustomerId, Mod> {

        private TenantId tenantId;

        CustomerModsUnassigner(TenantId tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        protected List<Mod> findEntities(CustomerId id, TextPageLink pageLink) {
            return modDao.findModsByTenantIdAndCustomerId(tenantId.getId(), id.getId(), pageLink);
        }

        @Override
        protected void removeEntity(Mod entity) {
            unassignModFromCustomer(new ModId(entity.getUuidId()));
        }

    }
}
