package org.thingsboard.rule.engine.debug;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;
import org.thingsboard.server.common.data.EntityType;

import java.util.Map;

@Data
public class TbMsgGeneratorNodeConfiguration implements NodeConfiguration<TbMsgGeneratorNodeConfiguration> {

    private int msgCount;
    private int periodInSeconds;
    private String originatorId;
    private EntityType originatorType;
    private String jsScript;

    @Override
    public TbMsgGeneratorNodeConfiguration defaultConfiguration() {
        TbMsgGeneratorNodeConfiguration configuration = new TbMsgGeneratorNodeConfiguration();
        configuration.setMsgCount(0);
        configuration.setPeriodInSeconds(1);
        configuration.setJsScript("var msg = { temp: 42, humidity: 77 };\n" +
                "var metadata = { data: 40 };\n" +
                "var msgType = \"POST_TELEMETRY_REQUEST\";\n\n" +
                "return { msg: msg, metadata: metadata, msgType: msgType };");
        return configuration;
    }
}
