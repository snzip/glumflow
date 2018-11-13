package org.thingsboard.server.dao.dashboard;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.DashboardInfo;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.page.TimePageLink;
import org.thingsboard.server.dao.Dao;

import java.util.List;
import java.util.UUID;

/**
 * The Interface DashboardInfoDao.
 */
public interface DashboardInfoDao extends Dao<DashboardInfo> {

    /**
     * Find dashboards by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of dashboard objects
     */
    List<DashboardInfo> findDashboardsByTenantId(UUID tenantId, TextPageLink pageLink);

    /**
     * Find dashboards by tenantId, customerId and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param pageLink the page link
     * @return the list of dashboard objects
     */
    ListenableFuture<List<DashboardInfo>> findDashboardsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TimePageLink pageLink);

}
