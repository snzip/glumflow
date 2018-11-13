import './login.scss';

import uiRouter from 'angular-ui-router';
import thingsboardApiLogin from '../api/login.service';
import thingsboardApiUser from '../api/user.service';
import thingsboardToast from '../services/toast';

import LoginRoutes from './login.routes';
import LoginController from './login.controller';
import ResetPasswordRequestController from './reset-password-request.controller';
import ResetPasswordController from './reset-password.controller';
import CreatePasswordController from './create-password.controller';

export default angular.module('thingsboard.login', [
    uiRouter,
    thingsboardApiLogin,
    thingsboardApiUser,
    thingsboardToast
])
    .config(LoginRoutes)
    .controller('LoginController', LoginController)
    .controller('ResetPasswordRequestController', ResetPasswordRequestController)
    .controller('ResetPasswordController', ResetPasswordController)
    .controller('CreatePasswordController', CreatePasswordController)
    .name;
