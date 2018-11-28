package org.thingsboard.server.controller;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.audit.ActionType;
import org.thingsboard.server.common.data.mod.ModSearchQuery;
import org.thingsboard.server.common.data.exception.ThingsboardErrorCode;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.security.Authority;
import org.thingsboard.server.dao.exception.IncorrectParameterException;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.service.security.model.SecurityUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ModController extends BaseController {

    public static final String DEVICE_ID = "modId";

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/mod/{modId}", method = RequestMethod.GET)
    @ResponseBody
    public Mod getModById(@PathVariable(DEVICE_ID) String strModId) throws ThingsboardException {
        checkParameter(DEVICE_ID, strModId);
        try {
            ModId modId = new ModId(toUUID(strModId));
            return checkModId(modId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/mod", method = RequestMethod.POST)
    @ResponseBody
    public Mod saveMod(@RequestBody Mod mod) throws ThingsboardException {
        try {
            mod.setTenantId(getCurrentUser().getTenantId());
            if (getCurrentUser().getAuthority() == Authority.CUSTOMER_USER) {
                if (mod.getId() == null || mod.getId().isNullUid() ||
                        mod.getCustomerId() == null || mod.getCustomerId().isNullUid()) {
                    throw new ThingsboardException("You don't have permission to perform this operation!",
                            ThingsboardErrorCode.PERMISSION_DENIED);
                } else {
                    checkCustomerId(mod.getCustomerId());
                }
            }
            Mod savedMod = checkNotNull(modService.saveMod(mod));

//            actorService
//                    .onModNameOrTypeUpdate(
//                            savedMod.getTenantId(),
//                            savedMod.getId(),
//                            savedMod.getName(),
//                            savedMod.getType());

            logEntityAction(savedMod.getId(), savedMod,
                    savedMod.getCustomerId(),
                    mod.getId() == null ? ActionType.ADDED : ActionType.UPDATED, null);

            if (mod.getId() == null) {
                modStateService.onModAdded(savedMod);
            } else {
                modStateService.onModUpdated(savedMod);
            }
            return savedMod;
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE), mod,
                    null, mod.getId() == null ? ActionType.ADDED : ActionType.UPDATED, e);
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/mod/{modId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteMod(@PathVariable(DEVICE_ID) String strModId) throws ThingsboardException {
        checkParameter(DEVICE_ID, strModId);
        try {
            ModId modId = new ModId(toUUID(strModId));
            Mod mod = checkModId(modId);
            modService.deleteMod(modId);

            logEntityAction(modId, mod,
                    mod.getCustomerId(),
                    ActionType.DELETED, null, strModId);

            modStateService.onModDeleted(mod);
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE),
                    null,
                    null,
                    ActionType.DELETED, e, strModId);
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer/{customerId}/mod/{modId}", method = RequestMethod.POST)
    @ResponseBody
    public Mod assignModToCustomer(@PathVariable("customerId") String strCustomerId,
                                         @PathVariable(DEVICE_ID) String strModId) throws ThingsboardException {
        checkParameter("customerId", strCustomerId);
        checkParameter(DEVICE_ID, strModId);
        try {
            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
            Customer customer = checkCustomerId(customerId);

            ModId modId = new ModId(toUUID(strModId));
            checkModId(modId);

            Mod savedMod = checkNotNull(modService.assignModToCustomer(modId, customerId));

            logEntityAction(modId, savedMod,
                    savedMod.getCustomerId(),
                    ActionType.ASSIGNED_TO_CUSTOMER, null, strModId, strCustomerId, customer.getName());

            return savedMod;
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE), null,
                    null,
                    ActionType.ASSIGNED_TO_CUSTOMER, e, strModId, strCustomerId);
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer/mod/{modId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Mod unassignModFromCustomer(@PathVariable(DEVICE_ID) String strModId) throws ThingsboardException {
        checkParameter(DEVICE_ID, strModId);
        try {
            ModId modId = new ModId(toUUID(strModId));
            Mod mod = checkModId(modId);
            if (mod.getCustomerId() == null || mod.getCustomerId().getId().equals(ModelConstants.NULL_UUID)) {
                throw new IncorrectParameterException("Mod isn't assigned to any customer!");
            }
            Customer customer = checkCustomerId(mod.getCustomerId());

            Mod savedMod = checkNotNull(modService.unassignModFromCustomer(modId));

            logEntityAction(modId, mod,
                    mod.getCustomerId(),
                    ActionType.UNASSIGNED_FROM_CUSTOMER, null, strModId, customer.getId().toString(), customer.getName());

            return savedMod;
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE), null,
                    null,
                    ActionType.UNASSIGNED_FROM_CUSTOMER, e, strModId);
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer/public/mod/{modId}", method = RequestMethod.POST)
    @ResponseBody
    public Mod assignModToPublicCustomer(@PathVariable(DEVICE_ID) String strModId) throws ThingsboardException {
        checkParameter(DEVICE_ID, strModId);
        try {
            ModId modId = new ModId(toUUID(strModId));
            Mod mod = checkModId(modId);
            Customer publicCustomer = customerService.findOrCreatePublicCustomer(mod.getTenantId());
            Mod savedMod = checkNotNull(modService.assignModToCustomer(modId, publicCustomer.getId()));

            logEntityAction(modId, savedMod,
                    savedMod.getCustomerId(),
                    ActionType.ASSIGNED_TO_CUSTOMER, null, strModId, publicCustomer.getId().toString(), publicCustomer.getName());

            return savedMod;
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE), null,
                    null,
                    ActionType.ASSIGNED_TO_CUSTOMER, e, strModId);
            throw handleException(e);
        }
    }



    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/mods", params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<Mod> getTenantMods(
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            TextPageLink pageLink = createPageLink(limit, textSearch, idOffset, textOffset);
            if (type != null && type.trim().length() > 0) {
                return checkNotNull(modService.findModsByTenantIdAndType(tenantId, type, pageLink));
            } else {
                return checkNotNull(modService.findModsByTenantId(tenantId, pageLink));
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/mods", params = {"modName"}, method = RequestMethod.GET)
    @ResponseBody
    public Mod getTenantMod(
            @RequestParam String modName) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            return checkNotNull(modService.findModByTenantIdAndName(tenantId, modName));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer/{customerId}/mods", params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<Mod> getCustomerMods(
            @PathVariable("customerId") String strCustomerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws ThingsboardException {
        checkParameter("customerId", strCustomerId);
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
            checkCustomerId(customerId);
            TextPageLink pageLink = createPageLink(limit, textSearch, idOffset, textOffset);
            if (type != null && type.trim().length() > 0) {
                return checkNotNull(modService.findModsByTenantIdAndCustomerIdAndType(tenantId, customerId, type, pageLink));
            } else {
                return checkNotNull(modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink));
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/mods", params = {"modIds"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Mod> getModsByIds(
            @RequestParam("modIds") String[] strModIds) throws ThingsboardException {
        checkArrayParameter("modIds", strModIds);
        try {
            SecurityUser user = getCurrentUser();
            TenantId tenantId = user.getTenantId();
            CustomerId customerId = user.getCustomerId();
            List<ModId> modIds = new ArrayList<>();
            for (String strModId : strModIds) {
                modIds.add(new ModId(toUUID(strModId)));
            }
            ListenableFuture<List<Mod>> mods;
            if (customerId == null || customerId.isNullUid()) {
                mods = modService.findModsByTenantIdAndIdsAsync(tenantId, modIds);
            } else {
                mods = modService.findModsByTenantIdCustomerIdAndIdsAsync(tenantId, customerId, modIds);
            }
            return checkNotNull(mods.get());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/mods", method = RequestMethod.POST)
    @ResponseBody
    public List<Mod> findByQuery(@RequestBody ModSearchQuery query) throws ThingsboardException {
        checkNotNull(query);
        checkNotNull(query.getParameters());
        checkNotNull(query.getModTypes());
        checkEntityId(query.getParameters().getEntityId());
        try {
            List<Mod> mods = checkNotNull(modService.findModsByQuery(query).get());
            mods = mods.stream().filter(mod -> {
                try {
                    checkMod(mod);
                    return true;
                } catch (ThingsboardException e) {
                    return false;
                }
            }).collect(Collectors.toList());
            return mods;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/mod/types", method = RequestMethod.GET)
    @ResponseBody
    public List<EntitySubtype> getModTypes() throws ThingsboardException {
        try {
            SecurityUser user = getCurrentUser();
            TenantId tenantId = user.getTenantId();
            ListenableFuture<List<EntitySubtype>> modTypes = modService.findModTypesByTenantId(tenantId);
            return checkNotNull(modTypes.get());
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
