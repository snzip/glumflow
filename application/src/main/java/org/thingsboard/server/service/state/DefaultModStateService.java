package org.thingsboard.server.service.state;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thingsboard.rule.engine.api.RpcError;
import org.thingsboard.server.actors.service.ActorService;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.plugin.ComponentLifecycleState;
import org.thingsboard.server.common.msg.TbMsg;
import org.thingsboard.server.common.msg.TbMsgDataType;
import org.thingsboard.server.common.msg.TbMsgMetaData;
import org.thingsboard.server.common.msg.cluster.SendToClusterMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.common.msg.system.ServiceToRuleEngineMsg;
import org.thingsboard.server.dao.attributes.AttributesService;
import org.thingsboard.server.dao.mod.ModService;
import org.thingsboard.server.dao.tenant.TenantService;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;
import org.thingsboard.server.service.cluster.routing.ClusterRoutingService;
import org.thingsboard.server.service.cluster.rpc.ClusterRpcService;
import org.thingsboard.server.service.telemetry.TelemetrySubscriptionService;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.thingsboard.server.common.data.DataConstants.ACTIVITY_EVENT;
import static org.thingsboard.server.common.data.DataConstants.CONNECT_EVENT;
import static org.thingsboard.server.common.data.DataConstants.DISCONNECT_EVENT;
import static org.thingsboard.server.common.data.DataConstants.INACTIVITY_EVENT;

/**
 * Created by ashvayka on 01.05.18.
 */
@Service
@Slf4j
//TODO: refactor to use page links as cursor and not fetch all
public class DefaultModStateService implements ModStateService {

    private static final ObjectMapper json = new ObjectMapper();
    public static final String ACTIVITY_STATE = "active";
    public static final String LAST_CONNECT_TIME = "lastConnectTime";
    public static final String LAST_DISCONNECT_TIME = "lastDisconnectTime";
    public static final String LAST_ACTIVITY_TIME = "lastActivityTime";
    public static final String INACTIVITY_ALARM_TIME = "inactivityAlarmTime";
    public static final String INACTIVITY_TIMEOUT = "inactivityTimeout";

    public static final List<String> PERSISTENT_ATTRIBUTES = Arrays.asList(ACTIVITY_STATE, LAST_CONNECT_TIME, LAST_DISCONNECT_TIME, LAST_ACTIVITY_TIME, INACTIVITY_ALARM_TIME, INACTIVITY_TIMEOUT);

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ModService modService;

    @Autowired
    private AttributesService attributesService;

    @Autowired
    @Lazy
    private ActorService actorService;

    @Autowired
    private TelemetrySubscriptionService tsSubService;

    @Autowired
    private ClusterRoutingService routingService;

    @Autowired
    private ClusterRpcService clusterRpcService;

    @Value("${state.defaultInactivityTimeoutInSec}")
    @Getter
    private long defaultInactivityTimeoutInSec;

    @Value("${state.defaultStateCheckIntervalInSec}")
    @Getter
    private long defaultStateCheckIntervalInSec;

// TODO in v2.1
//    @Value("${state.defaultStatePersistenceIntervalInSec}")
//    @Getter
//    private long defaultStatePersistenceIntervalInSec;
//
//    @Value("${state.defaultStatePersistencePack}")
//    @Getter
//    private long defaultStatePersistencePack;

    private ListeningScheduledExecutorService queueExecutor;

    private ConcurrentMap<TenantId, Set<ModId>> tenantMods = new ConcurrentHashMap<>();
    private ConcurrentMap<ModId, ModStateData> modStates = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Should be always single threaded due to absence of locks.
        queueExecutor = MoreExecutors.listeningDecorator(Executors.newSingleThreadScheduledExecutor());
        queueExecutor.submit(this::initStateFromDB);
        queueExecutor.scheduleAtFixedRate(this::updateState, defaultStateCheckIntervalInSec, defaultStateCheckIntervalInSec, TimeUnit.SECONDS);
        //TODO: schedule persistence in v2.1;
    }

    @PreDestroy
    public void stop() {
        if (queueExecutor != null) {
            queueExecutor.shutdownNow();
        }
    }

    @Override
    public void onModAdded(Mod mod) {
        queueExecutor.submit(() -> onModAddedSync(mod));
    }

    @Override
    public void onModUpdated(Mod mod) {
        queueExecutor.submit(() -> onModUpdatedSync(mod));
    }

    @Override
    public void onModConnect(ModId modId) {
        queueExecutor.submit(() -> onModConnectSync(modId));
    }

    @Override
    public void onModActivity(ModId modId) {
        queueExecutor.submit(() -> onModActivitySync(modId));
    }

    @Override
    public void onModDisconnect(ModId modId) {
        queueExecutor.submit(() -> onModDisconnectSync(modId));
    }

    @Override
    public void onModDeleted(Mod mod) {
        queueExecutor.submit(() -> onModDeleted(mod.getTenantId(), mod.getId()));
    }

    @Override
    public void onModInactivityTimeoutUpdate(ModId modId, long inactivityTimeout) {
        queueExecutor.submit(() -> onInactivityTimeoutUpdate(modId, inactivityTimeout));
    }

    @Override
    public void onClusterUpdate() {
        queueExecutor.submit(this::onClusterUpdateSync);
    }

    @Override
    public void onRemoteMsg(ServerAddress serverAddress, byte[] data) {
//        ClusterAPIProtos.ModStateServiceMsgProto proto;
//        try {
//            proto = ClusterAPIProtos.ModStateServiceMsgProto.parseFrom(data);
//        } catch (InvalidProtocolBufferException e) {
//            throw new RuntimeException(e);
//        }
//        TenantId tenantId = new TenantId(new UUID(proto.getTenantIdMSB(), proto.getTenantIdLSB()));
//        ModId modId = new ModId(new UUID(proto.getModIdMSB(), proto.getModIdLSB()));
//        if (proto.getDeleted()) {
//            queueExecutor.submit(() -> onModDeleted(tenantId, modId));
//        } else {
//            Mod mod = modService.findModById(modId);
//            if (mod != null) {
//                if (proto.getAdded()) {
//                    onModAdded(mod);
//                } else if (proto.getUpdated()) {
//                    onModUpdated(mod);
//                }
//            }
//        }
    }

    private void onClusterUpdateSync() {
        List<Tenant> tenants = tenantService.findTenants(new TextPageLink(Integer.MAX_VALUE)).getData();
        for (Tenant tenant : tenants) {
            List<ListenableFuture<ModStateData>> fetchFutures = new ArrayList<>();
            List<Mod> mods = modService.findModsByTenantId(tenant.getId(), new TextPageLink(Integer.MAX_VALUE)).getData();
            for (Mod mod : mods) {
                if (!routingService.resolveById(mod.getId()).isPresent()) {
                    if (!modStates.containsKey(mod.getId())) {
                        fetchFutures.add(fetchModState(mod));
                    }
                } else {
                    Set<ModId> tenantModSet = tenantMods.get(tenant.getId());
                    if (tenantModSet != null) {
                        tenantModSet.remove(mod.getId());
                    }
                    modStates.remove(mod.getId());
                }
            }
            try {
                Futures.successfulAsList(fetchFutures).get().forEach(this::addModUsingState);
            } catch (InterruptedException | ExecutionException e) {
                log.warn("Failed to init mod state service from DB", e);
            }
        }
    }

    private void initStateFromDB() {
        List<Tenant> tenants = tenantService.findTenants(new TextPageLink(Integer.MAX_VALUE)).getData();
        for (Tenant tenant : tenants) {
            List<ListenableFuture<ModStateData>> fetchFutures = new ArrayList<>();
            List<Mod> mods = modService.findModsByTenantId(tenant.getId(), new TextPageLink(Integer.MAX_VALUE)).getData();
            for (Mod mod : mods) {
                if (!routingService.resolveById(mod.getId()).isPresent()) {
                    fetchFutures.add(fetchModState(mod));
                }
            }
            try {
                Futures.successfulAsList(fetchFutures).get().forEach(this::addModUsingState);
            } catch (InterruptedException | ExecutionException e) {
                log.warn("Failed to init mod state service from DB", e);
            }
        }
    }

    private void addModUsingState(ModStateData state) {
        tenantMods.computeIfAbsent(state.getTenantId(), id -> ConcurrentHashMap.newKeySet()).add(state.getModId());
        modStates.put(state.getModId(), state);
    }

    private void updateState() {
        long ts = System.currentTimeMillis();
        Set<ModId> modIds = new HashSet<>(modStates.keySet());
        for (ModId modId : modIds) {
            ModStateData stateData = modStates.get(modId);
            ModState state = stateData.getState();
            state.setActive(ts < state.getLastActivityTime() + state.getInactivityTimeout());
            if (!state.isActive() && (state.getLastInactivityAlarmTime() == 0L || state.getLastInactivityAlarmTime() < state.getLastActivityTime())) {
                state.setLastInactivityAlarmTime(ts);
                pushRuleEngineMessage(stateData, INACTIVITY_EVENT);
                saveAttribute(modId, INACTIVITY_ALARM_TIME, ts);
                saveAttribute(modId, ACTIVITY_STATE, state.isActive());
            }
        }
    }

    private void onModConnectSync(ModId modId) {
        ModStateData stateData = getOrFetchModStateData(modId);
        if (stateData != null) {
            long ts = System.currentTimeMillis();
            stateData.getState().setLastConnectTime(ts);
            pushRuleEngineMessage(stateData, CONNECT_EVENT);
            saveAttribute(modId, LAST_CONNECT_TIME, ts);
        }
    }

    private void onModDisconnectSync(ModId modId) {
        ModStateData stateData = getOrFetchModStateData(modId);
        if (stateData != null) {
            long ts = System.currentTimeMillis();
            stateData.getState().setLastDisconnectTime(ts);
            pushRuleEngineMessage(stateData, DISCONNECT_EVENT);
            saveAttribute(modId, LAST_DISCONNECT_TIME, ts);
        }
    }

    private void onModActivitySync(ModId modId) {
        ModStateData stateData = getOrFetchModStateData(modId);
        if (stateData != null) {
            ModState state = stateData.getState();
            long ts = System.currentTimeMillis();
            state.setActive(true);
            stateData.getState().setLastActivityTime(ts);
            pushRuleEngineMessage(stateData, ACTIVITY_EVENT);
            saveAttribute(modId, LAST_ACTIVITY_TIME, ts);
            saveAttribute(modId, ACTIVITY_STATE, state.isActive());
        }
    }

    private ModStateData getOrFetchModStateData(ModId modId) {
        ModStateData modStateData = modStates.get(modId);
        if (modStateData == null) {
            if (!routingService.resolveById(modId).isPresent()) {
                Mod mod = modService.findModById(modId);
                if (mod != null) {
                    try {
                        modStateData = fetchModState(mod).get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.debug("[{}] Failed to fetch mod state!", modId, e);
                    }
                }
            }
        }
        return modStateData;
    }

    private void onInactivityTimeoutUpdate(ModId modId, long inactivityTimeout) {
        if (inactivityTimeout == 0L) {
            return;
        }
        ModStateData stateData = modStates.get(modId);
        if (stateData != null) {
            long ts = System.currentTimeMillis();
            ModState state = stateData.getState();
            state.setInactivityTimeout(inactivityTimeout);
            boolean oldActive = state.isActive();
            state.setActive(ts < state.getLastActivityTime() + state.getInactivityTimeout());
            if (!oldActive && state.isActive() || oldActive && !state.isActive()) {
                saveAttribute(modId, ACTIVITY_STATE, state.isActive());
            }
        }
    }

    private void onModAddedSync(Mod mod) {
        Optional<ServerAddress> address = routingService.resolveById(mod.getId());
        if (!address.isPresent()) {
            Futures.addCallback(fetchModState(mod), new FutureCallback<ModStateData>() {
                @Override
                public void onSuccess(@Nullable ModStateData state) {
                    addModUsingState(state);
                }

                @Override
                public void onFailure(Throwable t) {
                    log.warn("Failed to register mod to the state service", t);
                }
            });
        } else {
            sendModEvent(mod.getTenantId(), mod.getId(), address.get(), true, false, false);
        }
    }

    private void sendModEvent(TenantId tenantId, ModId modId, ServerAddress address, boolean added, boolean updated, boolean deleted) {
        log.trace("[{}][{}] Mod is monitored on other server: {}", tenantId, modId, address);
//        ClusterAPIProtos.ModStateServiceMsgProto.Builder builder = ClusterAPIProtos.ModStateServiceMsgProto.newBuilder();
//        builder.setTenantIdMSB(tenantId.getId().getMostSignificantBits());
//        builder.setTenantIdLSB(tenantId.getId().getLeastSignificantBits());
//        builder.setModIdMSB(modId.getId().getMostSignificantBits());
//        builder.setModIdLSB(modId.getId().getLeastSignificantBits());
//        builder.setAdded(added);
//        builder.setUpdated(updated);
//        builder.setDeleted(deleted);
//        clusterRpcService.tell(address, ClusterAPIProtos.MessageType.CLUSTER_DEVICE_STATE_SERVICE_MESSAGE, builder.build().toByteArray());
    }

    private void onModUpdatedSync(Mod mod) {
        Optional<ServerAddress> address = routingService.resolveById(mod.getId());
        if (!address.isPresent()) {
            ModStateData stateData = getOrFetchModStateData(mod.getId());
            if (stateData != null) {
                TbMsgMetaData md = new TbMsgMetaData();
                md.putValue("modName", mod.getName());
                md.putValue("modType", mod.getType());
                stateData.setMetaData(md);
            }
        } else {
            sendModEvent(mod.getTenantId(), mod.getId(), address.get(), false, true, false);
        }
    }

    private void onModDeleted(TenantId tenantId, ModId modId) {
        Optional<ServerAddress> address = routingService.resolveById(modId);
        if (!address.isPresent()) {
            modStates.remove(modId);
            Set<ModId> modIds = tenantMods.get(tenantId);
            if (modIds != null) {
                modIds.remove(modId);
                if (modIds.isEmpty()) {
                    tenantMods.remove(tenantId);
                }
            }
        } else {
            sendModEvent(tenantId, modId, address.get(), false, false, true);
        }
    }

    private ListenableFuture<ModStateData> fetchModState(Mod mod) {
        ListenableFuture<List<AttributeKvEntry>> attributes = attributesService.find(mod.getId(), DataConstants.SERVER_SCOPE, PERSISTENT_ATTRIBUTES);
        return Futures.transform(attributes, new Function<List<AttributeKvEntry>, ModStateData>() {
            @Nullable
            @Override
            public ModStateData apply(@Nullable List<AttributeKvEntry> attributes) {
                long lastActivityTime = getAttributeValue(attributes, LAST_ACTIVITY_TIME, 0L);
                long inactivityAlarmTime = getAttributeValue(attributes, INACTIVITY_ALARM_TIME, 0L);
                long inactivityTimeout = getAttributeValue(attributes, INACTIVITY_TIMEOUT, TimeUnit.SECONDS.toMillis(defaultInactivityTimeoutInSec));
                boolean active = System.currentTimeMillis() < lastActivityTime + inactivityTimeout;
                ModState modState = ModState.builder()
                        .active(active)
                        .lastConnectTime(getAttributeValue(attributes, LAST_CONNECT_TIME, 0L))
                        .lastDisconnectTime(getAttributeValue(attributes, LAST_DISCONNECT_TIME, 0L))
                        .lastActivityTime(lastActivityTime)
                        .lastInactivityAlarmTime(inactivityAlarmTime)
                        .inactivityTimeout(inactivityTimeout)
                        .build();
                TbMsgMetaData md = new TbMsgMetaData();
                md.putValue("modName", mod.getName());
                md.putValue("modType", mod.getType());
                return ModStateData.builder()
                        .tenantId(mod.getTenantId())
                        .modId(mod.getId())
                        .metaData(md)
                        .state(modState).build();
            }
        });
    }

    private long getAttributeValue(List<AttributeKvEntry> attributes, String attributeName, long defaultValue) {
        for (AttributeKvEntry attribute : attributes) {
            if (attribute.getKey().equals(attributeName)) {
                return attribute.getLongValue().orElse(defaultValue);
            }
        }
        return defaultValue;
    }

    private void pushRuleEngineMessage(ModStateData stateData, String msgType) {
        ModState state = stateData.getState();
        try {
            TbMsg tbMsg = new TbMsg(UUIDs.timeBased(), msgType, stateData.getModId(), stateData.getMetaData().copy(), TbMsgDataType.JSON
                    , json.writeValueAsString(state)
                    , null, null, 0L);
            actorService.onMsg(new SendToClusterMsg(stateData.getModId(), new ServiceToRuleEngineMsg(stateData.getTenantId(), tbMsg)));
        } catch (Exception e) {
            log.warn("[{}] Failed to push inactivity alarm: {}", stateData.getModId(), state, e);
        }
    }

    private void saveAttribute(ModId modId, String key, long value) {
        tsSubService.saveAttrAndNotify(modId, DataConstants.SERVER_SCOPE, key, value, new AttributeSaveCallback(modId, key, value));
    }

    private void saveAttribute(ModId modId, String key, boolean value) {
        tsSubService.saveAttrAndNotify(modId, DataConstants.SERVER_SCOPE, key, value, new AttributeSaveCallback(modId, key, value));
    }

    private class AttributeSaveCallback implements FutureCallback<Void> {
        private final ModId modId;
        private final String key;
        private final Object value;

        AttributeSaveCallback(ModId modId, String key, Object value) {
            this.modId = modId;
            this.key = key;
            this.value = value;
        }

        @Override
        public void onSuccess(@Nullable Void result) {
            log.trace("[{}] Successfully updated attribute [{}] with value [{}]", modId, key, value);
        }

        @Override
        public void onFailure(Throwable t) {
            log.warn("[{}] Failed to update attribute [{}] with value [{}]", modId, key, value, t);
        }
    }
}
