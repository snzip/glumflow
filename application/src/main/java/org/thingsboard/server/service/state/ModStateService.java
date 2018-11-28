package org.thingsboard.server.service.state;

import org.thingsboard.server.common.data.Mod;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.msg.cluster.ServerAddress;

import java.util.Optional;

/**
 * Created by ashvayka on 01.05.18.
 */
public interface ModStateService {

    void onModAdded(Mod mod);

    void onModUpdated(Mod mod);

    void onModDeleted(Mod mod);

    void onModConnect(ModId modId);

    void onModActivity(ModId modId);

    void onModDisconnect(ModId modId);

    void onModInactivityTimeoutUpdate(ModId modId, long inactivityTimeout);

    void onClusterUpdate();

    void onRemoteMsg(ServerAddress serverAddress, byte[] bytes);
}
