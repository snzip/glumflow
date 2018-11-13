package org.thingsboard.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "audit_log.logging_level")
public class AuditLogLevelProperties {

    private Map<String, String> mask = new HashMap<>();

    public AuditLogLevelProperties() {
        super();
    }

    public void setMask(Map<String, String> mask) {
        this.mask = mask;
    }

    public Map<String, String> getMask() {
        return this.mask;
    }
}
