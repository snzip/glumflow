package org.thingsboard.server.common.msg.aware;

import org.thingsboard.server.common.data.id.RuleChainId;

public interface RuleChainAwareMsg {

	RuleChainId getRuleChainId();
	
}
