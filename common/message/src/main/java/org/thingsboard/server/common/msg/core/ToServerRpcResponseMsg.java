package org.thingsboard.server.common.msg.core;

import lombok.Data;

/**
 * @author Andrew Shvayka
 */
@Data
public class ToServerRpcResponseMsg {

    private final int requestId;
    private final String data;

    public boolean isSuccess() {
        return true;
    }
}
