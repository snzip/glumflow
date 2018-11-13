import uiRouter from 'angular-ui-router';
import thingsboardGrid from '../components/grid.directive';
import thingsboardApiUser from '../api/user.service';
import thingsboardApiDevice from '../api/device.service';
import thingsboardApiCustomer from '../api/customer.service';

import DeviceRoutes from './device.routes';
import {DeviceController, DeviceCardController} from './device.controller';
import AssignDeviceToCustomerController from './assign-to-customer.controller';
import AddDevicesToCustomerController from './add-devices-to-customer.controller';
import ManageDeviceCredentialsController from './device-credentials.controller';
import DeviceDirective from './device.directive';

export default angular.module('thingsboard.device', [
    uiRouter,
    thingsboardGrid,
    thingsboardApiUser,
    thingsboardApiDevice,
    thingsboardApiCustomer
])
    .config(DeviceRoutes)
    .controller('DeviceController', DeviceController)
    .controller('DeviceCardController', DeviceCardController)
    .controller('AssignDeviceToCustomerController', AssignDeviceToCustomerController)
    .controller('AddDevicesToCustomerController', AddDevicesToCustomerController)
    .controller('ManageDeviceCredentialsController', ManageDeviceCredentialsController)
    .directive('tbDevice', DeviceDirective)
    .name;
