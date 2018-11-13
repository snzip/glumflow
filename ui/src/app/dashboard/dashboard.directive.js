/* eslint-disable import/no-unresolved, import/default */

import dashboardFieldsetTemplate from './dashboard-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function DashboardDirective($compile, $templateCache, $translate, types, toast, dashboardService) {
    var linker = function (scope, element) {
        var template = $templateCache.get(dashboardFieldsetTemplate);
        element.html(template);
        scope.publicLink = null;
        scope.$watch('dashboard', function(newVal) {
            if (newVal) {
                if (scope.dashboard.publicCustomerId) {
                    scope.publicLink = dashboardService.getPublicDashboardLink(scope.dashboard);
                } else {
                    scope.publicLink = null;
                }
            }
        });

        scope.onPublicLinkCopied = function() {
            toast.showSuccess($translate.instant('dashboard.public-link-copied-message'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            dashboard: '=',
            isEdit: '=',
            customerId: '=',
            dashboardScope: '=',
            theForm: '=',
            onMakePublic: '&',
            onMakePrivate: '&',
            onManageAssignedCustomers: '&',
            onUnassignFromCustomer: '&',
            onExportDashboard: '&',
            onDeleteDashboard: '&'
        }
    };
}
