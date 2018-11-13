/* eslint-disable import/no-unresolved, import/default */

import generalSettingsTemplate from '../admin/general-settings.tpl.html';
import outgoingMailSettingsTemplate from '../admin/outgoing-mail-settings.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AdminRoutes($stateProvider) {
    $stateProvider
        .state('home.settings', {
            url: '/settings',
            module: 'private',
            auth: ['SYS_ADMIN'],
            redirectTo: 'home.settings.general',
            ncyBreadcrumb: {
                label: '{"icon": "settings", "label": "admin.system-settings"}'
            }
        })
        .state('home.settings.general', {
            url: '/general',
            module: 'private',
            auth: ['SYS_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: generalSettingsTemplate,
                    controllerAs: 'vm',
                    controller: 'AdminController'
                }
            },
            data: {
                key: 'general',
                pageTitle: 'admin.general-settings'
            },
            ncyBreadcrumb: {
                label: '{"icon": "settings_applications", "label": "admin.general"}'
            }
        })
        .state('home.settings.outgoing-mail', {
            url: '/outgoing-mail',
            module: 'private',
            auth: ['SYS_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: outgoingMailSettingsTemplate,
                    controllerAs: 'vm',
                    controller: 'AdminController'
                }
            },
            data: {
                key: 'mail',
                pageTitle: 'admin.outgoing-mail-settings'
            },
            ncyBreadcrumb: {
                label: '{"icon": "mail", "label": "admin.outgoing-mail"}'
            }
        });
}
