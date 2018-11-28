package org.thingsboard.server.controller;

import static org.hamcrest.Matchers.containsString;
import static org.thingsboard.server.dao.model.ModelConstants.NULL_UUID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.datastax.driver.core.utils.UUIDs;
import org.apache.commons.lang3.RandomStringUtils;
import org.thingsboard.server.common.data.*;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.ModCredentialsId;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.security.Authority;
import org.thingsboard.server.dao.model.ModelConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

public abstract class BaseModControllerTest extends AbstractControllerTest {

    private IdComparator<Mod> idComparator = new IdComparator<>();

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

        doDelete("/api/tenant/"+savedTenant.getId().getId().toString())
        .andExpect(status().isOk());
    }

    @Test
    public void testSaveMod() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);

        Assert.assertNotNull(savedMod);
        Assert.assertNotNull(savedMod.getId());
        Assert.assertTrue(savedMod.getCreatedTime() > 0);
        Assert.assertEquals(savedTenant.getId(), savedMod.getTenantId());
        Assert.assertNotNull(savedMod.getCustomerId());
        Assert.assertEquals(NULL_UUID, savedMod.getCustomerId().getId());
        Assert.assertEquals(mod.getName(), savedMod.getName());

//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//
//        Assert.assertNotNull(modCredentials);
//        Assert.assertNotNull(modCredentials.getId());
//        Assert.assertEquals(savedMod.getId(), modCredentials.getModId());
//        Assert.assertEquals(ModCredentialsType.ACCESS_TOKEN, modCredentials.getCredentialsType());
//        Assert.assertNotNull(modCredentials.getCredentialsId());
//        Assert.assertEquals(20, modCredentials.getCredentialsId().length());

        savedMod.setName("My new mod");
        doPost("/api/mod", savedMod, Mod.class);

        Mod foundMod = doGet("/api/mod/" + savedMod.getId().getId().toString(), Mod.class);
        Assert.assertEquals(foundMod.getName(), savedMod.getName());
    }

    @Test
    public void testFindModById() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
        Mod foundMod = doGet("/api/mod/" + savedMod.getId().getId().toString(), Mod.class);
        Assert.assertNotNull(foundMod);
        Assert.assertEquals(savedMod, foundMod);
    }

    @Test
    public void testFindModTypesByTenantId() throws Exception {
        List<Mod> mods = new ArrayList<>();
        for (int i=0;i<3;i++) {
            Mod mod = new Mod();
            mod.setName("My mod B"+i);
            mod.setType("typeB");
            mods.add(doPost("/api/mod", mod, Mod.class));
        }
        for (int i=0;i<7;i++) {
            Mod mod = new Mod();
            mod.setName("My mod C"+i);
            mod.setType("typeC");
            mods.add(doPost("/api/mod", mod, Mod.class));
        }
        for (int i=0;i<9;i++) {
            Mod mod = new Mod();
            mod.setName("My mod A"+i);
            mod.setType("typeA");
            mods.add(doPost("/api/mod", mod, Mod.class));
        }
        List<EntitySubtype> modTypes = doGetTyped("/api/mod/types",
                new TypeReference<List<EntitySubtype>>(){});

        Assert.assertNotNull(modTypes);
        Assert.assertEquals(3, modTypes.size());
        Assert.assertEquals("typeA", modTypes.get(0).getType());
        Assert.assertEquals("typeB", modTypes.get(1).getType());
        Assert.assertEquals("typeC", modTypes.get(2).getType());
    }

    @Test
    public void testDeleteMod() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);

        doDelete("/api/mod/"+savedMod.getId().getId().toString())
        .andExpect(status().isOk());

        doGet("/api/mod/"+savedMod.getId().getId().toString())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveModWithEmptyType() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        doPost("/api/mod", mod)
                .andExpect(status().isBadRequest())
                .andExpect(statusReason(containsString("Mod type should be specified")));
    }

    @Test
    public void testSaveModWithEmptyName() throws Exception {
        Mod mod = new Mod();
        mod.setType("default");
        doPost("/api/mod", mod)
        .andExpect(status().isBadRequest())
        .andExpect(statusReason(containsString("Mod name should be specified")));
    }

    @Test
    public void testAssignUnassignModToCustomer() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);

        Customer customer = new Customer();
        customer.setTitle("My customer");
        Customer savedCustomer = doPost("/api/customer", customer, Customer.class);

        Mod assignedMod = doPost("/api/customer/" + savedCustomer.getId().getId().toString()
                + "/mod/" + savedMod.getId().getId().toString(), Mod.class);
        Assert.assertEquals(savedCustomer.getId(), assignedMod.getCustomerId());

        Mod foundMod = doGet("/api/mod/" + savedMod.getId().getId().toString(), Mod.class);
        Assert.assertEquals(savedCustomer.getId(), foundMod.getCustomerId());

        Mod unassignedMod =
                doDelete("/api/customer/mod/" + savedMod.getId().getId().toString(), Mod.class);
        Assert.assertEquals(ModelConstants.NULL_UUID, unassignedMod.getCustomerId().getId());

        foundMod = doGet("/api/mod/" + savedMod.getId().getId().toString(), Mod.class);
        Assert.assertEquals(ModelConstants.NULL_UUID, foundMod.getCustomerId().getId());
    }

    @Test
    public void testAssignModToNonExistentCustomer() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);

        doPost("/api/customer/" + UUIDs.timeBased().toString()
                + "/mod/" + savedMod.getId().getId().toString())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testAssignModToCustomerFromDifferentTenant() throws Exception {
        loginSysAdmin();

        Tenant tenant2 = new Tenant();
        tenant2.setTitle("Different tenant");
        Tenant savedTenant2 = doPost("/api/tenant", tenant2, Tenant.class);
        Assert.assertNotNull(savedTenant2);

        User tenantAdmin2 = new User();
        tenantAdmin2.setAuthority(Authority.TENANT_ADMIN);
        tenantAdmin2.setTenantId(savedTenant2.getId());
        tenantAdmin2.setEmail("tenant3@thingsboard.org");
        tenantAdmin2.setFirstName("Joe");
        tenantAdmin2.setLastName("Downs");

        tenantAdmin2 = createUserAndLogin(tenantAdmin2, "testPassword1");

        Customer customer = new Customer();
        customer.setTitle("Different customer");
        Customer savedCustomer = doPost("/api/customer", customer, Customer.class);

        login(tenantAdmin.getEmail(), "testPassword1");

        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);

        doPost("/api/customer/" + savedCustomer.getId().getId().toString()
                + "/mod/" + savedMod.getId().getId().toString())
        .andExpect(status().isForbidden());

        loginSysAdmin();

        doDelete("/api/tenant/"+savedTenant2.getId().getId().toString())
        .andExpect(status().isOk());
    }

    @Test
    public void testFindModCredentialsByModId() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//        Assert.assertEquals(savedMod.getId(), modCredentials.getModId());
    }

    @Test
    public void testSaveModCredentials() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//        Assert.assertEquals(savedMod.getId(), modCredentials.getModId());
//        modCredentials.setCredentialsType(ModCredentialsType.ACCESS_TOKEN);
//        modCredentials.setCredentialsId("access_token");
//        doPost("/api/mod/credentials", modCredentials)
//        .andExpect(status().isOk());
//
//        ModCredentials foundModCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//
//        Assert.assertEquals(modCredentials, foundModCredentials);
    }

    @Test
    public void testSaveModCredentialsWithEmptyMod() throws Exception {
//        ModCredentials modCredentials = new ModCredentials();
//        doPost("/api/mod/credentials", modCredentials)
//        .andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveModCredentialsWithEmptyCredentialsType() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//        modCredentials.setCredentialsType(null);
//        doPost("/api/mod/credentials", modCredentials)
//        .andExpect(status().isBadRequest())
//        .andExpect(statusReason(containsString("Mod credentials type should be specified")));
    }

    @Test
    public void testSaveModCredentialsWithEmptyCredentialsId() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//        modCredentials.setCredentialsId(null);
//        doPost("/api/mod/credentials", modCredentials)
//        .andExpect(status().isBadRequest())
//        .andExpect(statusReason(containsString("Mod credentials id should be specified")));
    }

    @Test
    public void testSaveNonExistentModCredentials() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//        ModCredentials newModCredentials = new ModCredentials(new ModCredentialsId(UUIDs.timeBased()));
//        newModCredentials.setCreatedTime(modCredentials.getCreatedTime());
//        newModCredentials.setModId(modCredentials.getModId());
//        newModCredentials.setCredentialsType(modCredentials.getCredentialsType());
//        newModCredentials.setCredentialsId(modCredentials.getCredentialsId());
//        doPost("/api/mod/credentials", newModCredentials)
//        .andExpect(status().isBadRequest())
//        .andExpect(statusReason(containsString("Unable to update non-existent mod credentials")));
    }

    @Test
    public void testSaveModCredentialsWithNonExistentMod() throws Exception {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = doPost("/api/mod", mod, Mod.class);
//        ModCredentials modCredentials =
//                doGet("/api/mod/" + savedMod.getId().getId().toString() + "/credentials", ModCredentials.class);
//        modCredentials.setModId(new ModId(UUIDs.timeBased()));
//        doPost("/api/mod/credentials", modCredentials)
//        .andExpect(status().isNotFound());
    }

    @Test
    public void testFindTenantMods() throws Exception {
        List<Mod> mods = new ArrayList<>();
        for (int i=0;i<178;i++) {
            Mod mod = new Mod();
            mod.setName("Mod"+i);
            mod.setType("default");
            mods.add(doPost("/api/mod", mod, Mod.class));
        }
        List<Mod> loadedMods = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(23);
        TextPageData<Mod> pageData = null;
        do {
            pageData = doGetTypedWithPageLink("/api/tenant/mods?",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink);
            loadedMods.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(mods, idComparator);
        Collections.sort(loadedMods, idComparator);

        Assert.assertEquals(mods, loadedMods);
    }

    @Test
    public void testFindTenantModsByName() throws Exception {
        String title1 = "Mod title 1";
        List<Mod> modsTitle1 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            modsTitle1.add(doPost("/api/mod", mod, Mod.class));
        }
        String title2 = "Mod title 2";
        List<Mod> modsTitle2 = new ArrayList<>();
        for (int i=0;i<75;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            modsTitle2.add(doPost("/api/mod", mod, Mod.class));
        }

        List<Mod> loadedModsTitle1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15, title1);
        TextPageData<Mod> pageData = null;
        do {
            pageData = doGetTypedWithPageLink("/api/tenant/mods?",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink);
            loadedModsTitle1.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsTitle1, idComparator);
        Collections.sort(loadedModsTitle1, idComparator);

        Assert.assertEquals(modsTitle1, loadedModsTitle1);

        List<Mod> loadedModsTitle2 = new ArrayList<>();
        pageLink = new TextPageLink(4, title2);
        do {
            pageData = doGetTypedWithPageLink("/api/tenant/mods?",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink);
            loadedModsTitle2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsTitle2, idComparator);
        Collections.sort(loadedModsTitle2, idComparator);

        Assert.assertEquals(modsTitle2, loadedModsTitle2);

        for (Mod mod : loadedModsTitle1) {
            doDelete("/api/mod/"+mod.getId().getId().toString())
            .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4, title1);
        pageData = doGetTypedWithPageLink("/api/tenant/mods?",
                new TypeReference<TextPageData<Mod>>(){}, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsTitle2) {
            doDelete("/api/mod/"+mod.getId().getId().toString())
            .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4, title2);
        pageData = doGetTypedWithPageLink("/api/tenant/mods?",
                new TypeReference<TextPageData<Mod>>(){}, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
    }

    @Test
    public void testFindTenantModsByType() throws Exception {
        String title1 = "Mod title 1";
        String type1 = "typeA";
        List<Mod> modsType1 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type1);
            modsType1.add(doPost("/api/mod", mod, Mod.class));
        }
        String title2 = "Mod title 2";
        String type2 = "typeB";
        List<Mod> modsType2 = new ArrayList<>();
        for (int i=0;i<75;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type2);
            modsType2.add(doPost("/api/mod", mod, Mod.class));
        }

        List<Mod> loadedModsType1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15);
        TextPageData<Mod> pageData = null;
        do {
            pageData = doGetTypedWithPageLink("/api/tenant/mods?type={type}&",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink, type1);
            loadedModsType1.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsType1, idComparator);
        Collections.sort(loadedModsType1, idComparator);

        Assert.assertEquals(modsType1, loadedModsType1);

        List<Mod> loadedModsType2 = new ArrayList<>();
        pageLink = new TextPageLink(4);
        do {
            pageData = doGetTypedWithPageLink("/api/tenant/mods?type={type}&",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink, type2);
            loadedModsType2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsType2, idComparator);
        Collections.sort(loadedModsType2, idComparator);

        Assert.assertEquals(modsType2, loadedModsType2);

        for (Mod mod : loadedModsType1) {
            doDelete("/api/mod/"+mod.getId().getId().toString())
                    .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4);
        pageData = doGetTypedWithPageLink("/api/tenant/mods?type={type}&",
                new TypeReference<TextPageData<Mod>>(){}, pageLink, type1);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsType2) {
            doDelete("/api/mod/"+mod.getId().getId().toString())
                    .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4);
        pageData = doGetTypedWithPageLink("/api/tenant/mods?type={type}&",
                new TypeReference<TextPageData<Mod>>(){}, pageLink, type2);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
    }

    @Test
    public void testFindCustomerMods() throws Exception {
        Customer customer = new Customer();
        customer.setTitle("Test customer");
        customer = doPost("/api/customer", customer, Customer.class);
        CustomerId customerId = customer.getId();

        List<Mod> mods = new ArrayList<>();
        for (int i=0;i<128;i++) {
            Mod mod = new Mod();
            mod.setName("Mod"+i);
            mod.setType("default");
            mod = doPost("/api/mod", mod, Mod.class);
            mods.add(doPost("/api/customer/" + customerId.getId().toString()
                            + "/mod/" + mod.getId().getId().toString(), Mod.class));
        }

        List<Mod> loadedMods = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(23);
        TextPageData<Mod> pageData = null;
        do {
            pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink);
            loadedMods.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(mods, idComparator);
        Collections.sort(loadedMods, idComparator);

        Assert.assertEquals(mods, loadedMods);
    }

    @Test
    public void testFindCustomerModsByName() throws Exception {
        Customer customer = new Customer();
        customer.setTitle("Test customer");
        customer = doPost("/api/customer", customer, Customer.class);
        CustomerId customerId = customer.getId();

        String title1 = "Mod title 1";
        List<Mod> modsTitle1 = new ArrayList<>();
        for (int i=0;i<125;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            mod = doPost("/api/mod", mod, Mod.class);
            modsTitle1.add(doPost("/api/customer/" + customerId.getId().toString()
                    + "/mod/" + mod.getId().getId().toString(), Mod.class));
        }
        String title2 = "Mod title 2";
        List<Mod> modsTitle2 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            mod = doPost("/api/mod", mod, Mod.class);
            modsTitle2.add(doPost("/api/customer/" + customerId.getId().toString()
                    + "/mod/" + mod.getId().getId().toString(), Mod.class));
        }

        List<Mod> loadedModsTitle1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15, title1);
        TextPageData<Mod> pageData = null;
        do {
            pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink);
            loadedModsTitle1.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsTitle1, idComparator);
        Collections.sort(loadedModsTitle1, idComparator);

        Assert.assertEquals(modsTitle1, loadedModsTitle1);

        List<Mod> loadedModsTitle2 = new ArrayList<>();
        pageLink = new TextPageLink(4, title2);
        do {
            pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink);
            loadedModsTitle2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsTitle2, idComparator);
        Collections.sort(loadedModsTitle2, idComparator);

        Assert.assertEquals(modsTitle2, loadedModsTitle2);

        for (Mod mod : loadedModsTitle1) {
            doDelete("/api/customer/mod/" + mod.getId().getId().toString())
            .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4, title1);
        pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?",
                new TypeReference<TextPageData<Mod>>(){}, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsTitle2) {
            doDelete("/api/customer/mod/" + mod.getId().getId().toString())
            .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4, title2);
        pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?",
                new TypeReference<TextPageData<Mod>>(){}, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
    }

    @Test
    public void testFindCustomerModsByType() throws Exception {
        Customer customer = new Customer();
        customer.setTitle("Test customer");
        customer = doPost("/api/customer", customer, Customer.class);
        CustomerId customerId = customer.getId();

        String title1 = "Mod title 1";
        String type1 = "typeC";
        List<Mod> modsType1 = new ArrayList<>();
        for (int i=0;i<125;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type1);
            mod = doPost("/api/mod", mod, Mod.class);
            modsType1.add(doPost("/api/customer/" + customerId.getId().toString()
                    + "/mod/" + mod.getId().getId().toString(), Mod.class));
        }
        String title2 = "Mod title 2";
        String type2 = "typeD";
        List<Mod> modsType2 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type2);
            mod = doPost("/api/mod", mod, Mod.class);
            modsType2.add(doPost("/api/customer/" + customerId.getId().toString()
                    + "/mod/" + mod.getId().getId().toString(), Mod.class));
        }

        List<Mod> loadedModsType1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15);
        TextPageData<Mod> pageData = null;
        do {
            pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?type={type}&",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink, type1);
            loadedModsType1.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsType1, idComparator);
        Collections.sort(loadedModsType1, idComparator);

        Assert.assertEquals(modsType1, loadedModsType1);

        List<Mod> loadedModsType2 = new ArrayList<>();
        pageLink = new TextPageLink(4);
        do {
            pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?type={type}&",
                    new TypeReference<TextPageData<Mod>>(){}, pageLink, type2);
            loadedModsType2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsType2, idComparator);
        Collections.sort(loadedModsType2, idComparator);

        Assert.assertEquals(modsType2, loadedModsType2);

        for (Mod mod : loadedModsType1) {
            doDelete("/api/customer/mod/" + mod.getId().getId().toString())
                    .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4);
        pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?type={type}&",
                new TypeReference<TextPageData<Mod>>(){}, pageLink, type1);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsType2) {
            doDelete("/api/customer/mod/" + mod.getId().getId().toString())
                    .andExpect(status().isOk());
        }

        pageLink = new TextPageLink(4);
        pageData = doGetTypedWithPageLink("/api/customer/" + customerId.getId().toString() + "/mods?type={type}&",
                new TypeReference<TextPageData<Mod>>(){}, pageLink, type2);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
    }

}
