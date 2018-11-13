package org.thingsboard.server.service.install;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.util.SqlTsDao;

@Service
@SqlTsDao
@Profile("install")
public class SqlTsDatabaseSchemaService extends SqlAbstractDatabaseSchemaService
        implements TsDatabaseSchemaService {
    public SqlTsDatabaseSchemaService() {
        super("schema-ts.sql");
    }
}