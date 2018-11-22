package org.thingsboard.server.controller;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.App;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.audit.ActionType;
//import org.thingsboard.server.common.data.app.AppSearchQuery;
import org.thingsboard.server.common.data.exception.ThingsboardErrorCode;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.AppId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.security.Authority;
//import org.thingsboard.server.common.data.security.AppCredentials;
import org.thingsboard.server.dao.exception.IncorrectParameterException;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.service.security.model.SecurityUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController extends BaseController {

    public static final String APP_ID = "appId";

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/app/{appId}", method = RequestMethod.GET)
    @ResponseBody
    public App getAppById(@PathVariable(APP_ID) String strAppId) throws ThingsboardException {
        checkParameter(APP_ID, strAppId);
        try {
            AppId appId = new AppId(toUUID(strAppId));
            return checkAppId(appId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/app", method = RequestMethod.POST)
    @ResponseBody
    public App saveApp(@RequestBody App app) throws ThingsboardException {
        try {
            app.setTenantId(getCurrentUser().getTenantId());

//            if (getCurrentUser().getAuthority() == Authority.CUSTOMER_USER) {
//                if (app.getId() == null || app.getId().isNullUid() ||
//                        app.getCustomerId() == null || app.getCustomerId().isNullUid()) {
//                    throw new ThingsboardException("You don't have permission to perform this operation!",
//                            ThingsboardErrorCode.PERMISSION_DENIED);
//                } else {
//                    checkCustomerId(app.getCustomerId());
//                }
//            }
            App savedApp = checkNotNull(appService.saveApp(app));

//            actorService
//                    .onAppNameOrTypeUpdate(
//                            savedApp.getTenantId(),
//                            savedApp.getId(),
//                            savedApp.getName(),
//                            savedApp.getType());
//
//            logEntityAction(savedApp.getId(), savedApp,
//                    savedApp.getCustomerId(),
//                    app.getId() == null ? ActionType.ADDED : ActionType.UPDATED, null);

//            if (app.getId() == null) {
//                appStateService.onAppAdded(savedApp);
//            } else {
//                appStateService.onAppUpdated(savedApp);
//            }
            return savedApp;
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE), app,
                    null, app.getId() == null ? ActionType.ADDED : ActionType.UPDATED, e);
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/app/{appId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteApp(@PathVariable(APP_ID) String strAppId) throws ThingsboardException {
        checkParameter(APP_ID, strAppId);
        try {
            AppId appId = new AppId(toUUID(strAppId));
            App app = checkAppId(appId);
            appService.deleteApp(appId);

//            logEntityAction(appId, app,
//                    app.getCustomerId(),
//                    ActionType.DELETED, null, strAppId);
//
//            appStateService.onAppDeleted(app);
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE),
                    null,
                    null,
                    ActionType.DELETED, e, strAppId);
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
//    @RequestMapping(value = "/customer/{customerId}/app/{appId}", method = RequestMethod.POST)
//    @ResponseBody
//    public App assignAppToCustomer(@PathVariable("customerId") String strCustomerId,
//                                         @PathVariable(APP_ID) String strAppId) throws ThingsboardException {
//        checkParameter("customerId", strCustomerId);
//        checkParameter(APP_ID, strAppId);
//        try {
//            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
//            Customer customer = checkCustomerId(customerId);
//
//            AppId appId = new AppId(toUUID(strAppId));
//            checkAppId(appId);
//
//            App savedApp = checkNotNull(appService.assignAppToCustomer(appId, customerId));
//
//            logEntityAction(appId, savedApp,
//                    savedApp.getCustomerId(),
//                    ActionType.ASSIGNED_TO_CUSTOMER, null, strAppId, strCustomerId, customer.getName());
//
//            return savedApp;
//        } catch (Exception e) {
//            logEntityAction(emptyId(EntityType.DEVICE), null,
//                    null,
//                    ActionType.ASSIGNED_TO_CUSTOMER, e, strAppId, strCustomerId);
//            throw handleException(e);
//        }
//    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
//    @RequestMapping(value = "/customer/app/{appId}", method = RequestMethod.DELETE)
//    @ResponseBody
//    public App unassignAppFromCustomer(@PathVariable(APP_ID) String strAppId) throws ThingsboardException {
//        checkParameter(APP_ID, strAppId);
//        try {
//            AppId appId = new AppId(toUUID(strAppId));
//            App app = checkAppId(appId);
//            if (app.getCustomerId() == null || app.getCustomerId().getId().equals(ModelConstants.NULL_UUID)) {
//                throw new IncorrectParameterException("App isn't assigned to any customer!");
//            }
//            Customer customer = checkCustomerId(app.getCustomerId());
//
//            App savedApp = checkNotNull(appService.unassignAppFromCustomer(appId));
//
//            logEntityAction(appId, app,
//                    app.getCustomerId(),
//                    ActionType.UNASSIGNED_FROM_CUSTOMER, null, strAppId, customer.getId().toString(), customer.getName());
//
//            return savedApp;
//        } catch (Exception e) {
//            logEntityAction(emptyId(EntityType.DEVICE), null,
//                    null,
//                    ActionType.UNASSIGNED_FROM_CUSTOMER, e, strAppId);
//            throw handleException(e);
//        }
//    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
//    @RequestMapping(value = "/customer/public/app/{appId}", method = RequestMethod.POST)
//    @ResponseBody
//    public App assignAppToPublicCustomer(@PathVariable(APP_ID) String strAppId) throws ThingsboardException {
//        checkParameter(APP_ID, strAppId);
//        try {
//            AppId appId = new AppId(toUUID(strAppId));
//            App app = checkAppId(appId);
//            Customer publicCustomer = customerService.findOrCreatePublicCustomer(app.getTenantId());
//            App savedApp = checkNotNull(appService.assignAppToCustomer(appId, publicCustomer.getId()));
//
//            logEntityAction(appId, savedApp,
//                    savedApp.getCustomerId(),
//                    ActionType.ASSIGNED_TO_CUSTOMER, null, strAppId, publicCustomer.getId().toString(), publicCustomer.getName());
//
//            return savedApp;
//        } catch (Exception e) {
//            logEntityAction(emptyId(EntityType.DEVICE), null,
//                    null,
//                    ActionType.ASSIGNED_TO_CUSTOMER, e, strAppId);
//            throw handleException(e);
//        }
//    }
//
//    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
//    @RequestMapping(value = "/app/{appId}/credentials", method = RequestMethod.GET)
//    @ResponseBody
//    public AppCredentials getAppCredentialsByAppId(@PathVariable(APP_ID) String strAppId) throws ThingsboardException {
//        checkParameter(APP_ID, strAppId);
//        try {
//            AppId appId = new AppId(toUUID(strAppId));
//            App app = checkAppId(appId);
//            AppCredentials appCredentials = checkNotNull(appCredentialsService.findAppCredentialsByAppId(appId));
//            logEntityAction(appId, app,
//                    app.getCustomerId(),
//                    ActionType.CREDENTIALS_READ, null, strAppId);
//            return appCredentials;
//        } catch (Exception e) {
//            logEntityAction(emptyId(EntityType.DEVICE), null,
//                    null,
//                    ActionType.CREDENTIALS_READ, e, strAppId);
//            throw handleException(e);
//        }
//    }
//
//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
//    @RequestMapping(value = "/app/credentials", method = RequestMethod.POST)
//    @ResponseBody
//    public AppCredentials saveAppCredentials(@RequestBody AppCredentials appCredentials) throws ThingsboardException {
//        checkNotNull(appCredentials);
//        try {
//            App app = checkAppId(appCredentials.getAppId());
//            AppCredentials result = checkNotNull(appCredentialsService.updateAppCredentials(appCredentials));
//            actorService.onCredentialsUpdate(getCurrentUser().getTenantId(), appCredentials.getAppId());
//            logEntityAction(app.getId(), app,
//                    app.getCustomerId(),
//                    ActionType.CREDENTIALS_UPDATED, null, appCredentials);
//            return result;
//        } catch (Exception e) {
//            logEntityAction(emptyId(EntityType.DEVICE), null,
//                    null,
//                    ActionType.CREDENTIALS_UPDATED, e, appCredentials);
//            throw handleException(e);
//        }
//    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/app/list", params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<App> getTenantApps(
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            TextPageLink pageLink = createPageLink(limit, textSearch, idOffset, textOffset);
            if (type != null && type.trim().length() > 0) {
                return checkNotNull(appService.findAppsByTenantIdAndType(tenantId, type, pageLink));
            } else {
                return checkNotNull(appService.findAppsByTenantId(tenantId, pageLink));
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/apps", params = {"appName"}, method = RequestMethod.GET)
    @ResponseBody
    public App getTenantApp(
            @RequestParam String appName) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            return checkNotNull(appService.findAppByTenantIdAndName(tenantId, appName));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer/{customerId}/apps", params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<App> getCustomerApps(
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
                return checkNotNull(appService.findAppsByTenantIdAndCustomerIdAndType(tenantId, customerId, type, pageLink));
            } else {
                return checkNotNull(appService.findAppsByTenantIdAndCustomerId(tenantId, customerId, pageLink));
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/apps", params = {"appIds"}, method = RequestMethod.GET)
    @ResponseBody
    public List<App> getAppsByIds(
            @RequestParam("appIds") String[] strAppIds) throws ThingsboardException {
        checkArrayParameter("appIds", strAppIds);
        try {
            SecurityUser user = getCurrentUser();
            TenantId tenantId = user.getTenantId();
            CustomerId customerId = user.getCustomerId();
            List<AppId> appIds = new ArrayList<>();
            for (String strAppId : strAppIds) {
                appIds.add(new AppId(toUUID(strAppId)));
            }
            ListenableFuture<List<App>> apps;
//            if (customerId == null || customerId.isNullUid()) {
                apps = appService.findAppsByTenantIdAndIdsAsync(tenantId, appIds);
//            } else {
//                apps = appService.findAppsByTenantIdCustomerIdAndIdsAsync(tenantId, customerId, appIds);
//            }
            return checkNotNull(apps.get());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
//    @RequestMapping(value = "/apps", method = RequestMethod.POST)
//    @ResponseBody
//    public List<App> findByQuery(@RequestBody AppSearchQuery query) throws ThingsboardException {
//        checkNotNull(query);
//        checkNotNull(query.getParameters());
//        checkNotNull(query.getAppTypes());
//        checkEntityId(query.getParameters().getEntityId());
//        try {
//            List<App> apps = checkNotNull(appService.findAppsByQuery(query).get());
//            apps = apps.stream().filter(app -> {
//                try {
//                    checkApp(app);
//                    return true;
//                } catch (ThingsboardException e) {
//                    return false;
//                }
//            }).collect(Collectors.toList());
//            return apps;
//        } catch (Exception e) {
//            throw handleException(e);
//        }
//    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/app/types", method = RequestMethod.GET)
    @ResponseBody
    public List<EntitySubtype> getAppTypes() throws ThingsboardException {
        try {
            SecurityUser user = getCurrentUser();
            TenantId tenantId = user.getTenantId();
            ListenableFuture<List<EntitySubtype>> appTypes = appService.findAppTypesByTenantId(tenantId);
            return checkNotNull(appTypes.get());
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
