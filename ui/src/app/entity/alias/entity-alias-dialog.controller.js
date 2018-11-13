import './entity-alias-dialog.scss';

/*@ngInject*/
export default function EntityAliasDialogController($scope, $mdDialog, $q, $filter, utils, entityService, types, isAdd, allowedEntityTypes, entityAliases, alias) {

    var vm = this;

    vm.types = types;
    vm.isAdd = isAdd;
    vm.allowedEntityTypes = allowedEntityTypes;
    if (angular.isArray(entityAliases)) {
        vm.entityAliases = entityAliases;
    } else {
        vm.entityAliases = [];
        for (var aliasId in entityAliases) {
            vm.entityAliases.push(entityAliases[aliasId]);
        }
    }
    if (vm.isAdd && !alias) {
        vm.alias = {
            alias: '',
            filter: {
                resolveMultiple: false
            }
        };
    } else {
        vm.alias = alias;
    }

    vm.cancel = cancel;
    vm.save = save;

    $scope.$watch('vm.alias.alias', function (newAlias) {
        if (newAlias) {
            var valid = true;
            var result = $filter('filter')(vm.entityAliases, {alias: newAlias}, true);
            if (result && result.length) {
                if (vm.isAdd || vm.alias.id != result[0].id) {
                    valid = false;
                }
            }
            $scope.theForm.aliasName.$setValidity('duplicateAliasName', valid);
        }
    });

    $scope.$watch('theForm.$pristine', function() {
        if ($scope.theForm && !$scope.theForm.$pristine) {
            $scope.theForm.$setValidity('entityFilter', true);
        }
    });

    function validate() {
        var deferred = $q.defer();
        var validationResult = {
            entity: null,
            stateEntity: false
        }
        entityService.resolveAliasFilter(vm.alias.filter, null, 1, true).then(
            function success(result) {
                validationResult.stateEntity = result.stateEntity;
                var entities = result.entities;
                if (entities.length) {
                    validationResult.entity = entities[0];
                }
                deferred.resolve(validationResult);
            },
            function fail() {
                deferred.reject();
            }
        );
        return deferred.promise;
    }

    function cancel() {
        $mdDialog.cancel();
    }

    function save() {
        $scope.theForm.$setPristine();
        validate().then(
            function success() {
                if (vm.isAdd) {
                    vm.alias.id = utils.guid();
                }
                $mdDialog.hide(vm.alias);
            },
            function fail() {
                $scope.theForm.$setValidity('entityFilter', false);
            }
        )
    }

}
