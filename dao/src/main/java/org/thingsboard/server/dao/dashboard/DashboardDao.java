package org.thingsboard.server.dao.dashboard;

import org.thingsboard.server.common.data.Dashboard;
import org.thingsboard.server.dao.Dao;

/**
 * The Interface DashboardDao.
 */
public interface DashboardDao extends Dao<Dashboard> {

    /**
     * Save or update dashboard object
     *
     * @param dashboard the dashboard object
     * @return saved dashboard object
     */
    Dashboard save(Dashboard dashboard);

}
