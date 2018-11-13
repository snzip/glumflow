/* eslint-disable import/no-unresolved, import/default */

import loginTemplate from './login.tpl.html';
import resetPasswordTemplate from './reset-password.tpl.html';
import resetPasswordRequestTemplate from './reset-password-request.tpl.html';
import createPasswordTemplate from './create-password.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function LoginRoutes($stateProvider) {
    $stateProvider.state('login', {
        url: '/login',
        module: 'public',
        views: {
            "@": {
                controller: 'LoginController',
                controllerAs: 'vm',
                templateUrl: loginTemplate
            }
        },
        data: {
            pageTitle: 'login.login'
        }
    }).state('login.resetPasswordRequest', {
        url: '/resetPasswordRequest',
        module: 'public',
        views: {
            "@": {
                controller: 'ResetPasswordRequestController',
                controllerAs: 'vm',
                templateUrl: resetPasswordRequestTemplate
            }
        },
        data: {
            pageTitle: 'login.request-password-reset'
        }
    }).state('login.resetPassword', {
        url: '/resetPassword?resetToken',
        module: 'public',
        views: {
            "@": {
                controller: 'ResetPasswordController',
                controllerAs: 'vm',
                templateUrl: resetPasswordTemplate
            }
        },
        data: {
            pageTitle: 'login.reset-password'
        }
    }).state('login.createPassword', {
        url: '/createPassword?activateToken',
        module: 'public',
        views: {
            "@": {
                controller: 'CreatePasswordController',
                controllerAs: 'vm',
                templateUrl: createPasswordTemplate
            }
        },
        data: {
            pageTitle: 'login.create-password'
        }
    });
}
