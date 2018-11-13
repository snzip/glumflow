import uiRouter from 'angular-ui-router';
import thingsboardGrid from '../components/grid.directive';
import thingsboardApiTenant from '../api/tenant.service';
import thingsboardContact from '../components/contact.directive';
import thingsboardContactShort from '../components/contact-short.filter';

import TenantRoutes from './tenant.routes';
import TenantController from './tenant.controller';
import TenantDirective from './tenant.directive';

export default angular.module('thingsboard.tenant', [
    uiRouter,
    thingsboardGrid,
    thingsboardApiTenant,
    thingsboardContact,
    thingsboardContactShort
])
    .config(TenantRoutes)
    .controller('TenantController', TenantController)
    .directive('tbTenant', TenantDirective)
    .name;
