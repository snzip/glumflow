import './relation-type-autocomplete.scss';

/* eslint-disable import/no-unresolved, import/default */

import relationTypeAutocompleteTemplate from './relation-type-autocomplete.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function RelationTypeAutocomplete($compile, $templateCache, $q, $filter, assetService, deviceService, types) {

    var linker = function (scope, element, attrs, ngModelCtrl) {
        var template = $templateCache.get(relationTypeAutocompleteTemplate);
        element.html(template);

        scope.tbRequired = angular.isDefined(scope.tbRequired) ? scope.tbRequired : false;
        scope.hideLabel = angular.isDefined(attrs.hideLabel) ? true : false;

        scope.relationType = null;
        scope.relationTypeSearchText = '';
        scope.relationTypes = [];
        for (var type in types.entityRelationType) {
            scope.relationTypes.push(types.entityRelationType[type]);
        }

        scope.fetchRelationTypes = function(searchText) {
            var deferred = $q.defer();
            var result = $filter('filter')(scope.relationTypes, {'$': searchText});
            if (result && result.length) {
                deferred.resolve(result);
            } else {
                deferred.resolve([searchText]);
            }
            return deferred.promise;
        }

        scope.relationTypeSearchTextChanged = function() {
        }

        scope.updateView = function () {
            if (!scope.disabled) {
                ngModelCtrl.$setViewValue(scope.relationType);
            }
        }

        ngModelCtrl.$render = function () {
            scope.relationType = ngModelCtrl.$viewValue;
        }

        scope.$watch('relationType', function (newValue, prevValue) {
            if (!angular.equals(newValue, prevValue)) {
                scope.updateView();
            }
        });

        scope.$watch('disabled', function () {
            scope.updateView();
        });

        $compile(element.contents())(scope);
    }

    return {
        restrict: "E",
        require: "^ngModel",
        link: linker,
        scope: {
            theForm: '=?',
            tbRequired: '=?',
            disabled:'=ngDisabled'
        }
    };
}
