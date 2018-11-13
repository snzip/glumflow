package org.thingsboard.server.kafka;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by ashvayka on 25.09.18.
 */
@Slf4j
public abstract class AbstractTbKafkaTemplate {
    protected byte[] uuidToBytes(UUID uuid) {
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.putLong(uuid.getMostSignificantBits());
        buf.putLong(uuid.getLeastSignificantBits());
        return buf.array();
    }

    protected static UUID bytesToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    protected byte[] stringToBytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    protected String bytesToString(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
