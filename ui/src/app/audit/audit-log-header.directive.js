/* eslint-disable import/no-unresolved, import/default */

import auditLogHeaderTemplate from './audit-log-header.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AuditLogHeaderDirective($compile, $templateCache, types) {

    var linker = function (scope, element, attrs) {

        var template = $templateCache.get(auditLogHeaderTemplate);
        element.html(template);
        scope.auditLogMode = attrs.auditLogMode;
        scope.types = types;
        $compile(element.contents())(scope);

    };

    return {
        restrict: "A",
        replace: false,
        link: linker,
        scope: false
    };
}
