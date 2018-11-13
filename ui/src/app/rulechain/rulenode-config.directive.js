/* eslint-disable import/no-unresolved, import/default */

import ruleNodeConfigTemplate from './rulenode-config.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function RuleNodeConfigDirective($compile, $templateCache, $injector, $translate) {

    var linker = function (scope, element, attrs, ngModelCtrl) {
        var template = $templateCache.get(ruleNodeConfigTemplate);
        element.html(template);

        scope.$watch('configuration', function (newVal, prevVal) {
            if (!angular.equals(newVal, prevVal)) {
                ngModelCtrl.$setViewValue(scope.configuration);
            }
        });

        ngModelCtrl.$render = function () {
            scope.configuration = ngModelCtrl.$viewValue;
        };

        scope.useDefinedDirective = function() {
            return scope.nodeDefinition &&
                scope.nodeDefinition.configDirective && !scope.definedDirectiveError;
        };

        scope.$watch('nodeDefinition', () => {
            if (scope.nodeDefinition) {
                validateDefinedDirective();
            }
        });

        function validateDefinedDirective() {
            if (scope.nodeDefinition.uiResourceLoadError && scope.nodeDefinition.uiResourceLoadError.length) {
                scope.definedDirectiveError = scope.nodeDefinition.uiResourceLoadError;
            } else {
                var definedDirective = scope.nodeDefinition.configDirective;
                if (definedDirective && definedDirective.length) {
                    if (!$injector.has(definedDirective + 'Directive')) {
                        scope.definedDirectiveError = $translate.instant('rulenode.directive-is-not-loaded', {directiveName: definedDirective});
                    }
                }
            }
        }

        $compile(element.contents())(scope);
    };

    return {
        restrict: "E",
        require: "^ngModel",
        scope: {
            ruleNodeId:'=',
            nodeDefinition:'=',
            required:'=ngRequired',
            readonly:'=ngReadonly'
        },
        link: linker
    };

}
