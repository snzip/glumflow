package org.thingsboard.server.service.transport;

import org.thingsboard.server.gen.transport.TransportProtos.DeviceActorToTransportMsg;

import java.util.function.Consumer;

/**
 * Created by ashvayka on 05.10.18.
 */
public interface RuleEngineTransportService {

    void process(String nodeId, DeviceActorToTransportMsg msg);

    void process(String nodeId, DeviceActorToTransportMsg msg, Runnable onSuccess, Consumer<Throwable> onFailure);

}
