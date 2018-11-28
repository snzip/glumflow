package org.thingsboard.server.dao.service;

import com.datastax.driver.core.utils.UUIDs;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.EntitySubtype;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.exception.DataValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.thingsboard.server.dao.model.ModelConstants.NULL_UUID;

public abstract class BaseModServiceTest extends AbstractServiceTest {

    private IdComparator<Mod> idComparator = new IdComparator<>();

    private TenantId tenantId;

    @Before
    public void before() {
        Tenant tenant = new Tenant();
        tenant.setTitle("My tenant");
        Tenant savedTenant = tenantService.saveTenant(tenant);
        Assert.assertNotNull(savedTenant);
        tenantId = savedTenant.getId();
    }

    @After
    public void after() {
        tenantService.deleteTenant(tenantId);
    }

    @Test
    public void testSaveMod() {
        Mod mod = new Mod();
        mod.setTenantId(tenantId);
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = modService.saveMod(mod);

        Assert.assertNotNull(savedMod);
        Assert.assertNotNull(savedMod.getId());
        Assert.assertTrue(savedMod.getCreatedTime() > 0);
        Assert.assertEquals(mod.getTenantId(), savedMod.getTenantId());
        Assert.assertNotNull(savedMod.getCustomerId());
        Assert.assertEquals(NULL_UUID, savedMod.getCustomerId().getId());
        Assert.assertEquals(mod.getName(), savedMod.getName());


        savedMod.setName("My new mod");

        modService.saveMod(savedMod);
        Mod foundMod = modService.findModById(savedMod.getId());
        Assert.assertEquals(foundMod.getName(), savedMod.getName());

        modService.deleteMod(savedMod.getId());
    }

    @Test(expected = DataValidationException.class)
    public void testSaveModWithEmptyName() {
        Mod mod = new Mod();
        mod.setType("default");
        mod.setTenantId(tenantId);
        modService.saveMod(mod);
    }

    @Test(expected = DataValidationException.class)
    public void testSaveModWithEmptyTenant() {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        modService.saveMod(mod);
    }

    @Test(expected = DataValidationException.class)
    public void testSaveModWithInvalidTenant() {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        mod.setTenantId(new TenantId(UUIDs.timeBased()));
        modService.saveMod(mod);
    }

    @Test(expected = DataValidationException.class)
    public void testAssignModToNonExistentCustomer() {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        mod.setTenantId(tenantId);
        mod = modService.saveMod(mod);
        try {
            modService.assignModToCustomer(mod.getId(), new CustomerId(UUIDs.timeBased()));
        } finally {
            modService.deleteMod(mod.getId());
        }
    }

    @Test(expected = DataValidationException.class)
    public void testAssignModToCustomerFromDifferentTenant() {
        Mod mod = new Mod();
        mod.setName("My mod");
        mod.setType("default");
        mod.setTenantId(tenantId);
        mod = modService.saveMod(mod);
        Tenant tenant = new Tenant();
        tenant.setTitle("Test different tenant");
        tenant = tenantService.saveTenant(tenant);
        Customer customer = new Customer();
        customer.setTenantId(tenant.getId());
        customer.setTitle("Test different customer");
        customer = customerService.saveCustomer(customer);
        try {
            modService.assignModToCustomer(mod.getId(), customer.getId());
        } finally {
            modService.deleteMod(mod.getId());
            tenantService.deleteTenant(tenant.getId());
        }
    }

    @Test
    public void testFindModById() {
        Mod mod = new Mod();
        mod.setTenantId(tenantId);
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = modService.saveMod(mod);
        Mod foundMod = modService.findModById(savedMod.getId());
        Assert.assertNotNull(foundMod);
        Assert.assertEquals(savedMod, foundMod);
        modService.deleteMod(savedMod.getId());
    }

    @Test
    public void testFindModTypesByTenantId() throws Exception {
        List<Mod> mods = new ArrayList<>();
        try {
            for (int i=0;i<3;i++) {
                Mod mod = new Mod();
                mod.setTenantId(tenantId);
                mod.setName("My mod B"+i);
                mod.setType("typeB");
                mods.add(modService.saveMod(mod));
            }
            for (int i=0;i<7;i++) {
                Mod mod = new Mod();
                mod.setTenantId(tenantId);
                mod.setName("My mod C"+i);
                mod.setType("typeC");
                mods.add(modService.saveMod(mod));
            }
            for (int i=0;i<9;i++) {
                Mod mod = new Mod();
                mod.setTenantId(tenantId);
                mod.setName("My mod A"+i);
                mod.setType("typeA");
                mods.add(modService.saveMod(mod));
            }
            List<EntitySubtype> modTypes = modService.findModTypesByTenantId(tenantId).get();
            Assert.assertNotNull(modTypes);
            Assert.assertEquals(3, modTypes.size());
            Assert.assertEquals("typeA", modTypes.get(0).getType());
            Assert.assertEquals("typeB", modTypes.get(1).getType());
            Assert.assertEquals("typeC", modTypes.get(2).getType());
        } finally {
            mods.forEach((mod) -> { modService.deleteMod(mod.getId()); });
        }
    }

    @Test
    public void testDeleteMod() {
        Mod mod = new Mod();
        mod.setTenantId(tenantId);
        mod.setName("My mod");
        mod.setType("default");
        Mod savedMod = modService.saveMod(mod);
        Mod foundMod = modService.findModById(savedMod.getId());
        Assert.assertNotNull(foundMod);
        modService.deleteMod(savedMod.getId());
        foundMod = modService.findModById(savedMod.getId());
        Assert.assertNull(foundMod);
    }

    @Test
    public void testFindModsByTenantId() {
        Tenant tenant = new Tenant();
        tenant.setTitle("Test tenant");
        tenant = tenantService.saveTenant(tenant);

        TenantId tenantId = tenant.getId();

        List<Mod> mods = new ArrayList<>();
        for (int i=0;i<178;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            mod.setName("Mod"+i);
            mod.setType("default");
            mods.add(modService.saveMod(mod));
        }

        List<Mod> loadedMods = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(23);
        TextPageData<Mod> pageData = null;
        do {
            pageData = modService.findModsByTenantId(tenantId, pageLink);
            loadedMods.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(mods, idComparator);
        Collections.sort(loadedMods, idComparator);

        Assert.assertEquals(mods, loadedMods);

        modService.deleteModsByTenantId(tenantId);

        pageLink = new TextPageLink(33);
        pageData = modService.findModsByTenantId(tenantId, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertTrue(pageData.getData().isEmpty());

        tenantService.deleteTenant(tenantId);
    }

    @Test
    public void testFindModsByTenantIdAndName() {
        String title1 = "Mod title 1";
        List<Mod> modsTitle1 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            modsTitle1.add(modService.saveMod(mod));
        }
        String title2 = "Mod title 2";
        List<Mod> modsTitle2 = new ArrayList<>();
        for (int i=0;i<175;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            modsTitle2.add(modService.saveMod(mod));
        }

        List<Mod> loadedModsTitle1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15, title1);
        TextPageData<Mod> pageData = null;
        do {
            pageData = modService.findModsByTenantId(tenantId, pageLink);
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
            pageData = modService.findModsByTenantId(tenantId, pageLink);
            loadedModsTitle2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsTitle2, idComparator);
        Collections.sort(loadedModsTitle2, idComparator);

        Assert.assertEquals(modsTitle2, loadedModsTitle2);

        for (Mod mod : loadedModsTitle1) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4, title1);
        pageData = modService.findModsByTenantId(tenantId, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsTitle2) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4, title2);
        pageData = modService.findModsByTenantId(tenantId, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
    }

    @Test
    public void testFindModsByTenantIdAndType() {
        String title1 = "Mod title 1";
        String type1 = "typeA";
        List<Mod> modsType1 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type1);
            modsType1.add(modService.saveMod(mod));
        }
        String title2 = "Mod title 2";
        String type2 = "typeB";
        List<Mod> modsType2 = new ArrayList<>();
        for (int i=0;i<175;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type2);
            modsType2.add(modService.saveMod(mod));
        }

        List<Mod> loadedModsType1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15);
        TextPageData<Mod> pageData = null;
        do {
            pageData = modService.findModsByTenantIdAndType(tenantId, type1, pageLink);
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
            pageData = modService.findModsByTenantIdAndType(tenantId, type2, pageLink);
            loadedModsType2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsType2, idComparator);
        Collections.sort(loadedModsType2, idComparator);

        Assert.assertEquals(modsType2, loadedModsType2);

        for (Mod mod : loadedModsType1) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4);
        pageData = modService.findModsByTenantIdAndType(tenantId, type1, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsType2) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4);
        pageData = modService.findModsByTenantIdAndType(tenantId, type2, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
    }

    @Test
    public void testFindModsByTenantIdAndCustomerId() {
        Tenant tenant = new Tenant();
        tenant.setTitle("Test tenant");
        tenant = tenantService.saveTenant(tenant);

        TenantId tenantId = tenant.getId();

        Customer customer = new Customer();
        customer.setTitle("Test customer");
        customer.setTenantId(tenantId);
        customer = customerService.saveCustomer(customer);
        CustomerId customerId = customer.getId();

        List<Mod> mods = new ArrayList<>();
        for (int i=0;i<278;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            mod.setName("Mod"+i);
            mod.setType("default");
            mod = modService.saveMod(mod);
            mods.add(modService.assignModToCustomer(mod.getId(), customerId));
        }

        List<Mod> loadedMods = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(23);
        TextPageData<Mod> pageData = null;
        do {
            pageData = modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
            loadedMods.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(mods, idComparator);
        Collections.sort(loadedMods, idComparator);

        Assert.assertEquals(mods, loadedMods);

        modService.unassignCustomerMods(tenantId, customerId);

        pageLink = new TextPageLink(33);
        pageData = modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertTrue(pageData.getData().isEmpty());

        tenantService.deleteTenant(tenantId);
    }

    @Test
    public void testFindModsByTenantIdCustomerIdAndName() {

        Customer customer = new Customer();
        customer.setTitle("Test customer");
        customer.setTenantId(tenantId);
        customer = customerService.saveCustomer(customer);
        CustomerId customerId = customer.getId();

        String title1 = "Mod title 1";
        List<Mod> modsTitle1 = new ArrayList<>();
        for (int i=0;i<175;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            mod = modService.saveMod(mod);
            modsTitle1.add(modService.assignModToCustomer(mod.getId(), customerId));
        }
        String title2 = "Mod title 2";
        List<Mod> modsTitle2 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType("default");
            mod = modService.saveMod(mod);
            modsTitle2.add(modService.assignModToCustomer(mod.getId(), customerId));
        }

        List<Mod> loadedModsTitle1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15, title1);
        TextPageData<Mod> pageData = null;
        do {
            pageData = modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
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
            pageData = modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
            loadedModsTitle2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsTitle2, idComparator);
        Collections.sort(loadedModsTitle2, idComparator);

        Assert.assertEquals(modsTitle2, loadedModsTitle2);

        for (Mod mod : loadedModsTitle1) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4, title1);
        pageData = modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsTitle2) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4, title2);
        pageData = modService.findModsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
        customerService.deleteCustomer(customerId);
    }

    @Test
    public void testFindModsByTenantIdCustomerIdAndType() {

        Customer customer = new Customer();
        customer.setTitle("Test customer");
        customer.setTenantId(tenantId);
        customer = customerService.saveCustomer(customer);
        CustomerId customerId = customer.getId();

        String title1 = "Mod title 1";
        String type1 = "typeC";
        List<Mod> modsType1 = new ArrayList<>();
        for (int i=0;i<175;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title1+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type1);
            mod = modService.saveMod(mod);
            modsType1.add(modService.assignModToCustomer(mod.getId(), customerId));
        }
        String title2 = "Mod title 2";
        String type2 = "typeD";
        List<Mod> modsType2 = new ArrayList<>();
        for (int i=0;i<143;i++) {
            Mod mod = new Mod();
            mod.setTenantId(tenantId);
            String suffix = RandomStringUtils.randomAlphanumeric(15);
            String name = title2+suffix;
            name = i % 2 == 0 ? name.toLowerCase() : name.toUpperCase();
            mod.setName(name);
            mod.setType(type2);
            mod = modService.saveMod(mod);
            modsType2.add(modService.assignModToCustomer(mod.getId(), customerId));
        }

        List<Mod> loadedModsType1 = new ArrayList<>();
        TextPageLink pageLink = new TextPageLink(15);
        TextPageData<Mod> pageData = null;
        do {
            pageData = modService.findModsByTenantIdAndCustomerIdAndType(tenantId, customerId, type1, pageLink);
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
            pageData = modService.findModsByTenantIdAndCustomerIdAndType(tenantId, customerId, type2, pageLink);
            loadedModsType2.addAll(pageData.getData());
            if (pageData.hasNext()) {
                pageLink = pageData.getNextPageLink();
            }
        } while (pageData.hasNext());

        Collections.sort(modsType2, idComparator);
        Collections.sort(loadedModsType2, idComparator);

        Assert.assertEquals(modsType2, loadedModsType2);

        for (Mod mod : loadedModsType1) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4);
        pageData = modService.findModsByTenantIdAndCustomerIdAndType(tenantId, customerId, type1, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());

        for (Mod mod : loadedModsType2) {
            modService.deleteMod(mod.getId());
        }

        pageLink = new TextPageLink(4);
        pageData = modService.findModsByTenantIdAndCustomerIdAndType(tenantId, customerId, type2, pageLink);
        Assert.assertFalse(pageData.hasNext());
        Assert.assertEquals(0, pageData.getData().size());
        customerService.deleteCustomer(customerId);
    }

}
