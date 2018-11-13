/* eslint-disable import/no-unresolved, import/default */

import breadcrumbTemplate from './breadcrumb.tpl.html';
import homeTemplate from './home.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function HomeRoutes($stateProvider, $breadcrumbProvider) {

    $breadcrumbProvider.setOptions({
        prefixStateName: 'home',
        templateUrl: breadcrumbTemplate
    });

    $stateProvider
        .state('home', {
            url: '',
            module: 'private',
            auth: ['SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER'],
            views: {
                "@": {
                    controller: 'HomeController',
                    controllerAs: 'vm',
                    templateUrl: homeTemplate
                }
            },
            data: {
                pageTitle: 'home.home'
            },
            ncyBreadcrumb: {
                skip: true
            }
        });
}
