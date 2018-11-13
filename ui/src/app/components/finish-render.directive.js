export default angular.module('thingsboard.directives.finishRender', [])
    .directive('tbOnFinishRender', OnFinishRender)
    .name;

/*@ngInject*/
function OnFinishRender($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit(attr.tbOnFinishRender);
                });
            }
        }
    };
}
