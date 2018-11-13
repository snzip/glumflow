package org.thingsboard.server.actors.shared;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Scheduler;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractContextAwareMsgProcessor {

    protected final ActorSystemContext systemContext;
    protected final ObjectMapper mapper = new ObjectMapper();

    protected AbstractContextAwareMsgProcessor(ActorSystemContext systemContext) {
        super();
        this.systemContext = systemContext;
    }

    private Scheduler getScheduler() {
        return systemContext.getScheduler();
    }

    private ExecutionContextExecutor getSystemDispatcher() {
        return systemContext.getActorSystem().dispatcher();
    }

    protected void schedulePeriodicMsgWithDelay(ActorContext ctx, Object msg, long delayInMs, long periodInMs) {
        schedulePeriodicMsgWithDelay(msg, delayInMs, periodInMs, ctx.self());
    }

    private void schedulePeriodicMsgWithDelay(Object msg, long delayInMs, long periodInMs, ActorRef target) {
        log.debug("Scheduling periodic msg {} every {} ms with delay {} ms", msg, periodInMs, delayInMs);
        getScheduler().schedule(Duration.create(delayInMs, TimeUnit.MILLISECONDS), Duration.create(periodInMs, TimeUnit.MILLISECONDS), target, msg, getSystemDispatcher(), null);
    }

    protected void scheduleMsgWithDelay(ActorContext ctx, Object msg, long delayInMs) {
        scheduleMsgWithDelay(msg, delayInMs, ctx.self());
    }

    private void scheduleMsgWithDelay(Object msg, long delayInMs, ActorRef target) {
        log.debug("Scheduling msg {} with delay {} ms", msg, delayInMs);
        getScheduler().scheduleOnce(Duration.create(delayInMs, TimeUnit.MILLISECONDS), target, msg, getSystemDispatcher(), null);
    }


}
