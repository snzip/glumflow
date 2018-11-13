import $ from 'jquery';

export default angular.module('thingsboard.directives.circularProgress', [])
    .directive('tbCircularProgress', CircularProgress)
    .name;

/* eslint-disable angular/angularelement */

/*@ngInject*/
function CircularProgress($compile) {

    var linker = function (scope, element) {

        var circularProgressElement = angular.element('<md-progress-circular style="margin: auto;" md-mode="indeterminate" md-diameter="20"></md-progress-circular>');

        $compile(circularProgressElement)(scope);

        var children = null;
        var cssWidth = element.prop('style')['width'];
        var width = null;
        if (!cssWidth) {
            $(element).css('width', width + 'px');
        }

        scope.$watch('circularProgress', function (newCircularProgress, prevCircularProgress) {
            if (newCircularProgress != prevCircularProgress) {
                if (newCircularProgress) {
                    if (!cssWidth) {
                        $(element).css('width', '');
                        width = element.prop('offsetWidth');
                        $(element).css('width', width + 'px');
                    }
                    children = $(element).children();
                    $(element).empty();
                    $(element).append($(circularProgressElement));
                } else {
                    $(element).empty();
                    $(element).append(children);
                    if (cssWidth) {
                        $(element).css('width', cssWidth);
                    } else {
                        $(element).css('width', '');
                    }
                }
            }
        });

    }

    return {
        restrict: "A",
        link: linker,
        scope: {
            circularProgress: "=tbCircularProgress"
        }
    };
}

/* eslint-enable angular/angularelement */
