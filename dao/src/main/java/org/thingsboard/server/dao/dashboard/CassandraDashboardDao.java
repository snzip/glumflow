package org.thingsboard.server.dao.dashboard;

import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.Dashboard;
import org.thingsboard.server.dao.model.nosql.DashboardEntity;
import org.thingsboard.server.dao.nosql.CassandraAbstractSearchTextDao;
import org.thingsboard.server.dao.util.NoSqlDao;

import static org.thingsboard.server.dao.model.ModelConstants.DASHBOARD_COLUMN_FAMILY_NAME;

@Component
@NoSqlDao
public class CassandraDashboardDao extends CassandraAbstractSearchTextDao<DashboardEntity, Dashboard> implements DashboardDao {

    @Override
    protected Class<DashboardEntity> getColumnFamilyClass() {
        return DashboardEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return DASHBOARD_COLUMN_FAMILY_NAME;
    }
}
