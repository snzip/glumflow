/* eslint-disable import/no-unresolved, import/default */

import entitySubtypeListTemplate from './entity-subtype-list.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

import './entity-subtype-list.scss';

/*@ngInject*/
export default function EntitySubtypeListDirective($compile, $templateCache, $q, $mdUtil, $translate, $filter, types, assetService, deviceService, entityViewService) {

    var linker = function (scope, element, attrs, ngModelCtrl) {

        var template = $templateCache.get(entitySubtypeListTemplate);
        element.html(template);

        scope.ngModelCtrl = ngModelCtrl;


        scope.entitySubtypesList = [];
        scope.entitySubtypes = null;

        if (scope.entityType == types.entityType.asset) {
            scope.placeholder = scope.tbRequired ? $translate.instant('asset.enter-asset-type')
                : $translate.instant('asset.any-asset');
            scope.secondaryPlaceholder = '+' + $translate.instant('asset.asset-type');
            scope.noSubtypesMathingText = 'asset.no-asset-types-matching';
            scope.subtypeListEmptyText = 'asset.asset-type-list-empty';
        } else if (scope.entityType == types.entityType.device) {
            scope.placeholder = scope.tbRequired ? $translate.instant('device.enter-device-type')
                : $translate.instant('device.any-device');
            scope.secondaryPlaceholder = '+' + $translate.instant('device.device-type');
            scope.noSubtypesMathingText = 'device.no-device-types-matching';
            scope.subtypeListEmptyText = 'device.device-type-list-empty';
        } else if (scope.entityType == types.entityType.entityView) {
            scope.placeholder = scope.tbRequired ? $translate.instant('entity-view.enter-entity-view-type')
                : $translate.instant('entity-view.any-entity-view');
            scope.secondaryPlaceholder = '+' + $translate.instant('entity-view.entity-view-type');
            scope.noSubtypesMathingText = 'entity-view.no-entity-view-types-matching';
            scope.subtypeListEmptyText = 'entity-view.entity-view-type-list-empty';
        }

        scope.$watch('tbRequired', function () {
            scope.updateValidity();
        });

        scope.fetchEntitySubtypes = function(searchText) {
            var deferred = $q.defer();
            loadSubTypes().then(
                function success(subTypes) {
                    var result = $filter('filter')(subTypes, {'$': searchText});
                    if (result && result.length) {
                        deferred.resolve(result);
                    } else {
                        deferred.resolve([searchText]);
                    }
                },
                function fail() {
                    deferred.reject();
                }
            );
            return deferred.promise;
        }

        scope.updateValidity = function() {
            var value = ngModelCtrl.$viewValue;
            var valid = !scope.tbRequired || value && value.length > 0;
            ngModelCtrl.$setValidity('entitySubtypesList', valid);
        }

        ngModelCtrl.$render = function () {
            scope.entitySubtypesList = ngModelCtrl.$viewValue;
            if (!scope.entitySubtypesList) {
                scope.entitySubtypesList = [];
            }
        }

        scope.$watch('entitySubtypesList', function () {
            ngModelCtrl.$setViewValue(scope.entitySubtypesList);
            scope.updateValidity();
        }, true);

        function loadSubTypes() {
            var deferred = $q.defer();
            if (!scope.entitySubtypes) {
                var entitySubtypesPromise;
                if (scope.entityType == types.entityType.asset) {
                    entitySubtypesPromise = assetService.getAssetTypes({ignoreLoading: true});
                } else if (scope.entityType == types.entityType.device) {
                    entitySubtypesPromise = deviceService.getDeviceTypes({ignoreLoading: true});
                } else if (scope.entityType == types.entityType.entityView) {
                    entitySubtypesPromise = entityViewService.getEntityViewTypes({ignoreLoading: true});
                }
                if (entitySubtypesPromise) {
                    entitySubtypesPromise.then(
                        function success(types) {
                            scope.entitySubtypes = [];
                            types.forEach(function (type) {
                                scope.entitySubtypes.push(type.type);
                            });
                            deferred.resolve(scope.entitySubtypes);
                        },
                        function fail() {
                            deferred.reject();
                        }
                    );
                } else {
                    deferred.reject();
                }
            } else {
                deferred.resolve(scope.entitySubtypes);
            }
            return deferred.promise;
        }

        $compile(element.contents())(scope);

        $mdUtil.nextTick(function(){
            var inputElement = angular.element('input', element);
            inputElement.on('blur', function() {
                scope.inputTouched = true;
            } );
        });

    }

    return {
        restrict: "E",
        require: "^ngModel",
        link: linker,
        scope: {
            disabled:'=ngDisabled',
            tbRequired: '=?',
            entityType: "="
        }
    };

}
