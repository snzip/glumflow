package org.thingsboard.server.dao.rule;

import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.rule.RuleChain;
import org.thingsboard.server.dao.Dao;

import java.util.List;
import java.util.UUID;

/**
 * Created by igor on 3/12/18.
 */
public interface RuleChainDao extends Dao<RuleChain> {

    /**
     * Find rule chains by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of rule chain objects
     */
    List<RuleChain> findRuleChainsByTenantId(UUID tenantId, TextPageLink pageLink);

}
