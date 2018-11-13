package org.thingsboard.server.service.install;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
public abstract class SqlAbstractDatabaseSchemaService implements DatabaseSchemaService {

    private static final String SQL_DIR = "sql";

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Autowired
    private InstallScripts installScripts;

    private final String schemaSql;

    protected SqlAbstractDatabaseSchemaService(String schemaSql) {
        this.schemaSql = schemaSql;
    }

    @Override
    public void createDatabaseSchema() throws Exception {

        log.info("Installing SQL DataBase schema part: " + schemaSql);

        Path schemaFile = Paths.get(installScripts.getDataDir(), SQL_DIR, schemaSql);
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            String sql = new String(Files.readAllBytes(schemaFile), Charset.forName("UTF-8"));
            conn.createStatement().execute(sql); //NOSONAR, ignoring because method used to load initial thingsboard database schema
        }

    }

}
