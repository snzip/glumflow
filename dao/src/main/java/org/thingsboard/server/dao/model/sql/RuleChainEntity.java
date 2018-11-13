package org.thingsboard.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.common.data.UUIDConverter;
import org.thingsboard.server.common.data.id.RuleChainId;
import org.thingsboard.server.common.data.id.RuleNodeId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.rule.RuleChain;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.model.BaseSqlEntity;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.model.SearchTextEntity;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.RULE_CHAIN_COLUMN_FAMILY_NAME)
public class RuleChainEntity extends BaseSqlEntity<RuleChain> implements SearchTextEntity<RuleChain> {

    @Column(name = ModelConstants.RULE_CHAIN_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.RULE_CHAIN_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Column(name = ModelConstants.RULE_CHAIN_FIRST_RULE_NODE_ID_PROPERTY)
    private String firstRuleNodeId;

    @Column(name = ModelConstants.RULE_CHAIN_ROOT_PROPERTY)
    private boolean root;

    @Column(name = ModelConstants.DEBUG_MODE)
    private boolean debugMode;

    @Type(type = "json")
    @Column(name = ModelConstants.RULE_CHAIN_CONFIGURATION_PROPERTY)
    private JsonNode configuration;

    @Type(type = "json")
    @Column(name = ModelConstants.ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    public RuleChainEntity() {
    }

    public RuleChainEntity(RuleChain ruleChain) {
        if (ruleChain.getId() != null) {
            this.setId(ruleChain.getUuidId());
        }
        this.tenantId = toString(DaoUtil.getId(ruleChain.getTenantId()));
        this.name = ruleChain.getName();
        this.searchText = ruleChain.getName();
        if (ruleChain.getFirstRuleNodeId() != null) {
            this.firstRuleNodeId = UUIDConverter.fromTimeUUID(ruleChain.getFirstRuleNodeId().getId());
        }
        this.root = ruleChain.isRoot();
        this.debugMode = ruleChain.isDebugMode();
        this.configuration = ruleChain.getConfiguration();
        this.additionalInfo = ruleChain.getAdditionalInfo();
    }

    @Override
    public String getSearchTextSource() {
        return searchText;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public RuleChain toData() {
        RuleChain ruleChain = new RuleChain(new RuleChainId(getId()));
        ruleChain.setCreatedTime(UUIDs.unixTimestamp(getId()));
        ruleChain.setTenantId(new TenantId(toUUID(tenantId)));
        ruleChain.setName(name);
        if (firstRuleNodeId != null) {
            ruleChain.setFirstRuleNodeId(new RuleNodeId(UUIDConverter.fromString(firstRuleNodeId)));
        }
        ruleChain.setRoot(root);
        ruleChain.setDebugMode(debugMode);
        ruleChain.setConfiguration(configuration);
        ruleChain.setAdditionalInfo(additionalInfo);
        return ruleChain;
    }
}
