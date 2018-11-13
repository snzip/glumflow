/*@ngInject*/
export default function ManageDeviceCredentialsController(deviceService, $scope, $mdDialog, deviceId, isReadOnly) {

    var vm = this;

    vm.credentialsTypes = [
        {
            name: 'Access token',
            value: 'ACCESS_TOKEN'
        },
        {
            name: 'X.509 Certificate',
            value: 'X509_CERTIFICATE'
        }
    ];

    vm.deviceCredentials = {};
    vm.isReadOnly = isReadOnly;

    vm.valid = valid;
    vm.cancel = cancel;
    vm.save = save;
    vm.clear = clear;

    loadDeviceCredentials();

    function loadDeviceCredentials() {
        deviceService.getDeviceCredentials(deviceId).then(function success(deviceCredentials) {
            vm.deviceCredentials = deviceCredentials;
        });
    }

    function cancel() {
        $mdDialog.cancel();
    }

    function valid() {
        return vm.deviceCredentials &&
               (vm.deviceCredentials.credentialsType === 'ACCESS_TOKEN'
                   && vm.deviceCredentials.credentialsId
                   && vm.deviceCredentials.credentialsId.length > 0
                   || vm.deviceCredentials.credentialsType === 'X509_CERTIFICATE'
                   && vm.deviceCredentials.credentialsValue
                   && vm.deviceCredentials.credentialsValue.length > 0);
    }

    function clear() {
        vm.deviceCredentials.credentialsId = null;
        vm.deviceCredentials.credentialsValue = null;
    }

    function save() {
        deviceService.saveDeviceCredentials(vm.deviceCredentials).then(function success(deviceCredentials) {
            vm.deviceCredentials = deviceCredentials;
            $scope.theForm.$setPristine();
            $mdDialog.hide();
        });
    }
}
