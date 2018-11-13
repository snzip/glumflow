package org.thingsboard.server.dao.component;

import com.fasterxml.jackson.databind.JsonNode;
import org.thingsboard.server.common.data.id.ComponentDescriptorId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.plugin.ComponentDescriptor;
import org.thingsboard.server.common.data.plugin.ComponentScope;
import org.thingsboard.server.common.data.plugin.ComponentType;

/**
 * @author Andrew Shvayka
 */
public interface ComponentDescriptorService {

    ComponentDescriptor saveComponent(ComponentDescriptor component);

    ComponentDescriptor findById(ComponentDescriptorId componentId);

    ComponentDescriptor findByClazz(String clazz);

    TextPageData<ComponentDescriptor> findByTypeAndPageLink(ComponentType type, TextPageLink pageLink);

    TextPageData<ComponentDescriptor> findByScopeAndTypeAndPageLink(ComponentScope scope, ComponentType type, TextPageLink pageLink);

    boolean validate(ComponentDescriptor component, JsonNode configuration);

    void deleteByClazz(String clazz);

}
