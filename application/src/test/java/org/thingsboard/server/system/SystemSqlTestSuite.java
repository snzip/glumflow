package org.thingsboard.server.system;

import org.junit.ClassRule;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;
import org.thingsboard.server.dao.CustomSqlUnit;

import java.util.Arrays;

/**
 * Created by Valerii Sosliuk on 6/27/2017.
 */
@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({"org.thingsboard.server.system.sql.*SqlTest"})
public class SystemSqlTestSuite {

    @ClassRule
    public static CustomSqlUnit sqlUnit = new CustomSqlUnit(
            Arrays.asList("sql/schema-ts.sql", "sql/schema-entities.sql", "sql/system-data.sql"),
            "sql/drop-all-tables.sql",
            "sql-test.properties");

}
