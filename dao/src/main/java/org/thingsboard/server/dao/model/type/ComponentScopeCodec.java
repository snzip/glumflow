package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.plugin.ComponentScope;

public class ComponentScopeCodec extends EnumNameCodec<ComponentScope> {

    public ComponentScopeCodec() {
        super(ComponentScope.class);
    }

}
