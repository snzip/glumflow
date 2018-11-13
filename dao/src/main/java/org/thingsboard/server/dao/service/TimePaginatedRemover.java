package org.thingsboard.server.dao.service;

import org.thingsboard.server.common.data.id.IdBased;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.util.List;
import java.util.UUID;

public abstract class TimePaginatedRemover<I, D extends IdBased<?>> {

    private static final int DEFAULT_LIMIT = 100;

    public void removeEntities(I id) {
        TimePageLink pageLink = new TimePageLink(DEFAULT_LIMIT);
        boolean hasNext = true;
        while (hasNext) {
            List<D> entities = findEntities(id, pageLink);
            for (D entity : entities) {
                removeEntity(entity);
            }
            hasNext = entities.size() == pageLink.getLimit();
            if (hasNext) {
                int index = entities.size() - 1;
                UUID idOffset = entities.get(index).getUuidId();
                pageLink.setIdOffset(idOffset);
            }
        }
    }

    protected abstract List<D> findEntities(I id, TimePageLink pageLink);

    protected abstract void removeEntity(D entity);

}
