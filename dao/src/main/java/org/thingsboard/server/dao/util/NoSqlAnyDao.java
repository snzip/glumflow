package org.thingsboard.server.dao.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

@ConditionalOnExpression("'${database.ts.type}'=='cassandra' || '${database.entities.type}'=='cassandra'")
public @interface NoSqlAnyDao {
}
