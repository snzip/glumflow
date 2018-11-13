const SNAKE_CASE_REGEXP = /[A-Z]/g;

/*@ngInject*/
export default function RuleNodeDefinedConfigDirective($compile) {

    var linker = function (scope, element, attrs, ngModelCtrl) {

        attrs.$observe('ruleNodeDirective', function() {
            loadTemplate();
        });

        scope.$watch('configuration', function (newVal, prevVal) {
            if (!angular.equals(newVal, prevVal)) {
                ngModelCtrl.$setViewValue(scope.configuration);
            }
        });

        ngModelCtrl.$render = function () {
            scope.configuration = ngModelCtrl.$viewValue;
        };

        function loadTemplate() {
            if (scope.ruleNodeConfigScope) {
                scope.ruleNodeConfigScope.$destroy();
            }
            var directive = snake_case(attrs.ruleNodeDirective, '-');
            var template = `<${directive} rule-node-id="ruleNodeId" ng-model="configuration" ng-required="required" ng-readonly="readonly"></${directive}>`;
            element.html(template);
            scope.ruleNodeConfigScope = scope.$new();
            $compile(element.contents())(scope.ruleNodeConfigScope);
        }

        function snake_case(name, separator) {
            separator = separator || '_';
            return name.replace(SNAKE_CASE_REGEXP, function(letter, pos) {
                return (pos ? separator : '') + letter.toLowerCase();
            });
        }
    };

    return {
        restrict: "E",
        require: "^ngModel",
        scope: {
            ruleNodeId:'=',
            required:'=ngRequired',
            readonly:'=ngReadonly'
        },
        link: linker
    };

}
