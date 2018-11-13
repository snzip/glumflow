/* eslint-disable import/no-unresolved, import/default */

import customerFieldsetTemplate from './customer-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function CustomerDirective($compile, $templateCache, $translate, toast) {
    var linker = function (scope, element) {
        var template = $templateCache.get(customerFieldsetTemplate);
        element.html(template);

        scope.isPublic = false;

        scope.onCustomerIdCopied = function() {
            toast.showSuccess($translate.instant('customer.idCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        scope.$watch('customer', function(newVal) {
            if (newVal) {
                if (scope.customer.additionalInfo) {
                    scope.isPublic = scope.customer.additionalInfo.isPublic;
                } else {
                    scope.isPublic = false;
                }
            }
        });

        $compile(element.contents())(scope);

    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            customer: '=',
            isEdit: '=',
            theForm: '=',
            onManageUsers: '&',
            onManageAssets: '&',
            onManageDevices: '&',
            onManageDashboards: '&',
            onDeleteCustomer: '&'
        }
    };
}
