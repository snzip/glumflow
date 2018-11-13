package org.thingsboard.server.common.transport.service;

import org.thingsboard.server.common.data.EntityType;

/**
 * Created by ashvayka on 22.10.18.
 */
public class TbRateLimitsException extends Exception {
    private final EntityType entityType;

    TbRateLimitsException(EntityType entityType) {
        this.entityType = entityType;
    }
}
