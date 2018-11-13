package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.relation.RelationTypeGroup;

public class RelationTypeGroupCodec extends EnumNameCodec<RelationTypeGroup> {

    public RelationTypeGroupCodec() {
        super(RelationTypeGroup.class);
    }

}
