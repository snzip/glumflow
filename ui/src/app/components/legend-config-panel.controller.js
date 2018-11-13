/*@ngInject*/
export default function LegendConfigPanelController(mdPanelRef, $scope, types, legendConfig, onLegendConfigUpdate) {

    var vm = this;
    vm._mdPanelRef = mdPanelRef;
    vm.legendConfig = legendConfig;
    vm.onLegendConfigUpdate = onLegendConfigUpdate;
    vm.positions = types.position;

    vm._mdPanelRef.config.onOpenComplete = function () {
        $scope.theForm.$setPristine();
    }

    $scope.$watch('vm.legendConfig', function () {
        if (onLegendConfigUpdate) {
            onLegendConfigUpdate(vm.legendConfig);
        }
    }, true);
}
