package org.thingsboard.server.actors.service;

import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.shared.ComponentMsgProcessor;
import org.thingsboard.server.actors.stats.StatsPersistMsg;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.plugin.ComponentLifecycleEvent;
import org.thingsboard.server.common.msg.cluster.ClusterEventMsg;
import org.thingsboard.server.common.msg.plugin.ComponentLifecycleMsg;

/**
 * @author Andrew Shvayka
 */
@Slf4j
public abstract class ComponentActor<T extends EntityId, P extends ComponentMsgProcessor<T>> extends ContextAwareActor {

    private long lastPersistedErrorTs = 0L;
    protected final TenantId tenantId;
    protected final T id;
    protected P processor;
    private long messagesProcessed;
    private long errorsOccurred;

    public ComponentActor(ActorSystemContext systemContext, TenantId tenantId, T id) {
        super(systemContext);
        this.tenantId = tenantId;
        this.id = id;
    }

    protected void setProcessor(P processor) {
        this.processor = processor;
    }

    @Override
    public void preStart() {
        try {
            processor.start(context());
            logLifecycleEvent(ComponentLifecycleEvent.STARTED);
            if (systemContext.isStatisticsEnabled()) {
                scheduleStatsPersistTick();
            }
        } catch (Exception e) {
            log.warn("[{}][{}] Failed to start {} processor: {}", tenantId, id, id.getEntityType(), e);
            logAndPersist("OnStart", e, true);
            logLifecycleEvent(ComponentLifecycleEvent.STARTED, e);
        }
    }

    private void scheduleStatsPersistTick() {
        try {
            processor.scheduleStatsPersistTick(context(), systemContext.getStatisticsPersistFrequency());
        } catch (Exception e) {
            log.error("[{}][{}] Failed to schedule statistics store message. No statistics is going to be stored: {}", tenantId, id, e.getMessage());
            logAndPersist("onScheduleStatsPersistMsg", e);
        }
    }

    @Override
    public void postStop() {
        try {
            processor.stop(context());
            logLifecycleEvent(ComponentLifecycleEvent.STOPPED);
        } catch (Exception e) {
            log.warn("[{}][{}] Failed to stop {} processor: {}", tenantId, id, id.getEntityType(), e.getMessage());
            logAndPersist("OnStop", e, true);
            logLifecycleEvent(ComponentLifecycleEvent.STOPPED, e);
        }
    }

    protected void onComponentLifecycleMsg(ComponentLifecycleMsg msg) {
        try {
            switch (msg.getEvent()) {
                case CREATED:
                    processor.onCreated(context());
                    break;
                case UPDATED:
                    processor.onUpdate(context());
                    break;
                case ACTIVATED:
                    processor.onActivate(context());
                    break;
                case SUSPENDED:
                    processor.onSuspend(context());
                    break;
                case DELETED:
                    processor.onStop(context());
                    break;
                default:
                    break;
            }
            logLifecycleEvent(msg.getEvent());
        } catch (Exception e) {
            logAndPersist("onLifecycleMsg", e, true);
            logLifecycleEvent(msg.getEvent(), e);
        }
    }

    protected void onClusterEventMsg(ClusterEventMsg msg) {
        try {
            processor.onClusterEventMsg(msg);
        } catch (Exception e) {
            logAndPersist("onClusterEventMsg", e);
        }
    }

    protected void onStatsPersistTick(EntityId entityId) {
        try {
            systemContext.getStatsActor().tell(new StatsPersistMsg(messagesProcessed, errorsOccurred, tenantId, entityId), ActorRef.noSender());
            resetStatsCounters();
        } catch (Exception e) {
            logAndPersist("onStatsPersistTick", e);
        }
    }

    private void resetStatsCounters() {
        messagesProcessed = 0;
        errorsOccurred = 0;
    }

    protected void increaseMessagesProcessedCount() {
        messagesProcessed++;
    }

    protected void logAndPersist(String method, Exception e) {
        logAndPersist(method, e, false);
    }

    private void logAndPersist(String method, Exception e, boolean critical) {
        errorsOccurred++;
        if (critical) {
            log.warn("[{}][{}] Failed to process {} msg: {}", id, tenantId, method, e);
        } else {
            log.debug("[{}][{}] Failed to process {} msg: {}", id, tenantId, method, e);
        }
        long ts = System.currentTimeMillis();
        if (ts - lastPersistedErrorTs > getErrorPersistFrequency()) {
            systemContext.persistError(tenantId, id, method, e);
            lastPersistedErrorTs = ts;
        }
    }

    private void logLifecycleEvent(ComponentLifecycleEvent event) {
        logLifecycleEvent(event, null);
    }

    private void logLifecycleEvent(ComponentLifecycleEvent event, Exception e) {
        systemContext.persistLifecycleEvent(tenantId, id, event, e);
    }

    protected abstract long getErrorPersistFrequency();
}
