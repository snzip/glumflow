package org.thingsboard.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.User;
import org.thingsboard.server.common.data.audit.AuditLog;
import org.thingsboard.server.common.data.page.TimePageData;
import org.thingsboard.server.common.data.page.TimePageLink;
import org.thingsboard.server.common.data.security.Authority;
import org.thingsboard.server.dao.model.ModelConstants;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseAuditLogControllerTest extends AbstractControllerTest {

    private Tenant savedTenant;
    private User tenantAdmin;

    @Before
    public void beforeTest() throws Exception {
        loginSysAdmin();

        Tenant tenant = new Tenant();
        tenant.setTitle("My tenant");
        savedTenant = doPost("/api/tenant", tenant, Tenant.class);
        Assert.assertNotNull(savedTenant);

        tenantAdmin = new User();
        tenantAdmin.setAuthority(Authority.TENANT_ADMIN);
        tenantAdmin.setTenantId(savedTenant.getId());
        tenantAdmin.setEmail("tenant2@thingsboard.org");
        tenantAdmin.setFirstName("Joe");
        tenantAdmin.setLastName("Downs");

        tenantAdmin = createUserAndLogin(tenantAdmin, "testPassword1");
    }

    @After
    public void afterTest() throws Exception {
        loginSysAdmin();

        doDelete("/api/tenant/" + savedTenant.getId().getId().toString())
                .andExpect(status().isOk());
    }

    @Test
    public void testAuditLogs() throws Exception {
        for (int i = 0; i < 178; i++) {
            Device device = new Device();
            device.setName("Device" + i);
            device.setType("default");
            doPost("/api/device", device, Device.class);
        }

        List<AuditLog> loadedAuditLogs = new ArrayList<>();
        TimePageLink pageLink = new TimePageLink(23);
        TimePageData<AuditLog> pageData;
        do {
            pageData = doGetTypedWithTimePageLink("/api/audit/logs?",
                    new TypeReference<TimePageData<AuditLog>>() {
                    }, pageLink);
            loadedAuditLogs.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Assert.assertEquals(178, loadedAuditLogs.size());

        loadedAuditLogs = new ArrayList<>();
        pageLink = new TimePageLink(23);
        do {
            pageData = doGetTypedWithTimePageLink("/api/audit/logs/customer/" + ModelConstants.NULL_UUID + "?",
                    new TypeReference<TimePageData<AuditLog>>() {
                    }, pageLink);
            loadedAuditLogs.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Assert.assertEquals(178, loadedAuditLogs.size());

        loadedAuditLogs = new ArrayList<>();
        pageLink = new TimePageLink(23);
        do {
            pageData = doGetTypedWithTimePageLink("/api/audit/logs/user/" + tenantAdmin.getId().getId().toString() + "?",
                    new TypeReference<TimePageData<AuditLog>>() {
                    }, pageLink);
            loadedAuditLogs.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Assert.assertEquals(178, loadedAuditLogs.size());
    }

    @Test
    public void testAuditLogs_byTenantIdAndEntityId() throws Exception {
        Device device = new Device();
        device.setName("Device name");
        device.setType("default");
        Device savedDevice = doPost("/api/device", device, Device.class);
        for (int i = 0; i < 178; i++) {
            savedDevice.setName("Device name" + i);
            doPost("/api/device", savedDevice, Device.class);
        }

        List<AuditLog> loadedAuditLogs = new ArrayList<>();
        TimePageLink pageLink = new TimePageLink(23);
        TimePageData<AuditLog> pageData;
        do {
            pageData = doGetTypedWithTimePageLink("/api/audit/logs/entity/DEVICE/" + savedDevice.getId().getId() + "?",
                    new TypeReference<TimePageData<AuditLog>>() {
                    }, pageLink);
            loadedAuditLogs.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Assert.assertEquals(179, loadedAuditLogs.size());
    }
}
