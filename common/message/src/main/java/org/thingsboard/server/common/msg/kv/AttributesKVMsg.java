package org.thingsboard.server.common.msg.kv;

import java.io.Serializable;
import java.util.List;

import org.thingsboard.server.common.data.kv.AttributeKey;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;

public interface AttributesKVMsg extends Serializable {

    List<AttributeKvEntry> getClientAttributes();
    List<AttributeKvEntry> getSharedAttributes();
    List<AttributeKey> getDeletedAttributes();
}
