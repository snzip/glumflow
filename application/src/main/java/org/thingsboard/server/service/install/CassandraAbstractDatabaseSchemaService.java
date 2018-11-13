package org.thingsboard.server.service.install;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.dao.cassandra.CassandraInstallCluster;
import org.thingsboard.server.service.install.cql.CQLStatementsParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public abstract class CassandraAbstractDatabaseSchemaService implements DatabaseSchemaService {

    private static final String CASSANDRA_DIR = "cassandra";

    @Autowired
    private CassandraInstallCluster cluster;

    @Autowired
    private InstallScripts installScripts;

    private final String schemaCql;

    protected CassandraAbstractDatabaseSchemaService(String schemaCql) {
        this.schemaCql = schemaCql;
    }

    @Override
    public void createDatabaseSchema() throws Exception {
        log.info("Installing Cassandra DataBase schema part: " + schemaCql);
        Path schemaFile = Paths.get(installScripts.getDataDir(), CASSANDRA_DIR, schemaCql);
        loadCql(schemaFile);

    }

    private void loadCql(Path cql) throws Exception {
        List<String> statements = new CQLStatementsParser(cql).getStatements();
        statements.forEach(statement -> cluster.getSession().execute(statement));
    }
}
