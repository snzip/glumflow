package org.thingsboard.server.common.data.kv;

/**
 * Represents time series KV data entry
 * 
 * @author ashvayka
 *
 */
public interface TsKvEntry extends KvEntry {

    long getTs();

}
