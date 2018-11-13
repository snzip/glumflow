package org.thingsboard.server.common.msg.session;

import java.util.UUID;

public interface SessionContext {

    UUID getSessionId();

    int nextMsgId();
}
