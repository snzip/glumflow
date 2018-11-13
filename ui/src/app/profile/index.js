import uiRouter from 'angular-ui-router';
import ngMaterial from 'angular-material';
import ngMessages from 'angular-messages';
import thingsboardApiUser from '../api/user.service';
import thingsboardApiLogin from '../api/login.service';
import thingsboardConfirmOnExit from '../components/confirm-on-exit.directive';

import ProfileRoutes from './profile.routes';
import ProfileController from './profile.controller';
import ChangePasswordController from './change-password.controller';

export default angular.module('thingsboard.profile', [
    uiRouter,
    ngMaterial,
    ngMessages,
    thingsboardApiUser,
    thingsboardApiLogin,
    thingsboardConfirmOnExit
])
    .config(ProfileRoutes)
    .controller('ProfileController', ProfileController)
    .controller('ChangePasswordController', ChangePasswordController)
    .name;
