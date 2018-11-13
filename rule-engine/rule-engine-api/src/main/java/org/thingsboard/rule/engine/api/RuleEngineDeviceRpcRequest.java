package org.thingsboard.rule.engine.api;

import lombok.Builder;
import lombok.Data;
import org.thingsboard.server.common.data.id.DeviceId;

import java.util.UUID;

/**
 * Created by ashvayka on 02.04.18.
 */
@Data
@Builder
public final class RuleEngineDeviceRpcRequest {

    private final DeviceId deviceId;
    private final int requestId;
    private final UUID requestUUID;
    private final String originHost;
    private final int originPort;
    private final boolean oneway;
    private final String method;
    private final String body;
    private final long expirationTime;
    private final boolean restApiCall;

}
