package org.thingsboard.server.dao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;
import org.thingsboard.server.common.data.AdminSettings;
import org.thingsboard.server.dao.exception.DataValidationException;

public abstract class BaseAdminSettingsServiceTest extends AbstractServiceTest {

    @Test
    public void testFindAdminSettingsByKey() {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("general");
        Assert.assertNotNull(adminSettings);
        adminSettings = adminSettingsService.findAdminSettingsByKey("mail");
        Assert.assertNotNull(adminSettings);
        adminSettings = adminSettingsService.findAdminSettingsByKey("unknown");
        Assert.assertNull(adminSettings);
    }
    
    @Test
    public void testFindAdminSettingsById() {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("general");
        AdminSettings foundAdminSettings = adminSettingsService.findAdminSettingsById(adminSettings.getId());
        Assert.assertNotNull(foundAdminSettings);
        Assert.assertEquals(adminSettings, foundAdminSettings);
    }
    
    @Test
    public void testSaveAdminSettings() throws Exception {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("general");
        JsonNode json = adminSettings.getJsonValue();
        ((ObjectNode) json).put("baseUrl", "http://myhost.org");
        adminSettings.setJsonValue(json);
        adminSettingsService.saveAdminSettings(adminSettings);
        AdminSettings savedAdminSettings = adminSettingsService.findAdminSettingsByKey("general");
        Assert.assertNotNull(savedAdminSettings);
        Assert.assertEquals(adminSettings.getJsonValue(), savedAdminSettings.getJsonValue());
    }
    
    @Test(expected = DataValidationException.class)
    public void testSaveAdminSettingsWithEmptyKey() {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("mail");
        adminSettings.setKey(null);
        adminSettingsService.saveAdminSettings(adminSettings);
    }
    
    @Test(expected = DataValidationException.class)
    public void testChangeAdminSettingsKey() {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("mail");
        adminSettings.setKey("newKey");
        adminSettingsService.saveAdminSettings(adminSettings);
    }
    
    @Test(expected = DataValidationException.class)
    public void testSaveAdminSettingsWithNewJsonStructure() throws Exception {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("mail");
        JsonNode json = adminSettings.getJsonValue();
        ((ObjectNode) json).put("newKey", "my new value");
        adminSettings.setJsonValue(json);
        adminSettingsService.saveAdminSettings(adminSettings);
    }
    
    @Test(expected = DataValidationException.class)
    public void testSaveAdminSettingsWithNonTextValue() throws Exception {
        AdminSettings adminSettings = adminSettingsService.findAdminSettingsByKey("mail");
        JsonNode json = adminSettings.getJsonValue();
        ((ObjectNode) json).put("timeout", 10000L);
        adminSettings.setJsonValue(json);
        adminSettingsService.saveAdminSettings(adminSettings);
    }
}
