package org.thingsboard.server.dao.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.rule.RuleNode;
import org.thingsboard.server.dao.model.nosql.RuleNodeEntity;
import org.thingsboard.server.dao.nosql.CassandraAbstractSearchTextDao;
import org.thingsboard.server.dao.util.NoSqlDao;

import static org.thingsboard.server.dao.model.ModelConstants.RULE_NODE_COLUMN_FAMILY_NAME;

@Component
@Slf4j
@NoSqlDao
public class CassandraRuleNodeDao extends CassandraAbstractSearchTextDao<RuleNodeEntity, RuleNode> implements RuleNodeDao {

    @Override
    protected Class<RuleNodeEntity> getColumnFamilyClass() {
        return RuleNodeEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return RULE_NODE_COLUMN_FAMILY_NAME;
    }

}
