package org.thingsboard.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.rule.engine.api.MailService;
import org.thingsboard.server.common.data.AdminSettings;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.dao.settings.AdminSettingsService;
import org.thingsboard.server.service.update.UpdateService;
import org.thingsboard.server.service.update.model.UpdateMessage;

@RestController
@RequestMapping("/api/admin")
public class AdminController extends BaseController {

    @Autowired
    private MailService mailService;
    
    @Autowired
    private AdminSettingsService adminSettingsService;

    @Autowired
    private UpdateService updateService;

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/settings/{key}", method = RequestMethod.GET)
    @ResponseBody
    public AdminSettings getAdminSettings(@PathVariable("key") String key) throws ThingsboardException {
        try {
            return checkNotNull(adminSettingsService.findAdminSettingsByKey(key));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    @ResponseBody 
    public AdminSettings saveAdminSettings(@RequestBody AdminSettings adminSettings) throws ThingsboardException {
        try {
            adminSettings = checkNotNull(adminSettingsService.saveAdminSettings(adminSettings));
            if (adminSettings.getKey().equals("mail")) {
                mailService.updateMailConfiguration();
            }
            return adminSettings;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/settings/testMail", method = RequestMethod.POST)
    public void sendTestMail(@RequestBody AdminSettings adminSettings) throws ThingsboardException {
        try {
            adminSettings = checkNotNull(adminSettings);
            if (adminSettings.getKey().equals("mail")) {
               String email = getCurrentUser().getEmail();
               mailService.sendTestMail(adminSettings.getJsonValue(), email);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/updates", method = RequestMethod.GET)
    @ResponseBody
    public UpdateMessage checkUpdates() throws ThingsboardException {
        try {
            return updateService.checkUpdates();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

}
