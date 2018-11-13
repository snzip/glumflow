package org.thingsboard.server.service.update;

import org.thingsboard.server.service.update.model.UpdateMessage;

public interface UpdateService {

    UpdateMessage checkUpdates();

}
