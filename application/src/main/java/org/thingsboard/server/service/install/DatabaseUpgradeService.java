package org.thingsboard.server.service.install;

public interface DatabaseUpgradeService {

    void upgradeDatabase(String fromVersion) throws Exception;

}
