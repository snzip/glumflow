/*@ngInject*/
export default function ActivationLinkDialogController($mdDialog, $translate, toast, activationLink) {

    var vm = this;

    vm.activationLink = activationLink;

    vm.onActivationLinkCopied = onActivationLinkCopied;
    vm.close = close;

    function onActivationLinkCopied(){
        toast.showSuccess($translate.instant('user.activation-link-copied-message'), 750, angular.element('#activation-link-dialog-content'), 'bottom left');
    }

    function close() {
        $mdDialog.hide();
    }

}