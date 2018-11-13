package org.thingsboard.server.common.msg.cluster;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Andrew Shvayka
 */
@Data
@EqualsAndHashCode
public class ServerAddress implements Comparable<ServerAddress>, Serializable {

    private final String host;
    private final int port;
    private final ServerType serverType;

    @Override
    public int compareTo(ServerAddress o) {
        int result = this.host.compareTo(o.host);
        if (result == 0) {
            result = this.port - o.port;
        }
        return result;
    }

    @Override
    public String toString() {
        return '[' + host + ':' + port + ']';
    }
}
