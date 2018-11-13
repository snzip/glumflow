package org.thingsboard.server.actors.service;

import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.common.msg.TbActorMsg;

@Slf4j
public abstract class ContextAwareActor extends UntypedActor {

    public static final int ENTITY_PACK_LIMIT = 1024;

    protected final ActorSystemContext systemContext;

    public ContextAwareActor(ActorSystemContext systemContext) {
        super();
        this.systemContext = systemContext;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Processing msg: {}", msg);
        }
        if (msg instanceof TbActorMsg) {
            try {
                if (!process((TbActorMsg) msg)) {
                    log.warn("Unknown message: {}!", msg);
                }
            } catch (Exception e) {
                throw e;
            }
        } else if (msg instanceof Terminated) {
            processTermination((Terminated) msg);
        } else {
            log.warn("Unknown message: {}!", msg);
        }
    }

    protected void processTermination(Terminated msg) {
    }

    protected abstract boolean process(TbActorMsg msg);
}
