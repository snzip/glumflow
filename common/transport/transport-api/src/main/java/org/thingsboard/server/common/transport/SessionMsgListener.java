package org.thingsboard.server.common.transport;

import org.thingsboard.server.gen.transport.TransportProtos.ToServerRpcResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.AttributeUpdateNotificationMsg;
import org.thingsboard.server.gen.transport.TransportProtos.GetAttributeResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.SessionCloseNotificationProto;
import org.thingsboard.server.gen.transport.TransportProtos.ToDeviceRpcRequestMsg;

/**
 * Created by ashvayka on 04.10.18.
 */
public interface SessionMsgListener {

    void onGetAttributesResponse(GetAttributeResponseMsg getAttributesResponse);

    void onAttributeUpdate(AttributeUpdateNotificationMsg attributeUpdateNotification);

    void onRemoteSessionCloseCommand(SessionCloseNotificationProto sessionCloseNotification);

    void onToDeviceRpcRequest(ToDeviceRpcRequestMsg toDeviceRequest);

    void onToServerRpcResponse(ToServerRpcResponseMsg toServerResponse);
}
