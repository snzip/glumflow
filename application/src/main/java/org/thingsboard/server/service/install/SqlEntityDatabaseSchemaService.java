package org.thingsboard.server.service.install;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.util.SqlDao;

@Service
@SqlDao
@Profile("install")
public class SqlEntityDatabaseSchemaService extends SqlAbstractDatabaseSchemaService
        implements EntityDatabaseSchemaService {
    public SqlEntityDatabaseSchemaService() {
        super("schema-entities.sql");
    }
}
