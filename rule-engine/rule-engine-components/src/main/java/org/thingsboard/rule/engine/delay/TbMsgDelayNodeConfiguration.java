package org.thingsboard.rule.engine.delay;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;
import org.thingsboard.server.common.data.EntityType;

@Data
public class TbMsgDelayNodeConfiguration implements NodeConfiguration<TbMsgDelayNodeConfiguration> {

    private int periodInSeconds;
    private int maxPendingMsgs;

    @Override
    public TbMsgDelayNodeConfiguration defaultConfiguration() {
        TbMsgDelayNodeConfiguration configuration = new TbMsgDelayNodeConfiguration();
        configuration.setPeriodInSeconds(60);
        configuration.setMaxPendingMsgs(1000);
        return configuration;
    }
}
