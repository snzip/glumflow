/*@ngInject*/
export default function AdminController(adminService, toast, $scope, $rootScope, $state, $translate) {

    var vm = this;
    vm.save = save;
    vm.sendTestMail = sendTestMail;
    vm.smtpProtocols = ('smtp smtps').split(' ').map(function (protocol) {
        return protocol;
    });

    $translate('admin.test-mail-sent').then(function (translation) {
        vm.testMailSent = translation;
    }, function (translationId) {
        vm.testMailSent = translationId;
    });


    loadSettings();

    function loadSettings() {
        adminService.getAdminSettings($state.$current.data.key).then(function success(settings) {
            vm.settings = settings;
        });
    }

    function save() {
        adminService.saveAdminSettings(vm.settings).then(function success(settings) {
            vm.settings = settings;
            vm.settingsForm.$setPristine();
        });
    }

    function sendTestMail() {
        adminService.sendTestMail(vm.settings).then(function success() {
            toast.showSuccess($translate.instant('admin.test-mail-sent'));
        });
    }

}
