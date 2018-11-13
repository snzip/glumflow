package org.thingsboard.server.service.transport;

import akka.actor.ActorRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.thingsboard.rule.engine.api.util.DonAsynchron;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.common.transport.SessionMsgListener;
import org.thingsboard.server.common.transport.TransportService;
import org.thingsboard.server.common.transport.TransportServiceCallback;
import org.thingsboard.server.common.transport.service.AbstractTransportService;
import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.gen.transport.TransportProtos.DeviceActorToTransportMsg;
import org.thingsboard.server.gen.transport.TransportProtos.GetAttributeRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.GetOrCreateDeviceFromGatewayRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.GetOrCreateDeviceFromGatewayResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.PostAttributeMsg;
import org.thingsboard.server.gen.transport.TransportProtos.PostTelemetryMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SessionEventMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SessionInfoProto;
import org.thingsboard.server.gen.transport.TransportProtos.SubscribeToAttributeUpdatesMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SubscribeToRPCMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ToDeviceRpcResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ToServerRpcRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.TransportApiRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.TransportToDeviceActorMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceCredentialsResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceTokenRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceX509CertRequestMsg;
import org.thingsboard.server.service.cluster.routing.ClusterRoutingService;
import org.thingsboard.server.service.cluster.rpc.ClusterRpcService;
import org.thingsboard.server.service.encoding.DataDecodingEncodingService;
import org.thingsboard.server.service.transport.msg.TransportToDeviceActorMsgWrapper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by ashvayka on 12.10.18.
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "transport", value = "type", havingValue = "local")
public class LocalTransportService extends AbstractTransportService implements RuleEngineTransportService {

    @Autowired
    private TransportApiService transportApiService;

    @Autowired
    private ActorSystemContext actorContext;

    //TODO: completely replace this routing with the Kafka routing by partition ids.
    @Autowired
    private ClusterRoutingService routingService;
    @Autowired
    private ClusterRpcService rpcService;
    @Autowired
    private DataDecodingEncodingService encodingService;

    @PostConstruct
    public void init() {
        super.init();
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public void process(ValidateDeviceTokenRequestMsg msg, TransportServiceCallback<ValidateDeviceCredentialsResponseMsg> callback) {
        DonAsynchron.withCallback(
                transportApiService.handle(TransportApiRequestMsg.newBuilder().setValidateTokenRequestMsg(msg).build()),
                transportApiResponseMsg -> {
                    if (callback != null) {
                        callback.onSuccess(transportApiResponseMsg.getValidateTokenResponseMsg());
                    }
                },
                getThrowableConsumer(callback), transportCallbackExecutor);
    }

    @Override
    public void process(ValidateDeviceX509CertRequestMsg msg, TransportServiceCallback<ValidateDeviceCredentialsResponseMsg> callback) {
        DonAsynchron.withCallback(
                transportApiService.handle(TransportApiRequestMsg.newBuilder().setValidateX509CertRequestMsg(msg).build()),
                transportApiResponseMsg -> {
                    if (callback != null) {
                        callback.onSuccess(transportApiResponseMsg.getValidateTokenResponseMsg());
                    }
                },
                getThrowableConsumer(callback), transportCallbackExecutor);
    }

    @Override
    public void process(GetOrCreateDeviceFromGatewayRequestMsg msg, TransportServiceCallback<GetOrCreateDeviceFromGatewayResponseMsg> callback) {
        DonAsynchron.withCallback(
                transportApiService.handle(TransportApiRequestMsg.newBuilder().setGetOrCreateDeviceRequestMsg(msg).build()),
                transportApiResponseMsg -> {
                    if (callback != null) {
                        callback.onSuccess(transportApiResponseMsg.getGetOrCreateDeviceResponseMsg());
                    }
                },
                getThrowableConsumer(callback), transportCallbackExecutor);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, SessionEventMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setSessionEvent(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, PostTelemetryMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setPostTelemetry(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, PostAttributeMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setPostAttributes(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, GetAttributeRequestMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setGetAttributes(msg).build(), callback);
    }

    @Override
    public void process(SessionInfoProto sessionInfo, TransportProtos.SubscriptionInfoProto msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setSubscriptionInfo(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, SubscribeToAttributeUpdatesMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setSubscribeToAttributes(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, SubscribeToRPCMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setSubscribeToRPC(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, ToDeviceRpcResponseMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setToDeviceRPCCallResponse(msg).build(), callback);
    }

    @Override
    protected void doProcess(SessionInfoProto sessionInfo, ToServerRpcRequestMsg msg, TransportServiceCallback<Void> callback) {
        forwardToDeviceActor(TransportToDeviceActorMsg.newBuilder().setSessionInfo(sessionInfo).setToServerRPCCallRequest(msg).build(), callback);
    }

    @Override
    public void process(String nodeId, DeviceActorToTransportMsg msg) {
        process(nodeId, msg, null, null);
    }

    @Override
    public void process(String nodeId, DeviceActorToTransportMsg msg, Runnable onSuccess, Consumer<Throwable> onFailure) {
        processToTransportMsg(msg);
        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    private void forwardToDeviceActor(TransportToDeviceActorMsg toDeviceActorMsg, TransportServiceCallback<Void> callback) {
        TransportToDeviceActorMsgWrapper wrapper = new TransportToDeviceActorMsgWrapper(toDeviceActorMsg);
        Optional<ServerAddress> address = routingService.resolveById(wrapper.getDeviceId());
        if (address.isPresent()) {
            rpcService.tell(encodingService.convertToProtoDataMessage(address.get(), wrapper));
        } else {
            actorContext.getAppActor().tell(wrapper, ActorRef.noSender());
        }
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    private <T> Consumer<Throwable> getThrowableConsumer(TransportServiceCallback<T> callback) {
        return e -> {
            if (callback != null) {
                callback.onError(e);
            }
        };
    }

}
