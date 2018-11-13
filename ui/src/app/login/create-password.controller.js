/*@ngInject*/
export default function CreatePasswordController($stateParams, $translate, toast, loginService, userService) {
    var vm = this;

    vm.password = '';
    vm.password2 = '';

    vm.createPassword = createPassword;

    function createPassword() {
        if (vm.password !== vm.password2) {
            toast.showError($translate.instant('login.passwords-mismatch-error'));
        } else {
            loginService.activate($stateParams.activateToken, vm.password).then(function success(response) {
                var token = response.data.token;
                var refreshToken = response.data.refreshToken;
                userService.setUserFromJwtToken(token, refreshToken, true);
            }, function fail() {
            });
        }
    }
}
