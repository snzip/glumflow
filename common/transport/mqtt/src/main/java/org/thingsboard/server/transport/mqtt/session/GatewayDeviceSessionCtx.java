package org.thingsboard.server.transport.mqtt.session;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.common.transport.SessionMsgListener;
import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.gen.transport.TransportProtos.DeviceInfoProto;
import org.thingsboard.server.gen.transport.TransportProtos.SessionInfoProto;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ashvayka on 19.01.17.
 */
@Slf4j
public class GatewayDeviceSessionCtx extends MqttDeviceAwareSessionContext implements SessionMsgListener {

    private final GatewaySessionHandler parent;
    private final SessionInfoProto sessionInfo;

    public GatewayDeviceSessionCtx(GatewaySessionHandler parent, DeviceInfoProto deviceInfo, ConcurrentMap<MqttTopicMatcher, Integer> mqttQoSMap) {
        super(UUID.randomUUID(), mqttQoSMap);
        this.parent = parent;
        this.sessionInfo = SessionInfoProto.newBuilder()
                .setNodeId(parent.getNodeId())
                .setSessionIdMSB(sessionId.getMostSignificantBits())
                .setSessionIdLSB(sessionId.getLeastSignificantBits())
                .setDeviceIdMSB(deviceInfo.getDeviceIdMSB())
                .setDeviceIdLSB(deviceInfo.getDeviceIdLSB())
                .setTenantIdMSB(deviceInfo.getTenantIdMSB())
                .setTenantIdLSB(deviceInfo.getTenantIdLSB())
                .build();
        setDeviceInfo(deviceInfo);
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public int nextMsgId() {
        return parent.nextMsgId();
    }

    SessionInfoProto getSessionInfo() {
        return sessionInfo;
    }

    @Override
    public void onGetAttributesResponse(TransportProtos.GetAttributeResponseMsg response) {
        try {
            parent.getAdaptor().convertToGatewayPublish(this, response).ifPresent(parent::writeAndFlush);
        } catch (Exception e) {
            log.trace("[{}] Failed to convert device attributes response to MQTT msg", sessionId, e);
        }
    }

    @Override
    public void onAttributeUpdate(TransportProtos.AttributeUpdateNotificationMsg notification) {
        try {
            parent.getAdaptor().convertToGatewayPublish(this, getDeviceInfo().getDeviceName(), notification).ifPresent(parent::writeAndFlush);
        } catch (Exception e) {
            log.trace("[{}] Failed to convert device attributes response to MQTT msg", sessionId, e);
        }
    }

    @Override
    public void onRemoteSessionCloseCommand(TransportProtos.SessionCloseNotificationProto sessionCloseNotification) {
        parent.deregisterSession(getDeviceInfo().getDeviceName());
    }

    @Override
    public void onToDeviceRpcRequest(TransportProtos.ToDeviceRpcRequestMsg request) {
        try {
            parent.getAdaptor().convertToGatewayPublish(this, getDeviceInfo().getDeviceName(), request).ifPresent(parent::writeAndFlush);
        } catch (Exception e) {
            log.trace("[{}] Failed to convert device attributes response to MQTT msg", sessionId, e);
        }
    }

    @Override
    public void onToServerRpcResponse(TransportProtos.ToServerRpcResponseMsg toServerResponse) {
        // This feature is not supported in the TB IoT Gateway yet.
    }
}
