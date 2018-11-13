package org.thingsboard.server.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.BaseCassandraUnit;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

import java.util.List;

public class CustomCassandraCQLUnit extends BaseCassandraUnit {
    protected List<CQLDataSet> dataSets;

    public Session session;
    public Cluster cluster;

    public CustomCassandraCQLUnit(List<CQLDataSet> dataSets) {
        this.dataSets = dataSets;
    }

    public CustomCassandraCQLUnit(List<CQLDataSet> dataSets, int readTimeoutMillis) {
        this.dataSets = dataSets;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public CustomCassandraCQLUnit(List<CQLDataSet> dataSets, String configurationFileName) {
        this(dataSets);
        this.configurationFileName = configurationFileName;
    }

    public CustomCassandraCQLUnit(List<CQLDataSet> dataSets, String configurationFileName, int readTimeoutMillis) {
        this(dataSets);
        this.configurationFileName = configurationFileName;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public CustomCassandraCQLUnit(List<CQLDataSet> dataSets, String configurationFileName, long startUpTimeoutMillis) {
        super(startUpTimeoutMillis);
        this.dataSets = dataSets;
        this.configurationFileName = configurationFileName;
    }

    public CustomCassandraCQLUnit(List<CQLDataSet> dataSets, String configurationFileName, long startUpTimeoutMillis, int readTimeoutMillis) {
        super(startUpTimeoutMillis);
        this.dataSets = dataSets;
        this.configurationFileName = configurationFileName;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    @Override
    protected void load() {
        String hostIp = EmbeddedCassandraServerHelper.getHost();
        int port = EmbeddedCassandraServerHelper.getNativeTransportPort();
        cluster = new Cluster.Builder().addContactPoints(hostIp).withPort(port).withSocketOptions(getSocketOptions())
                .build();
        session = cluster.connect();
        CQLDataLoader dataLoader = new CQLDataLoader(session);
        dataSets.forEach(dataLoader::load);
        session = dataLoader.getSession();
    }

    @Override
    protected void after() {
        super.after();
        try (Cluster c = cluster; Session s = session) {
            session = null;
            cluster = null;
        }
    }

    // Getters for those who do not like to directly access fields

    public Session getSession() {
        return session;
    }

    public Cluster getCluster() {
        return cluster;
    }
}
