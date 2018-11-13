/* eslint-disable import/no-unresolved, import/default */

import devicesTemplate from './devices.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function DeviceRoutes($stateProvider, types) {
    $stateProvider
        .state('home.devices', {
            url: '/devices',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: devicesTemplate,
                    controller: 'DeviceController',
                    controllerAs: 'vm'
                }
            },
            data: {
                devicesType: 'tenant',
                searchEnabled: true,
                searchByEntitySubtype: true,
                searchEntityType: types.entityType.device,
                pageTitle: 'device.devices'
            },
            ncyBreadcrumb: {
                label: '{"icon": "devices_other", "label": "device.devices"}'
            }
        })
        .state('home.customers.devices', {
            url: '/:customerId/devices',
            params: {'topIndex': 0},
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: devicesTemplate,
                    controllerAs: 'vm',
                    controller: 'DeviceController'
                }
            },
            data: {
                devicesType: 'customer',
                searchEnabled: true,
                searchByEntitySubtype: true,
                searchEntityType: types.entityType.device,
                pageTitle: 'customer.devices'
            },
            ncyBreadcrumb: {
                label: '{"icon": "devices_other", "label": "{{ vm.customerDevicesTitle }}", "translate": "false"}'
            }
        });

}
