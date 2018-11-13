package org.thingsboard.server.service.install;

public interface SystemDataLoaderService {

    void createSysAdmin() throws Exception;

    void createAdminSettings() throws Exception;

    void loadSystemWidgets() throws Exception;

    void loadDemoData() throws Exception;

    void deleteSystemWidgetBundle(String bundleAlias) throws Exception;

}
