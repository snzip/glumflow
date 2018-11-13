package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import org.thingsboard.server.common.data.plugin.ComponentType;

public class ComponentTypeCodec extends EnumNameCodec<ComponentType> {

    public ComponentTypeCodec() {
        super(ComponentType.class);
    }

}
