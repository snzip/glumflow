/*@ngInject*/
export default function SelectTargetStateController($scope, $mdDialog, dashboardUtils, states) {

    var vm = this;
    vm.states = states;
    vm.stateId = dashboardUtils.getRootStateId(vm.states);

    vm.cancel = cancel;
    vm.save = save;

    function cancel() {
        $mdDialog.cancel();
    }

    function save() {
        $scope.theForm.$setPristine();
        $mdDialog.hide(vm.stateId);
    }
}
