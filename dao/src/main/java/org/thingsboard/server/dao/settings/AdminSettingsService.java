package org.thingsboard.server.dao.settings;

import org.thingsboard.server.common.data.AdminSettings;
import org.thingsboard.server.common.data.id.AdminSettingsId;

public interface AdminSettingsService {

    AdminSettings findAdminSettingsById(AdminSettingsId adminSettingsId);

    AdminSettings findAdminSettingsByKey(String key);
    
    AdminSettings saveAdminSettings(AdminSettings adminSettings);
    
}
