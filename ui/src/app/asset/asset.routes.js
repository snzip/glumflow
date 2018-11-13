/* eslint-disable import/no-unresolved, import/default */

import assetsTemplate from './assets.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AssetRoutes($stateProvider, types) {
    $stateProvider
        .state('home.assets', {
            url: '/assets',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: assetsTemplate,
                    controller: 'AssetController',
                    controllerAs: 'vm'
                }
            },
            data: {
                assetsType: 'tenant',
                searchEnabled: true,
                searchByEntitySubtype: true,
                searchEntityType: types.entityType.asset,
                pageTitle: 'asset.assets'
            },
            ncyBreadcrumb: {
                label: '{"icon": "domain", "label": "asset.assets"}'
            }
        })
        .state('home.customers.assets', {
            url: '/:customerId/assets',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: assetsTemplate,
                    controllerAs: 'vm',
                    controller: 'AssetController'
                }
            },
            data: {
                assetsType: 'customer',
                searchEnabled: true,
                searchByEntitySubtype: true,
                searchEntityType: types.entityType.asset,
                pageTitle: 'customer.assets'
            },
            ncyBreadcrumb: {
                label: '{"icon": "domain", "label": "{{ vm.customerAssetsTitle }}", "translate": "false"}'
            }
        });

}
