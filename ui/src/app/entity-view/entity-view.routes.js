/* eslint-disable import/no-unresolved, import/default */

import entityViewsTemplate from './entity-views.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function EntityViewRoutes($stateProvider, types) {
    $stateProvider
        .state('home.entityViews', {
            url: '/entityViews',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: entityViewsTemplate,
                    controller: 'EntityViewController',
                    controllerAs: 'vm'
                }
            },
            data: {
                entityViewsType: 'tenant',
                searchEnabled: true,
                searchByEntitySubtype: true,
                searchEntityType: types.entityType.entityView,
                pageTitle: 'entity-view.entity-views'
            },
            ncyBreadcrumb: {
                label: '{"icon": "view_quilt", "label": "entity-view.entity-views"}'
            }
        })
        .state('home.customers.entityViews', {
            url: '/:customerId/entityViews',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: entityViewsTemplate,
                    controllerAs: 'vm',
                    controller: 'EntityViewController'
                }
            },
            data: {
                entityViewsType: 'customer',
                searchEnabled: true,
                searchByEntitySubtype: true,
                searchEntityType: types.entityType.entityView,
                pageTitle: 'customer.entity-views'
            },
            ncyBreadcrumb: {
                label: '{"icon": "view_quilt", "label": "{{ vm.customerEntityViewsTitle }}", "translate": "false"}'
            }
        });

}
