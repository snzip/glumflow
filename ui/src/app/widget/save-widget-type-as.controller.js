/*@ngInject*/
export default function SaveWidgetTypeAsController($mdDialog, $q, userService) {

    var vm = this;

    vm.saveAs = saveAs;
    vm.cancel = cancel;

    vm.widgetName;
    vm.widgetsBundle;

    vm.bundlesScope = 'system';

    if (userService.getAuthority() === 'TENANT_ADMIN') {
        vm.bundlesScope = 'tenant';
    }

    function cancel () {
        $mdDialog.cancel();
    }

    function saveAs () {
        $mdDialog.hide({widgetName: vm.widgetName, bundleId: vm.widgetsBundle.id.id, bundleAlias: vm.widgetsBundle.alias});
    }

}
