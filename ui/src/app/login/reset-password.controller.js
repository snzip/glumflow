/*@ngInject*/
export default function ResetPasswordController($stateParams, $translate, toast, loginService, userService) {
    var vm = this;

    vm.newPassword = '';
    vm.newPassword2 = '';

    vm.resetPassword = resetPassword;

    function resetPassword() {
        if (vm.newPassword !== vm.newPassword2) {
            toast.showError($translate.instant('login.passwords-mismatch-error'));
        } else {
            loginService.resetPassword($stateParams.resetToken, vm.newPassword).then(function success(response) {
                var token = response.data.token;
                var refreshToken = response.data.refreshToken;
                userService.setUserFromJwtToken(token, refreshToken, true);
            }, function fail() {
            });
        }
    }
}
