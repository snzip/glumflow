/* eslint-disable import/no-unresolved, import/default */

import dashboardSettingsTemplate from '../dashboard-settings.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function ManageDashboardLayoutsController($scope, $mdDialog, $document, dashboardUtils, layouts) {

    var vm = this;

    vm.openLayoutSettings = openLayoutSettings;
    vm.cancel = cancel;
    vm.save = save;

    vm.layouts = layouts;
    vm.displayLayouts = {
        main: angular.isDefined(vm.layouts.main),
        right: angular.isDefined(vm.layouts.right)
    }

    for (var l in vm.displayLayouts) {
        if (!vm.layouts[l]) {
            vm.layouts[l] = dashboardUtils.createDefaultLayoutData();
        }
    }

    function openLayoutSettings($event, layoutId) {
        var gridSettings = angular.copy(vm.layouts[layoutId].gridSettings);
        $mdDialog.show({
            controller: 'DashboardSettingsController',
            controllerAs: 'vm',
            templateUrl: dashboardSettingsTemplate,
            locals: {
                settings: null,
                gridSettings: gridSettings
            },
            parent: angular.element($document[0].body),
            multiple: true,
            fullscreen: true,
            targetEvent: $event
        }).then(function (data) {
            var gridSettings = data.gridSettings;
            if (gridSettings) {
                dashboardUtils.updateLayoutSettings(vm.layouts[layoutId], gridSettings);
            }
            $scope.theForm.$setDirty();
        }, function () {
        });
    }

    function cancel() {
        $mdDialog.cancel();
    }

    function save() {
        $scope.theForm.$setPristine();
        for (var l in vm.displayLayouts) {
            if (!vm.displayLayouts[l]) {
                if (vm.layouts[l]) {
                    delete vm.layouts[l];
                }
            }
        }
        $mdDialog.hide(vm.layouts);
    }
}
