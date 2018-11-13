package org.thingsboard.server.service.cluster.routing;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.service.cluster.discovery.ServerInstance;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by ashvayka on 23.09.18.
 */
@Slf4j
public class ConsistentHashCircle {
    private final ConcurrentNavigableMap<Long, ServerInstance> circle =
            new ConcurrentSkipListMap<>();

    public void put(long hash, ServerInstance instance) {
        circle.put(hash, instance);
    }

    public void remove(long hash) {
        circle.remove(hash);
    }

    public boolean isEmpty() {
        return circle.isEmpty();
    }

    public boolean containsKey(Long hash) {
        return circle.containsKey(hash);
    }

    public ConcurrentNavigableMap<Long, ServerInstance> tailMap(Long hash) {
        return circle.tailMap(hash);
    }

    public Long firstKey() {
        return circle.firstKey();
    }

    public ServerInstance get(Long hash) {
        return circle.get(hash);
    }

    public void log() {
        circle.entrySet().forEach((e) -> log.debug("{} -> {}", e.getKey(), e.getValue().getServerAddress()));
    }
}
