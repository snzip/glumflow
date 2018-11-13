package org.thingsboard.rule.engine.mail;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;

@Data
public class TbSendEmailNodeConfiguration implements NodeConfiguration {

    private boolean useSystemSmtpSettings;
    private String smtpHost;
    private int smtpPort;
    private String username;
    private String password;
    private String smtpProtocol;
    private int timeout;
    private boolean enableTls;

    @Override
    public TbSendEmailNodeConfiguration defaultConfiguration() {
        TbSendEmailNodeConfiguration configuration = new TbSendEmailNodeConfiguration();
        configuration.setUseSystemSmtpSettings(true);
        configuration.setSmtpHost("localhost");
        configuration.setSmtpProtocol("smtp");
        configuration.setSmtpPort(25);
        configuration.setTimeout(10000);
        configuration.setEnableTls(false);
        return configuration;
    }
}
