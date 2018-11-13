/* eslint-disable import/no-unresolved, import/default */

import jsonFormTemplate from './jsonform.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function JsonFormRoutes($stateProvider) {
    $stateProvider
        .state('home.jsonform', {
            url: '/jsonform',
            module: 'private',
            auth: ['SYS_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: jsonFormTemplate,
                    controllerAs: 'vm',
                    controller: 'JsonFormController'
                }
            },
            data: {
                key: 'general',
                pageTitle: 'admin.general-settings'
            },
            ncyBreadcrumb: {
                label: '{"icon": "settings", "label": "admin.system-settings"}'
            }
        });
}
