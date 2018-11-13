import './legend.scss';

/* eslint-disable import/no-unresolved, import/default */

import legendTemplate from './legend.tpl.html';

/* eslint-enable import/no-unresolved, import/default */


export default angular.module('thingsboard.directives.legend', [])
    .directive('tbLegend', Legend)
    .name;

/*@ngInject*/
function Legend($compile, $templateCache, types) {

    var linker = function (scope, element) {
        var template = $templateCache.get(legendTemplate);
        element.html(template);

        scope.displayHeader = function() {
            return scope.legendConfig.showMin === true ||
                   scope.legendConfig.showMax === true ||
                   scope.legendConfig.showAvg === true ||
                   scope.legendConfig.showTotal === true;
        }

        scope.isHorizontal = scope.legendConfig.position === types.position.bottom.value ||
            scope.legendConfig.position === types.position.top.value;

        scope.toggleHideData = function(index) {
            scope.legendData.keys[index].dataKey.hidden = !scope.legendData.keys[index].dataKey.hidden;
        }

        $compile(element.contents())(scope);

    }

    /*    scope.legendData = {
     keys: [],
     data: []

     key: {
       dataKey: dataKey,
       dataIndex: 0
     }
     data: {
       min: null,
       max: null,
       avg: null,
       total: null
     }
     };*/

    return {
        restrict: "E",
        link: linker,
        scope: {
            legendConfig: '=',
            legendData: '='
        }
    };
}
