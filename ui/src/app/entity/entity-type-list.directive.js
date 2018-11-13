/* eslint-disable import/no-unresolved, import/default */

import entityTypeListTemplate from './entity-type-list.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

import './entity-type-list.scss';

/*@ngInject*/
export default function EntityTypeListDirective($compile, $templateCache, $q, $mdUtil, $translate, $filter, types, entityService) {

    var linker = function (scope, element, attrs, ngModelCtrl) {

        var template = $templateCache.get(entityTypeListTemplate);
        element.html(template);

        scope.ngModelCtrl = ngModelCtrl;

        scope.placeholder = scope.tbRequired ? $translate.instant('entity.enter-entity-type')
                                : $translate.instant('entity.any-entity');
        scope.secondaryPlaceholder = '+' + $translate.instant('entity.entity-type');

        var entityTypes;

        if (scope.ignoreAuthorityFilter && scope.allowedEntityTypes
            && scope.allowedEntityTypes.length) {
            entityTypes = {};
            scope.allowedEntityTypes.forEach((entityTypeValue) => {
                var entityType = entityTypeFromValue(entityTypeValue);
                if (entityType) {
                    entityTypes[entityType] = entityTypeValue;
                }
            });
        } else {
            entityTypes = entityService.prepareAllowedEntityTypesList(scope.allowedEntityTypes);
        }

        function entityTypeFromValue(entityTypeValue) {
            for (var entityType in types.entityType) {
                if (types.entityType[entityType] === entityTypeValue) {
                    return entityType;
                }
            }
            return null;
        }

        scope.entityTypesList = [];
        for (var type in entityTypes) {
            var entityTypeInfo = {};
            entityTypeInfo.value = entityTypes[type];
            entityTypeInfo.name = $translate.instant(types.entityTypeTranslations[entityTypeInfo.value].type) + '';
            scope.entityTypesList.push(entityTypeInfo);
        }

        scope.$watch('tbRequired', function () {
            scope.updateValidity();
        });

        scope.fetchEntityTypes = function(searchText) {
            var deferred = $q.defer();
            var entityTypes = $filter('filter')(scope.entityTypesList, {name: searchText});
            deferred.resolve(entityTypes);
            return deferred.promise;
        }

        scope.updateValidity = function() {
            var value = ngModelCtrl.$viewValue;
            var valid = !scope.tbRequired || value && value.length > 0;
            ngModelCtrl.$setValidity('entityTypeList', valid);
        }

        ngModelCtrl.$render = function () {
            if (scope.entityTypeListWatch) {
                scope.entityTypeListWatch();
                scope.entityTypeListWatch = null;
            }
            var entityTypeList = [];
            var value = ngModelCtrl.$viewValue;
            if (value && value.length) {
                value.forEach(function(type) {
                    var entityTypeInfo = {};
                    entityTypeInfo.value = type;
                    entityTypeInfo.name = $translate.instant(types.entityTypeTranslations[entityTypeInfo.value].type) + '';
                    entityTypeList.push(entityTypeInfo);
                });
            }
            scope.entityTypeList = entityTypeList;
            scope.entityTypeListWatch = scope.$watch('entityTypeList', function (newVal, prevVal) {
                if (!angular.equals(newVal, prevVal)) {
                    updateEntityTypeList();
                }
            }, true);
        }

        function updateEntityTypeList() {
            var values = ngModelCtrl.$viewValue;
            if (!values) {
                values = [];
                ngModelCtrl.$setViewValue(values);
            } else {
                values.length = 0;
            }
            if (scope.entityTypeList && scope.entityTypeList.length) {
                scope.entityTypeList.forEach(function (entityType) {
                    values.push(entityType.value);
                });
            }
            scope.updateValidity();
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
            allowedEntityTypes: '=?',
            ignoreAuthorityFilter: '=?'
        }
    };

}
