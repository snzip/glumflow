/*@ngInject*/
export default function ResetPasswordRequestController($translate, toast, loginService) {
    var vm = this;

    vm.email = '';

    vm.sendResetPasswordLink = sendResetPasswordLink;

    function sendResetPasswordLink() {
        loginService.sendResetPasswordLink(vm.email).then(function success() {
            toast.showSuccess($translate.instant('login.password-link-sent-message'));
        }, function fail() {
        });
    }
}
