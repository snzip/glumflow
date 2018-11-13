package org.thingsboard.server.service.cluster.routing;

import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.common.msg.cluster.ServerType;
import org.thingsboard.server.service.cluster.discovery.DiscoveryServiceListener;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
public interface ClusterRoutingService extends DiscoveryServiceListener {

    ServerAddress getCurrentServer();

    Optional<ServerAddress> resolveByUuid(UUID uuid);

    Optional<ServerAddress> resolveById(EntityId entityId);

    Optional<ServerAddress> resolveByUuid(ServerType server, UUID uuid);

    Optional<ServerAddress> resolveById(ServerType server, EntityId entityId);


}
