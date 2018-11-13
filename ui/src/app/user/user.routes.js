/* eslint-disable import/no-unresolved, import/default */

import usersTemplate from '../user/users.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function UserRoutes($stateProvider) {

    $stateProvider
        .state('home.tenants.users', {
            url: '/:tenantId/users',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['SYS_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: usersTemplate,
                    controllerAs: 'vm',
                    controller: 'UserController'
                }
            },
            data: {
                usersType: 'tenant',
                searchEnabled: true,
                pageTitle: 'user.tenant-admins'
            },
            ncyBreadcrumb: {
                label: '{"icon": "account_circle", "label": "user.tenant-admins"}'
            }
        })
        .state('home.customers.users', {
            url: '/:customerId/users',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: usersTemplate,
                    controllerAs: 'vm',
                    controller: 'UserController'
                }
            },
            data: {
                usersType: 'customer',
                searchEnabled: true,
                pageTitle: 'user.customer-users'
            },
            ncyBreadcrumb: {
                label: '{"icon": "account_circle", "label": "user.customer-users"}'
            }
        });

}
