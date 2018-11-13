/*@ngInject*/
export default function DashboardStateDialogController($scope, $mdDialog, $filter, dashboardUtils, isAdd, allStates, state) {

    var vm = this;

    vm.isAdd = isAdd;
    vm.allStates = allStates;
    vm.state = state;

    vm.stateIdTouched = false;

    if (vm.isAdd) {
        vm.state = dashboardUtils.createDefaultState('', false);
        vm.state.id = '';
        vm.prevStateId = '';
    } else {
        vm.state = state;
        vm.prevStateId = vm.state.id;
    }

    vm.cancel = cancel;
    vm.save = save;

    $scope.$watch("vm.state.name", function(newVal, prevVal) {
        if (!angular.equals(newVal, prevVal) && vm.state.name != null) {
            checkStateName();
        }
    });

    $scope.$watch("vm.state.id", function(newVal, prevVal) {
        if (!angular.equals(newVal, prevVal) && vm.state.id != null) {
            checkStateId();
        }
    });

    function checkStateName() {
        if (!vm.stateIdTouched && vm.isAdd) {
            vm.state.id = vm.state.name.toLowerCase().replace(/\W/g,"_");
        }
    }

    function checkStateId() {
        var result = $filter('filter')(vm.allStates, {id: vm.state.id}, true);
        if (result && result.length && result[0].id !== vm.prevStateId) {
            $scope.theForm.stateId.$setValidity('stateExists', false);
        } else {
            $scope.theForm.stateId.$setValidity('stateExists', true);
        }
    }

    function cancel() {
        $mdDialog.cancel();
    }

    function save() {
        $scope.theForm.$setPristine();
        vm.state.id = vm.state.id.trim();
        $mdDialog.hide(vm.state);
    }
}
