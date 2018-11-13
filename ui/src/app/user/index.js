import uiRouter from 'angular-ui-router';
import thingsboardGrid from '../components/grid.directive';
import thingsboardApiUser from '../api/user.service';
import thingsboardToast from '../services/toast';

import UserRoutes from './user.routes';
import UserController from './user.controller';
import AddUserController from './add-user.controller';
import ActivationLinkDialogController from './activation-link.controller';
import UserDirective from './user.directive';

export default angular.module('thingsboard.user', [
    uiRouter,
    thingsboardGrid,
    thingsboardApiUser,
    thingsboardToast
])
    .config(UserRoutes)
    .controller('UserController', UserController)
    .controller('AddUserController', AddUserController)
    .controller('ActivationLinkDialogController', ActivationLinkDialogController)
    .directive('tbUser', UserDirective)
    .name;
