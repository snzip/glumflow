/*@ngInject*/
export default function AliasesEntitySelectPanelController(mdPanelRef, $scope, $filter, types, aliasController, onEntityAliasesUpdate) {

    var vm = this;
    vm._mdPanelRef = mdPanelRef;
    vm.aliasController = aliasController;
    vm.onEntityAliasesUpdate = onEntityAliasesUpdate;
    vm.entityAliases = {};
    vm.entityAliasesInfo = {};

    vm.currentAliasEntityChanged = currentAliasEntityChanged;

    var allEntityAliases = vm.aliasController.getEntityAliases();
    for (var aliasId in allEntityAliases) {
        var aliasInfo = vm.aliasController.getInstantAliasInfo(aliasId);
        if (aliasInfo && !aliasInfo.resolveMultiple && aliasInfo.currentEntity
            && aliasInfo.resolvedEntities.length > 1) {
            vm.entityAliasesInfo[aliasId] = angular.copy(aliasInfo);
            vm.entityAliasesInfo[aliasId].selectedId = aliasInfo.currentEntity.id;
        }
    }

    function currentAliasEntityChanged(aliasId, selectedId) {
        var resolvedEntities = vm.entityAliasesInfo[aliasId].resolvedEntities;
        var selected = $filter('filter')(resolvedEntities, {id: selectedId});
        if (selected && selected.length) {
            vm.aliasController.updateCurrentAliasEntity(aliasId, selected[0]);
            if (onEntityAliasesUpdate) {
                onEntityAliasesUpdate();
            }
        }
    }

}
