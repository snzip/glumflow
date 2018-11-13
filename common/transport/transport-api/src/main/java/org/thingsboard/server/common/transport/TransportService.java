package org.thingsboard.server.common.transport;

import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.gen.transport.TransportProtos.ToServerRpcRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ToDeviceRpcResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SubscribeToAttributeUpdatesMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SubscribeToRPCMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SessionInfoProto;
import org.thingsboard.server.gen.transport.TransportProtos.PostAttributeMsg;
import org.thingsboard.server.gen.transport.TransportProtos.PostTelemetryMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SessionEventMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceCredentialsResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceTokenRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceX509CertRequestMsg;
import org.thingsboard.server.gen.transport.TransportProtos.GetAttributeRequestMsg;

/**
 * Created by ashvayka on 04.10.18.
 */
public interface TransportService {

    void process(ValidateDeviceTokenRequestMsg msg,
                 TransportServiceCallback<ValidateDeviceCredentialsResponseMsg> callback);

    void process(ValidateDeviceX509CertRequestMsg msg,
                 TransportServiceCallback<ValidateDeviceCredentialsResponseMsg> callback);

    void process(TransportProtos.GetOrCreateDeviceFromGatewayRequestMsg msg,
                 TransportServiceCallback<TransportProtos.GetOrCreateDeviceFromGatewayResponseMsg> callback);

    boolean checkLimits(SessionInfoProto sessionInfo, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, SessionEventMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, PostTelemetryMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, PostAttributeMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, GetAttributeRequestMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, SubscribeToAttributeUpdatesMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, SubscribeToRPCMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, ToDeviceRpcResponseMsg msg, TransportServiceCallback<Void> callback);

    void process(SessionInfoProto sessionInfo, ToServerRpcRequestMsg msg, TransportServiceCallback<Void> callback);

    void process(TransportProtos.SessionInfoProto sessionInfo, TransportProtos.SubscriptionInfoProto msg, TransportServiceCallback<Void> callback);

    void registerAsyncSession(SessionInfoProto sessionInfo, SessionMsgListener listener);

    void registerSyncSession(SessionInfoProto sessionInfo, SessionMsgListener listener, long timeout);

    void reportActivity(SessionInfoProto sessionInfo);

    void deregisterSession(SessionInfoProto sessionInfo);

}
