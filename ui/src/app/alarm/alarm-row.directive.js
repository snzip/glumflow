/* eslint-disable import/no-unresolved, import/default */

import alarmDetailsDialogTemplate from './alarm-details-dialog.tpl.html';

import alarmRowTemplate from './alarm-row.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AlarmRowDirective($compile, $templateCache, types, $mdDialog, $document) {

    var linker = function (scope, element, attrs) {

        var template = $templateCache.get(alarmRowTemplate);
        element.html(template);

        scope.alarm = attrs.alarm;
        scope.types = types;

        scope.showAlarmDetails = function($event) {
            var onShowingCallback = {
                onShowing: function(){}
            }
            $mdDialog.show({
                controller: 'AlarmDetailsDialogController',
                controllerAs: 'vm',
                templateUrl: alarmDetailsDialogTemplate,
                locals: {
                    alarmId: scope.alarm.id.id,
                    allowAcknowledgment: true,
                    allowClear: true,
                    displayDetails: true,
                    showingCallback: onShowingCallback
                },
                parent: angular.element($document[0].body),
                targetEvent: $event,
                fullscreen: true,
                multiple: true,
                onShowing: function(scope, element) {
                    onShowingCallback.onShowing(scope, element);
                }
            }).then(function (alarm) {
                if (alarm) {
                    scope.alarm = alarm;
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
