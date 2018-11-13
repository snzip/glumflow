package org.thingsboard.rule.engine.filter;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;
import org.thingsboard.server.common.data.relation.EntitySearchDirection;
import org.thingsboard.server.common.msg.session.SessionMsgType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ashvayka on 19.01.18.
 */
@Data
public class TbCheckRelationNodeConfiguration implements NodeConfiguration<TbCheckRelationNodeConfiguration> {

    private String direction;
    private String entityId;
    private String entityType;
    private String relationType;

    @Override
    public TbCheckRelationNodeConfiguration defaultConfiguration() {
        TbCheckRelationNodeConfiguration configuration = new TbCheckRelationNodeConfiguration();
        configuration.setDirection(EntitySearchDirection.FROM.name());
        configuration.setRelationType("Contains");
        return configuration;
    }
}
