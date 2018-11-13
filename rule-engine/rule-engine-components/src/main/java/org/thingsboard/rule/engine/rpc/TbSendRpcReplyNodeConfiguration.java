package org.thingsboard.rule.engine.rpc;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;
import org.thingsboard.server.common.data.DataConstants;

@Data
public class TbSendRpcReplyNodeConfiguration implements NodeConfiguration<TbSendRpcReplyNodeConfiguration> {

    private String requestIdMetaDataAttribute;

    @Override
    public TbSendRpcReplyNodeConfiguration defaultConfiguration() {
        TbSendRpcReplyNodeConfiguration configuration = new TbSendRpcReplyNodeConfiguration();
        configuration.setRequestIdMetaDataAttribute("requestId");
        return configuration;
    }
}
