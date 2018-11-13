package org.thingsboard.rule.engine.action;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;
import org.thingsboard.server.common.data.alarm.AlarmSeverity;

@Data
public class TbCreateAlarmNodeConfiguration extends TbAbstractAlarmNodeConfiguration implements NodeConfiguration<TbCreateAlarmNodeConfiguration> {

    private AlarmSeverity severity;
    private boolean propagate;

    @Override
    public TbCreateAlarmNodeConfiguration defaultConfiguration() {
        TbCreateAlarmNodeConfiguration configuration = new TbCreateAlarmNodeConfiguration();
        configuration.setAlarmDetailsBuildJs("var details = {};\n" +
                "if (metadata.prevAlarmDetails) {\n" +
                "    details = JSON.parse(metadata.prevAlarmDetails);\n" +
                "}\n"+
                "return details;");
        configuration.setAlarmType("General Alarm");
        configuration.setSeverity(AlarmSeverity.CRITICAL);
        configuration.setPropagate(false);
        return configuration;
    }
}
