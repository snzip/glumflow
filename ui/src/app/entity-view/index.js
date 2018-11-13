import uiRouter from 'angular-ui-router';
import thingsboardGrid from '../components/grid.directive';
import thingsboardApiUser from '../api/user.service';
import thingsboardApiEntityView from '../api/entity-view.service';
import thingsboardApiCustomer from '../api/customer.service';

import EntityViewRoutes from './entity-view.routes';
import {EntityViewController, EntityViewCardController} from './entity-view.controller';
import AssignEntityViewToCustomerController from './assign-to-customer.controller';
import AddEntityViewsToCustomerController from './add-entity-views-to-customer.controller';
import EntityViewDirective from './entity-view.directive';

export default angular.module('thingsboard.entityView', [
    uiRouter,
    thingsboardGrid,
    thingsboardApiUser,
    thingsboardApiEntityView,
    thingsboardApiCustomer
])
    .config(EntityViewRoutes)
    .controller('EntityViewController', EntityViewController)
    .controller('EntityViewCardController', EntityViewCardController)
    .controller('AssignEntityViewToCustomerController', AssignEntityViewToCustomerController)
    .controller('AddEntityViewsToCustomerController', AddEntityViewsToCustomerController)
    .directive('tbEntityView', EntityViewDirective)
    .name;
