package org.thingsboard.server.common.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ashvayka on 13.01.18.
 */
@Data
@NoArgsConstructor
public final class TbMsgMetaData implements Serializable {

    private final Map<String, String> data = new ConcurrentHashMap<>();

    public TbMsgMetaData(Map<String, String> data) {
        this.data.putAll(data);
    }

    public String getValue(String key) {
        return data.get(key);
    }

    public void putValue(String key, String value) {
        data.put(key, value);
    }

    public Map<String, String> values() {
        return new HashMap<>(data);
    }

    public TbMsgMetaData copy() {
        return new TbMsgMetaData(new ConcurrentHashMap<>(data));
    }
}
