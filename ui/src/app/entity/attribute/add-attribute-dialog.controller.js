/*@ngInject*/
export default function AddAttributeDialogController($scope, $mdDialog, types, attributeService, entityType, entityId, attributeScope) {

    var vm = this;

    vm.attribute = {};

    vm.valueTypes = types.valueType;

    vm.valueType = types.valueType.string;

    vm.add = add;
    vm.cancel = cancel;

    function cancel() {
        $mdDialog.cancel();
    }

    function add() {
        $scope.theForm.$setPristine();
        attributeService.saveEntityAttributes(entityType, entityId, attributeScope, [vm.attribute]).then(
            function success() {
                $mdDialog.hide();
            }
        );
    }

    $scope.$watch('vm.valueType', function() {
        if (vm.valueType === types.valueType.boolean) {
            vm.attribute.value = false;
        } else {
            vm.attribute.value = null;
        }
    });
}
