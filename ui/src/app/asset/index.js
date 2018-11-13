import uiRouter from 'angular-ui-router';
import thingsboardGrid from '../components/grid.directive';
import thingsboardApiUser from '../api/user.service';
import thingsboardApiAsset from '../api/asset.service';
import thingsboardApiCustomer from '../api/customer.service';

import AssetRoutes from './asset.routes';
import {AssetController, AssetCardController} from './asset.controller';
import AssignAssetToCustomerController from './assign-to-customer.controller';
import AddAssetsToCustomerController from './add-assets-to-customer.controller';
import AssetDirective from './asset.directive';

export default angular.module('thingsboard.asset', [
    uiRouter,
    thingsboardGrid,
    thingsboardApiUser,
    thingsboardApiAsset,
    thingsboardApiCustomer
])
    .config(AssetRoutes)
    .controller('AssetController', AssetController)
    .controller('AssetCardController', AssetCardController)
    .controller('AssignAssetToCustomerController', AssignAssetToCustomerController)
    .controller('AddAssetsToCustomerController', AddAssetsToCustomerController)
    .directive('tbAsset', AssetDirective)
    .name;
