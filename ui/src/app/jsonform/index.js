import uiRouter from 'angular-ui-router';
import ngMaterial from 'angular-material';
import ngMessages from 'angular-messages';
import thingsboardJsonForm from "../components/json-form.directive";

import JsonFormRoutes from './jsonform.routes';
import JsonFormController from './jsonform.controller';

export default angular.module('thingsboard.jsonform', [
    uiRouter,
    ngMaterial,
    ngMessages,
    thingsboardJsonForm
])
    .config(JsonFormRoutes)
    .controller('JsonFormController', JsonFormController)
    .name;
