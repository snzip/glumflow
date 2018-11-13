package org.thingsboard.server.dao.settings;

import com.datastax.driver.core.querybuilder.Select.Where;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.AdminSettings;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.model.nosql.AdminSettingsEntity;
import org.thingsboard.server.dao.nosql.CassandraAbstractModelDao;
import org.thingsboard.server.dao.util.NoSqlDao;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.thingsboard.server.dao.model.ModelConstants.ADMIN_SETTINGS_BY_KEY_COLUMN_FAMILY_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.ADMIN_SETTINGS_COLUMN_FAMILY_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.ADMIN_SETTINGS_KEY_PROPERTY;

@Component
@Slf4j
@NoSqlDao
public class CassandraAdminSettingsDao extends CassandraAbstractModelDao<AdminSettingsEntity, AdminSettings> implements AdminSettingsDao {

    @Override
    protected Class<AdminSettingsEntity> getColumnFamilyClass() {
        return AdminSettingsEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return ADMIN_SETTINGS_COLUMN_FAMILY_NAME;
    }

    @Override
    public AdminSettings findByKey(String key) {
        log.debug("Try to find admin settings by key [{}] ", key);
        Where query = select().from(ADMIN_SETTINGS_BY_KEY_COLUMN_FAMILY_NAME).where(eq(ADMIN_SETTINGS_KEY_PROPERTY, key));
        log.trace("Execute query {}", query);
        AdminSettingsEntity adminSettingsEntity = findOneByStatement(query);
        log.trace("Found admin settings [{}] by key [{}]", adminSettingsEntity, key);
        return DaoUtil.getData(adminSettingsEntity);
    }

}
