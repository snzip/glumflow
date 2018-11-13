/* eslint-disable import/no-unresolved, import/default */

import tenantsTemplate from './tenants.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function TenantRoutes($stateProvider) {

    $stateProvider
        .state('home.tenants', {
            url: '/tenants',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['SYS_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: tenantsTemplate,
                    controllerAs: 'vm',
                    controller: 'TenantController'
                }
            },
            data: {
                searchEnabled: true,
                pageTitle: 'tenant.tenants'
            },
            ncyBreadcrumb: {
                label: '{"icon": "supervisor_account", "label": "tenant.tenants"}'
            }
        });
}
