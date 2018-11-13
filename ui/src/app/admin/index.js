import uiRouter from 'angular-ui-router';
import ngMaterial from 'angular-material';
import ngMessages from 'angular-messages';
import thingsboardApiAdmin from '../api/admin.service';
import thingsboardConfirmOnExit from '../components/confirm-on-exit.directive';
import thingsboardToast from '../services/toast';

import AdminRoutes from './admin.routes';
import AdminController from './admin.controller';

export default angular.module('thingsboard.admin', [
    uiRouter,
    ngMaterial,
    ngMessages,
    thingsboardApiAdmin,
    thingsboardConfirmOnExit,
    thingsboardToast
])
    .config(AdminRoutes)
    .controller('AdminController', AdminController)
    .name;
