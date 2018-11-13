package org.thingsboard.server.rules;

import org.junit.ClassRule;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;
import org.thingsboard.server.dao.CustomSqlUnit;

import java.util.Arrays;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({
        "org.thingsboard.server.rules.flow.sql.*Test",
        "org.thingsboard.server.rules.lifecycle.sql.*Test"})
public class RuleEngineSqlTestSuite {

    @ClassRule
    public static CustomSqlUnit sqlUnit = new CustomSqlUnit(
            Arrays.asList("sql/schema-ts.sql", "sql/schema-entities.sql", "sql/system-data.sql"),
            "sql/drop-all-tables.sql",
            "sql-test.properties");
}
