package org.thingsboard.server.actors.app;

import akka.actor.ActorRef;
import akka.actor.LocalActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.ruleChain.RuleChainManagerActor;
import org.thingsboard.server.actors.service.ContextBasedCreator;
import org.thingsboard.server.actors.service.DefaultActorService;
import org.thingsboard.server.actors.shared.rulechain.SystemRuleChainManager;
import org.thingsboard.server.actors.tenant.TenantActor;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.PageDataIterable;
import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.aware.TenantAwareMsg;
import org.thingsboard.server.common.msg.cluster.SendToClusterMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.common.msg.plugin.ComponentLifecycleMsg;
import org.thingsboard.server.common.msg.system.ServiceToRuleEngineMsg;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.tenant.TenantService;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AppActor extends RuleChainManagerActor {

    private static final TenantId SYSTEM_TENANT = new TenantId(ModelConstants.NULL_UUID);
    private final TenantService tenantService;
    private final BiMap<TenantId, ActorRef> tenantActors;

    private AppActor(ActorSystemContext systemContext) {
        super(systemContext, new SystemRuleChainManager(systemContext));
        this.tenantService = systemContext.getTenantService();
        this.tenantActors = HashBiMap.create();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void preStart() {
        log.info("Starting main system actor.");
        try {
            initRuleChains();
            if (systemContext.isTenantComponentsInitEnabled()) {
                PageDataIterable<Tenant> tenantIterator = new PageDataIterable<>(tenantService::findTenants, ENTITY_PACK_LIMIT);
                for (Tenant tenant : tenantIterator) {
                    log.debug("[{}] Creating tenant actor", tenant.getId());
                    getOrCreateTenantActor(tenant.getId());
                    log.debug("Tenant actor created.");
                }
            }
            log.info("Main system actor started.");
        } catch (Exception e) {
            log.warn("Unknown failure", e);
        }
    }

    @Override
    protected boolean process(TbActorMsg msg) {
        switch (msg.getMsgType()) {
            case SEND_TO_CLUSTER_MSG:
                onPossibleClusterMsg((SendToClusterMsg) msg);
                break;
            case CLUSTER_EVENT_MSG:
                broadcast(msg);
                break;
            case COMPONENT_LIFE_CYCLE_MSG:
                onComponentLifecycleMsg((ComponentLifecycleMsg) msg);
                break;
            case SERVICE_TO_RULE_ENGINE_MSG:
                onServiceToRuleEngineMsg((ServiceToRuleEngineMsg) msg);
                break;
            case TRANSPORT_TO_DEVICE_ACTOR_MSG:
            case DEVICE_ATTRIBUTES_UPDATE_TO_DEVICE_ACTOR_MSG:
            case DEVICE_CREDENTIALS_UPDATE_TO_DEVICE_ACTOR_MSG:
            case DEVICE_NAME_OR_TYPE_UPDATE_TO_DEVICE_ACTOR_MSG:
            case DEVICE_RPC_REQUEST_TO_DEVICE_ACTOR_MSG:
            case SERVER_RPC_RESPONSE_TO_DEVICE_ACTOR_MSG:
            case REMOTE_TO_RULE_CHAIN_TELL_NEXT_MSG:
                onToDeviceActorMsg((TenantAwareMsg) msg);
                break;
            default:
                return false;
        }
        return true;
    }

    private void onPossibleClusterMsg(SendToClusterMsg msg) {
        Optional<ServerAddress> address = systemContext.getRoutingService().resolveById(msg.getEntityId());
        if (address.isPresent()) {
            systemContext.getRpcService().tell(
                    systemContext.getEncodingService().convertToProtoDataMessage(address.get(), msg.getMsg()));
        } else {
            self().tell(msg.getMsg(), ActorRef.noSender());
        }
    }

    private void onServiceToRuleEngineMsg(ServiceToRuleEngineMsg msg) {
        if (SYSTEM_TENANT.equals(msg.getTenantId())) {
            log.warn("[{}] Invalid service to rule engine msg called. System messages are not supported yet", SYSTEM_TENANT);
        } else {
            getOrCreateTenantActor(msg.getTenantId()).tell(msg, self());
        }
    }

    @Override
    protected void broadcast(Object msg) {
        super.broadcast(msg);
        tenantActors.values().forEach(actorRef -> actorRef.tell(msg, ActorRef.noSender()));
    }

    private void onComponentLifecycleMsg(ComponentLifecycleMsg msg) {
        ActorRef target;
        if (SYSTEM_TENANT.equals(msg.getTenantId())) {
            target = getEntityActorRef(msg.getEntityId());
        } else {
            target = getOrCreateTenantActor(msg.getTenantId());
        }
        if (target != null) {
            target.tell(msg, ActorRef.noSender());
        } else {
            log.debug("[{}] Invalid component lifecycle msg: {}", msg.getTenantId(), msg);
        }
    }

    private void onToDeviceActorMsg(TenantAwareMsg msg) {
        getOrCreateTenantActor(msg.getTenantId()).tell(msg, ActorRef.noSender());
    }

    private ActorRef getOrCreateTenantActor(TenantId tenantId) {
        return tenantActors.computeIfAbsent(tenantId, k -> {
            log.debug("[{}] Creating tenant actor.", tenantId);
            ActorRef tenantActor = context().actorOf(Props.create(new TenantActor.ActorCreator(systemContext, tenantId))
                    .withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME), tenantId.toString());
            context().watch(tenantActor);
            log.debug("[{}] Created tenant actor: {}.", tenantId, tenantActor);
            return tenantActor;
        });
    }

    @Override
    protected void processTermination(Terminated message) {
        ActorRef terminated = message.actor();
        if (terminated instanceof LocalActorRef) {
            boolean removed = tenantActors.inverse().remove(terminated) != null;
            if (removed) {
                log.debug("[{}] Removed actor:", terminated);
            } else {
                log.warn("[{}] Removed actor was not found in the tenant map!");
            }
        } else {
            throw new IllegalStateException("Remote actors are not supported!");
        }
    }

    public static class ActorCreator extends ContextBasedCreator<AppActor> {
        private static final long serialVersionUID = 1L;

        public ActorCreator(ActorSystemContext context) {
            super(context);
        }

        @Override
        public AppActor create() {
            return new AppActor(context);
        }
    }

    private final SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create("1 minute"), t -> {
        log.warn("Unknown failure", t);
        if (t instanceof RuntimeException) {
            return SupervisorStrategy.restart();
        } else {
            return SupervisorStrategy.stop();
        }
    });
}
