import uiRouter from 'angular-ui-router';
import thingsboardApiCustomer from '../api/customer.service';
import thingsboardGrid from '../components/grid.directive';
import thingsboardContact from '../components/contact.directive';
import thingsboardContactShort from '../components/contact-short.filter';

import CustomerRoutes from './customer.routes';
import CustomerController from './customer.controller';
import CustomerDirective from './customer.directive';

export default angular.module('thingsboard.customer', [
    uiRouter,
    thingsboardApiCustomer,
    thingsboardGrid,
    thingsboardContact,
    thingsboardContactShort
])
    .config(CustomerRoutes)
    .controller('CustomerController', CustomerController)
    .directive('tbCustomer', CustomerDirective)
    .name;
