import './entity-type-select.scss';

/* eslint-disable import/no-unresolved, import/default */

import entityTypeSelectTemplate from './entity-type-select.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function EntityTypeSelect($compile, $templateCache, utils, entityService, userService, types) {

    var linker = function (scope, element, attrs, ngModelCtrl) {
        var template = $templateCache.get(entityTypeSelectTemplate);
        element.html(template);

        scope.tbRequired = angular.isDefined(scope.tbRequired) ? scope.tbRequired : false;

        if (angular.isDefined(attrs.hideLabel)) {
            scope.showLabel = false;
        } else {
            scope.showLabel = true;
        }

        scope.ngModelCtrl = ngModelCtrl;

        scope.entityTypes = entityService.prepareAllowedEntityTypesList(scope.allowedEntityTypes, scope.useAliasEntityTypes);

        scope.typeName = function(type) {
            return type ? types.entityTypeTranslations[type].type : '';
        }

        scope.updateValidity = function () {
            var value = ngModelCtrl.$viewValue;
            var valid = angular.isDefined(value) && value != null;
            ngModelCtrl.$setValidity('entityType', valid);
        };

        scope.$watch('entityType', function (newValue, prevValue) {
            if (!angular.equals(newValue, prevValue)) {
                scope.updateView();
            }
        });

        scope.updateView = function () {
            ngModelCtrl.$setViewValue(scope.entityType);
            scope.updateValidity();
        };

        ngModelCtrl.$render = function () {
            if (ngModelCtrl.$viewValue) {
                scope.entityType = ngModelCtrl.$viewValue;
            }
        };

        $compile(element.contents())(scope);
    }

    return {
        restrict: "E",
        require: "^ngModel",
        link: linker,
        scope: {
            theForm: '=?',
            tbRequired: '=?',
            disabled:'=ngDisabled',
            allowedEntityTypes: "=?",
            useAliasEntityTypes: "=?"
        }
    };
}
