package org.thingsboard.server.service.cluster.discovery;

import java.util.List;

/**
 * @author Andrew Shvayka
 */
public interface DiscoveryService {

    void publishCurrentServer();

    void unpublishCurrentServer();

    ServerInstance getCurrentServer();

    List<ServerInstance> getOtherServers();

}
