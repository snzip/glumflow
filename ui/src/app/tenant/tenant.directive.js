/* eslint-disable import/no-unresolved, import/default */

import tenantFieldsetTemplate from './tenant-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function TenantDirective($compile, $templateCache, $translate, toast) {
    var linker = function (scope, element) {
        var template = $templateCache.get(tenantFieldsetTemplate);
        element.html(template);

        scope.onTenantIdCopied = function() {
            toast.showSuccess($translate.instant('tenant.idCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            tenant: '=',
            isEdit: '=',
            theForm: '=',
            onManageUsers: '&',
            onDeleteTenant: '&'
        }
    };
}
