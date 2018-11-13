/* eslint-disable import/no-unresolved, import/default */

import widgetsBundleFieldsetTemplate from './widgets-bundle-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function WidgetsBundleDirective($compile, $templateCache) {
    var linker = function (scope, element) {
        var template = $templateCache.get(widgetsBundleFieldsetTemplate);
        element.html(template);
        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            widgetsBundle: '=',
            isEdit: '=',
            isReadOnly: '=',
            theForm: '=',
            onExportWidgetsBundle: '&',
            onDeleteWidgetsBundle: '&'
        }
    };
}
