package org.thingsboard.server.actors.shared;

import akka.actor.ActorContext;
import akka.event.LoggingAdapter;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.stats.StatsPersistTick;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.plugin.ComponentLifecycleState;
import org.thingsboard.server.common.msg.TbMsg;
import org.thingsboard.server.common.msg.cluster.ClusterEventMsg;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Slf4j
public abstract class ComponentMsgProcessor<T extends EntityId> extends AbstractContextAwareMsgProcessor {

    protected final TenantId tenantId;
    protected final T entityId;
    protected ComponentLifecycleState state;

    protected ComponentMsgProcessor(ActorSystemContext systemContext, TenantId tenantId, T id) {
        super(systemContext);
        this.tenantId = tenantId;
        this.entityId = id;
    }

    public abstract void start(ActorContext context) throws Exception;

    public abstract void stop(ActorContext context) throws Exception;

    public abstract void onClusterEventMsg(ClusterEventMsg msg) throws Exception;

    public void onCreated(ActorContext context) throws Exception {
        start(context);
    }

    public void onUpdate(ActorContext context) throws Exception {
        restart(context);
    }

    public void onActivate(ActorContext context) throws Exception {
        restart(context);
    }

    public void onSuspend(ActorContext context) throws Exception {
        stop(context);
    }

    public void onStop(ActorContext context) throws Exception {
        stop(context);
    }

    private void restart(ActorContext context) throws Exception {
        stop(context);
        start(context);
    }

    public void scheduleStatsPersistTick(ActorContext context, long statsPersistFrequency) {
        schedulePeriodicMsgWithDelay(context, new StatsPersistTick(), statsPersistFrequency, statsPersistFrequency);
    }

    protected void checkActive() {
        if (state != ComponentLifecycleState.ACTIVE) {
            log.warn("Rule chain is not active. Current state [{}] for processor [{}] tenant [{}]", state, tenantId, entityId);
            throw new IllegalStateException("Rule chain is not active! " + entityId + " - " + tenantId);
        }
    }

}
