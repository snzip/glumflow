package org.thingsboard.server.service.cluster.discovery;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.thingsboard.server.common.msg.cluster.ServerAddress;

/**
 * @author Andrew Shvayka
 */
@ToString
@EqualsAndHashCode(exclude = {"serverInfo", "serverAddress"})
public final class ServerInstance implements Comparable<ServerInstance> {

    @Getter
    private final String host;
    @Getter
    private final int port;
    @Getter
    private final ServerAddress serverAddress;

    public ServerInstance(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
        this.host = serverAddress.getHost();
        this.port = serverAddress.getPort();
    }

    @Override
    public int compareTo(ServerInstance o) {
        return this.serverAddress.compareTo(o.serverAddress);
    }
}
