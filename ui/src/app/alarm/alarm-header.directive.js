/* eslint-disable import/no-unresolved, import/default */

import alarmHeaderTemplate from './alarm-header.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AlarmHeaderDirective($compile, $templateCache) {

    var linker = function (scope, element) {

        var template = $templateCache.get(alarmHeaderTemplate);
        element.html(template);
        $compile(element.contents())(scope);

    }

    return {
        restrict: "A",
        replace: false,
        link: linker,
        scope: false
    };
}
