/* eslint-disable import/no-unresolved, import/default */

import profileTemplate from './profile.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function ProfileRoutes($stateProvider) {

    $stateProvider
        .state('home.profile', {
            url: '/profile',
            module: 'private',
            auth: ['SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "content@home": {
                    templateUrl: profileTemplate,
                    controllerAs: 'vm',
                    controller: 'ProfileController'
                }
            },
            data: {
                pageTitle: 'profile.profile'
            },
            ncyBreadcrumb: {
                label: '{"icon": "account_circle", "label": "profile.profile"}'
            }
        });

}
