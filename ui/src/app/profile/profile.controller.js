/* eslint-disable import/no-unresolved, import/default */

import changePasswordTemplate from './change-password.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function ProfileController(userService, $scope, $document, $mdDialog, $translate) {
    var vm = this;

    vm.profileUser = {};
    vm.save = save;
    vm.changePassword = changePassword;
    vm.languageList = SUPPORTED_LANGS; //eslint-disable-line

    loadProfile();

    function loadProfile() {
        userService.getUser(userService.getCurrentUser().userId).then(function success(user) {
            vm.profileUser = user;
            if (!vm.profileUser.additionalInfo) {
                vm.profileUser.additionalInfo = {};
            }
            if (!vm.profileUser.additionalInfo.lang) {
                vm.profileUser.additionalInfo.lang = $translate.use();
            }
        });
    }

    function save() {
        userService.saveUser(vm.profileUser).then(function success(user) {
            $translate.use(vm.profileUser.additionalInfo.lang);
            vm.profileUser = user;
            $scope.theForm.$setPristine();
        });
    }

    function changePassword($event) {
        $mdDialog.show({
            controller: 'ChangePasswordController',
            controllerAs: 'vm',
            templateUrl: changePasswordTemplate,
            parent: angular.element($document[0].body),
            fullscreen: true,
            targetEvent: $event
        }).then(function () {
        }, function () {
        });
    }
}
