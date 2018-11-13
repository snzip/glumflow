/* eslint-disable import/no-unresolved, import/default */

import customersTemplate from './customers.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function CustomerRoutes($stateProvider) {

    $stateProvider
        .state('home.customers', {
            url: '/customers',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: customersTemplate,
                    controllerAs: 'vm',
                    controller: 'CustomerController'
                }
            },
            data: {
                searchEnabled: true,
                pageTitle: 'customer.customers'
            },
            ncyBreadcrumb: {
                label: '{"icon": "supervisor_account", "label": "customer.customers"}'
            }
        });

}
