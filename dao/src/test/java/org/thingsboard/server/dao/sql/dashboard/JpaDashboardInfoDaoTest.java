package org.thingsboard.server.dao.sql.dashboard;

import com.datastax.driver.core.utils.UUIDs;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.common.data.DashboardInfo;
import org.thingsboard.server.common.data.id.DashboardId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.dao.AbstractJpaDaoTest;
import org.thingsboard.server.dao.dashboard.DashboardInfoDao;

import java.util.List;
import java.util.UUID;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
public class JpaDashboardInfoDaoTest extends AbstractJpaDaoTest {

    @Autowired
    private DashboardInfoDao dashboardInfoDao;

    @Test
    public void testFindDashboardsByTenantId() {
        UUID tenantId1 = UUIDs.timeBased();
        UUID tenantId2 = UUIDs.timeBased();

        for (int i = 0; i < 20; i++) {
            createDashboard(tenantId1, i);
            createDashboard(tenantId2, i * 2);
        }

        TextPageLink pageLink1 = new TextPageLink(15, "DASHBOARD");
        List<DashboardInfo> dashboardInfos1 = dashboardInfoDao.findDashboardsByTenantId(tenantId1, pageLink1);
        Assert.assertEquals(15, dashboardInfos1.size());

        TextPageLink pageLink2 = new TextPageLink(15, "DASHBOARD", dashboardInfos1.get(14).getId().getId(), null);
        List<DashboardInfo> dashboardInfos2 = dashboardInfoDao.findDashboardsByTenantId(tenantId1, pageLink2);
        Assert.assertEquals(5, dashboardInfos2.size());
    }

    private void createDashboard(UUID tenantId, int index) {
        DashboardInfo dashboardInfo = new DashboardInfo();
        dashboardInfo.setId(new DashboardId(UUIDs.timeBased()));
        dashboardInfo.setTenantId(new TenantId(tenantId));
        dashboardInfo.setTitle("DASHBOARD_" + index);
        dashboardInfoDao.save(dashboardInfo);
    }
}
