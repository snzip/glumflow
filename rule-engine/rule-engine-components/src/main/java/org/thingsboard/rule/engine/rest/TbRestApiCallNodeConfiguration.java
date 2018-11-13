package org.thingsboard.rule.engine.rest;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;

import java.util.Collections;
import java.util.Map;

@Data
public class TbRestApiCallNodeConfiguration implements NodeConfiguration<TbRestApiCallNodeConfiguration> {

    private String restEndpointUrlPattern;
    private String requestMethod;
    private Map<String, String> headers;

    @Override
    public TbRestApiCallNodeConfiguration defaultConfiguration() {
        TbRestApiCallNodeConfiguration configuration = new TbRestApiCallNodeConfiguration();
        configuration.setRestEndpointUrlPattern("http://localhost/api");
        configuration.setRequestMethod("POST");
        configuration.setHeaders(Collections.emptyMap());
        return configuration;
    }
}
