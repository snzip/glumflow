package org.thingsboard.server.transport.mqtt.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andrew Shvayka
 */
@Slf4j
public class DeviceSessionCtx extends MqttDeviceAwareSessionContext {

    @Getter
    private ChannelHandlerContext channel;
    private AtomicInteger msgIdSeq = new AtomicInteger(0);

    public DeviceSessionCtx(UUID sessionId, ConcurrentMap<MqttTopicMatcher, Integer> mqttQoSMap) {
        super(sessionId, mqttQoSMap);
    }

    public void setChannel(ChannelHandlerContext channel) {
        this.channel = channel;
    }

    public int nextMsgId() {
        return msgIdSeq.incrementAndGet();
    }
}
