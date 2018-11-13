/*@ngInject*/
export default function SelectWidgetTypeController($mdDialog, types) {

    var vm = this;

    vm.types = types;

    vm.cancel = cancel;
    vm.typeSelected = typeSelected;

    function cancel() {
        $mdDialog.cancel();
    }

    function typeSelected(widgetType) {
        $mdDialog.hide(widgetType);
    }

}
