package org.thingsboard.server.dao.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(prefix = "database.entities", value = "type", havingValue = "cassandra")
public @interface NoSqlDao {
}
