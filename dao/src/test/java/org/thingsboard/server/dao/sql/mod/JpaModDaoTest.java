package org.thingsboard.server.dao.sql.mod;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.AbstractJpaDaoTest;
import org.thingsboard.server.dao.mod.ModDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
public class JpaModDaoTest extends AbstractJpaDaoTest {

    @Autowired
    private ModDao modDao;

    @Test
    public void testFindModsByTenantId() {
        UUID tenantId1 = UUIDs.timeBased();
        UUID tenantId2 = UUIDs.timeBased();
        UUID customerId1 = UUIDs.timeBased();
        UUID customerId2 = UUIDs.timeBased();
        createMods(tenantId1, tenantId2, customerId1, customerId2, 40);

        TextPageLink pageLink1 = new TextPageLink(15, "SEARCH_TEXT");
        List<Mod> mods1 = modDao.findModsByTenantId(tenantId1, pageLink1);
        assertEquals(15, mods1.size());

        TextPageLink pageLink2 = new TextPageLink(15, "SEARCH_TEXT",  mods1.get(14).getId().getId(), null);
        List<Mod> mods2 = modDao.findModsByTenantId(tenantId1, pageLink2);
        assertEquals(5, mods2.size());
    }

    @Test
    public void testFindAsync() throws ExecutionException, InterruptedException {
        UUID tenantId = UUIDs.timeBased();
        UUID customerId = UUIDs.timeBased();
        Mod mod = getMod(tenantId, customerId);
        modDao.save(mod);

        UUID uuid = mod.getId().getId();
        Mod entity = modDao.findById(uuid);
        assertNotNull(entity);
        assertEquals(uuid, entity.getId().getId());

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Mod> future = service.submit(() -> modDao.findById(uuid));
        Mod asyncMod = future.get();
        assertNotNull("Async mod expected to be not null", asyncMod);
    }

    @Test
    public void testFindModsByTenantIdAndIdsAsync() throws ExecutionException, InterruptedException {
        UUID tenantId1 = UUIDs.timeBased();
        UUID customerId1 = UUIDs.timeBased();
        UUID tenantId2 = UUIDs.timeBased();
        UUID customerId2 = UUIDs.timeBased();

        List<UUID> modIds = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            UUID modId1 = UUIDs.timeBased();
            UUID modId2 = UUIDs.timeBased();
            modDao.save(getMod(tenantId1, customerId1, modId1));
            modDao.save(getMod(tenantId2, customerId2, modId2));
            modIds.add(modId1);
            modIds.add(modId2);
        }

        ListenableFuture<List<Mod>> modsFuture = modDao.findModsByTenantIdAndIdsAsync(tenantId1, modIds);
        List<Mod> mods = modsFuture.get();
        assertEquals(5, mods.size());
    }

    @Test
    public void testFindModsByTenantIdAndCustomerIdAndIdsAsync() throws ExecutionException, InterruptedException {
        UUID tenantId1 = UUIDs.timeBased();
        UUID customerId1 = UUIDs.timeBased();
        UUID tenantId2 = UUIDs.timeBased();
        UUID customerId2 = UUIDs.timeBased();

        List<UUID> modIds = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            UUID modId1 = UUIDs.timeBased();
            UUID modId2 = UUIDs.timeBased();
            modDao.save(getMod(tenantId1, customerId1, modId1));
            modDao.save(getMod(tenantId2, customerId2, modId2));
            modIds.add(modId1);
            modIds.add(modId2);
        }

        ListenableFuture<List<Mod>> modsFuture = modDao.findModsByTenantIdCustomerIdAndIdsAsync(tenantId1, customerId1, modIds);
        List<Mod> mods = modsFuture.get();
        assertEquals(20, mods.size());
    }

    private void createMods(UUID tenantId1, UUID tenantId2, UUID customerId1, UUID customerId2, int count) {
        for (int i = 0; i < count / 2; i++) {
            modDao.save(getMod(tenantId1, customerId1));
            modDao.save(getMod(tenantId2, customerId2));
        }
    }

    private Mod getMod(UUID tenantId, UUID customerID) {
        return getMod(tenantId, customerID, UUIDs.timeBased());
    }

    private Mod getMod(UUID tenantId, UUID customerID, UUID modId) {
        Mod mod = new Mod();
        mod.setId(new ModId(modId));
        mod.setTenantId(new TenantId(tenantId));
        mod.setCustomerId(new CustomerId(customerID));
        mod.setName("SEARCH_TEXT");
        return mod;
    }
}
