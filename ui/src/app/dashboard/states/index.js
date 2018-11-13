import ManageDashboardStatesController from './manage-dashboard-states.controller';
import DashboardStateDialogController from './dashboard-state-dialog.controller';
import SelectTargetStateController from './select-target-state.controller';
import StatesComponentDirective from './states-component.directive';
import StatesControllerService from './states-controller.service';

export default angular.module('thingsboard.dashboard.states', [])
    .controller('ManageDashboardStatesController', ManageDashboardStatesController)
    .controller('DashboardStateDialogController', DashboardStateDialogController)
    .controller('SelectTargetStateController', SelectTargetStateController)
    .directive('tbStatesComponent', StatesComponentDirective)
    .factory('statesControllerService', StatesControllerService)
    .name;
