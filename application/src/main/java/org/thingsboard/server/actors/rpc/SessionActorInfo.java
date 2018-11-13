package org.thingsboard.server.actors.rpc;

import akka.actor.ActorRef;
import lombok.Data;

import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
@Data
public final class SessionActorInfo {
    protected final UUID sessionId;
    protected final ActorRef actor;
}
