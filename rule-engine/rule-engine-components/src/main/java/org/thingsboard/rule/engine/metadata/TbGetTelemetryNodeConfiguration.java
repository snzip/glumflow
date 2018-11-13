package org.thingsboard.rule.engine.metadata;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mshvayka on 04.09.18.
 */
@Data
public class TbGetTelemetryNodeConfiguration implements NodeConfiguration<TbGetTelemetryNodeConfiguration> {

    public static final String FETCH_MODE_FIRST = "FIRST";
    public static final String FETCH_MODE_LAST = "LAST";
    public static final String FETCH_MODE_ALL = "ALL";
    public static final int MAX_FETCH_SIZE = 1000;

    private int startInterval;
    private int endInterval;
    private String startIntervalTimeUnit;
    private String endIntervalTimeUnit;
    private String fetchMode; //FIRST, LAST, LATEST

    private List<String> latestTsKeyNames;



    @Override
    public TbGetTelemetryNodeConfiguration defaultConfiguration() {
        TbGetTelemetryNodeConfiguration configuration = new TbGetTelemetryNodeConfiguration();
        configuration.setLatestTsKeyNames(Collections.emptyList());
        configuration.setFetchMode("FIRST");
        configuration.setStartIntervalTimeUnit(TimeUnit.MINUTES.name());
        configuration.setStartInterval(2);
        configuration.setEndIntervalTimeUnit(TimeUnit.MINUTES.name());
        configuration.setEndInterval(1);
        return configuration;
    }
}
