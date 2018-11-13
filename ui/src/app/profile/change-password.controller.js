/*@ngInject*/
export default function ChangePasswordController($scope, $translate, toast, $mdDialog, loginService) {
    var vm = this;

    vm.currentPassword = '';
    vm.newPassword = '';
    vm.newPassword2 = '';

    vm.cancel = cancel;
    vm.changePassword = changePassword;

    function cancel() {
        $mdDialog.cancel();
    }

    function changePassword() {
        if (vm.newPassword !== vm.newPassword2) {
            toast.showError($translate.instant('login.passwords-mismatch-error'));
        } else {
            loginService.changePassword(vm.currentPassword, vm.newPassword).then(function success() {
                $scope.theForm.$setPristine();
                $mdDialog.hide();
            });
        }
    }
}
