/* eslint-disable import/no-unresolved, import/default */

import auditLogDetailsDialogTemplate from './audit-log-details-dialog.tpl.html';

import auditLogRowTemplate from './audit-log-row.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AuditLogRowDirective($compile, $templateCache, types, $mdDialog, $document) {

    var linker = function (scope, element, attrs) {

        var template = $templateCache.get(auditLogRowTemplate);
        element.html(template);

        scope.auditLog = attrs.auditLog;
        scope.auditLogMode = attrs.auditLogMode;
        scope.types = types;

        scope.showAuditLogDetails = function($event) {
            var onShowingCallback = {
                onShowing: function(){}
            }
            $mdDialog.show({
                controller: 'AuditLogDetailsDialogController',
                controllerAs: 'vm',
                templateUrl: auditLogDetailsDialogTemplate,
                locals: {
                    auditLog: scope.auditLog,
                    showingCallback: onShowingCallback
                },
                parent: angular.element($document[0].body),
                targetEvent: $event,
                fullscreen: true,
                multiple: true,
                onShowing: function(scope, element) {
                    onShowingCallback.onShowing(scope, element);
                }
            });
        }

        $compile(element.contents())(scope);
    }

    return {
        restrict: "A",
        replace: false,
        link: linker,
        scope: false
    };
}
