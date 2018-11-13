package org.thingsboard.server.dao.nosql;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import lombok.Data;
import org.thingsboard.server.dao.util.AsyncTask;

/**
 * Created by ashvayka on 24.10.18.
 */
@Data
public class CassandraStatementTask implements AsyncTask {

    private final Session session;
    private final Statement statement;

}
