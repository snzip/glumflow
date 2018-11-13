import thingsboardDatakeyConfig from './datakey-config.directive';

export default angular.module('thingsboard.dialogs.datakeyConfigDialog', [thingsboardDatakeyConfig])
    .controller('DatakeyConfigDialogController', DatakeyConfigDialogController)
    .name;

/*@ngInject*/
function DatakeyConfigDialogController($scope, $mdDialog, $q, entityService, dataKey, dataKeySettingsSchema, entityAlias, aliasController) {

    var vm = this;

    vm.dataKey = dataKey;
    vm.dataKeySettingsSchema = dataKeySettingsSchema;
    vm.entityAlias = entityAlias;
    vm.aliasController = aliasController;

    vm.hide = function () {
        $mdDialog.hide();
    };

    vm.cancel = function () {
        $mdDialog.cancel();
    };

    vm.fetchEntityKeys = function (entityAliasId, query, type) {
        var deferred = $q.defer();
        vm.aliasController.getAliasInfo(entityAliasId).then(
            function success(aliasInfo) {
                var entity = aliasInfo.currentEntity;
                if (entity) {
                    entityService.getEntityKeys(entity.entityType, entity.id, query, type, {ignoreLoading: true}).then(
                        function success(keys) {
                            deferred.resolve(keys);
                        },
                        function fail() {
                            deferred.resolve([]);
                        }
                    );
                } else {
                    deferred.resolve([]);
                }
            },
            function fail() {
                deferred.resolve([]);
            }
        );
        return deferred.promise;
    };

    vm.save = function () {
        $scope.$broadcast('form-submit');
        if ($scope.theForm.$valid) {
            $scope.theForm.$setPristine();
            $mdDialog.hide(vm.dataKey);
        }
    };
}