package org.thingsboard.server.dao;

import org.junit.ClassRule;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({
        "org.thingsboard.server.dao.service.*ServiceSqlTest"
})
public class SqlDaoServiceTestSuite {

    @ClassRule
    public static CustomSqlUnit sqlUnit = new CustomSqlUnit(
            Arrays.asList("sql/schema-ts.sql", "sql/schema-entities.sql", "sql/system-data.sql", "sql/system-test.sql"),
            "sql/drop-all-tables.sql",
            "sql-test.properties"
    );

}
