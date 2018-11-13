/* eslint-disable import/no-unresolved, import/default */

import dashboardsTemplate from './dashboards.tpl.html';
import dashboardTemplate from './dashboard.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function DashboardRoutes($stateProvider) {
    $stateProvider
        .state('home.dashboards', {
            url: '/dashboards',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: dashboardsTemplate,
                    controller: 'DashboardsController',
                    controllerAs: 'vm'
                }
            },
            data: {
                dashboardsType: 'tenant',
                searchEnabled: true,
                pageTitle: 'dashboard.dashboards'
            },
            ncyBreadcrumb: {
                label: '{"icon": "dashboard", "label": "dashboard.dashboards"}'
            }
        })
        .state('home.customers.dashboards', {
            url: '/:customerId/dashboards',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: dashboardsTemplate,
                    controllerAs: 'vm',
                    controller: 'DashboardsController'
                }
            },
            data: {
                dashboardsType: 'customer',
                searchEnabled: true,
                pageTitle: 'customer.dashboards'
            },
            ncyBreadcrumb: {
                label: '{"icon": "dashboard", "label": "{{ vm.customerDashboardsTitle }}", "translate": "false"}'
            }
        })
        .state('home.dashboards.dashboard', {
            url: '/:dashboardId?state',
            reloadOnSearch: false,
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: dashboardTemplate,
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                }
            },
            data: {
                widgetEditMode: false,
                searchEnabled: false,
                pageTitle: 'dashboard.dashboard'
            },
            ncyBreadcrumb: {
                label: '{"icon": "dashboard", "label": "{{ vm.dashboard.title }}", "translate": "false"}'
            }
        })
        .state('dashboard', {
            url: '/dashboard/:dashboardId?state',
            reloadOnSearch: false,
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "@": {
                    templateUrl: dashboardTemplate,
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                }
            },
            data: {
                widgetEditMode: false,
                searchEnabled: false,
                pageTitle: 'dashboard.dashboard'
            }
        })
        .state('home.customers.dashboards.dashboard', {
            url: '/:dashboardId?state',
            reloadOnSearch: false,
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: dashboardTemplate,
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                }
            },
            data: {
                searchEnabled: false,
                pageTitle: 'customer.dashboard'
            },
            ncyBreadcrumb: {
                label: '{"icon": "dashboard", "label": "customer.dashboard"}'
            }
        })
}
