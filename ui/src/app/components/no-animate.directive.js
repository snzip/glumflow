import ngAnimate from 'angular-animate';

export default angular.module('thingsboard.directives.noAnimate', [ngAnimate])
    .directive('tbNoAnimate', NoAnimate)
    .name;

/*@ngInject*/
function NoAnimate($animate) {
    return {
        restrict: 'A',
        link: function (scope, element) {
            $animate.enabled(element, false);
            scope.$watch(function () {
                $animate.enabled(element, false)
            });
        }
    };
}
