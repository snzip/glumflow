package org.thingsboard.server.service.install;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.util.NoSqlDao;

@Service
@NoSqlDao
@Profile("install")
public class CassandraEntityDatabaseSchemaService extends CassandraAbstractDatabaseSchemaService
        implements EntityDatabaseSchemaService {
    public CassandraEntityDatabaseSchemaService() {
        super("schema-entities.cql");
    }
}
