import './material-icons-dialog.scss';

/*@ngInject*/
export default function MaterialIconsDialogController($scope, $mdDialog, $timeout, utils, icon) {

    var vm = this;

    vm.selectedIcon = icon;

    vm.showAll = false;
    vm.loadingIcons = false;

    $scope.$watch('vm.showAll', function(showAll) {
        if (showAll) {
            vm.loadingIcons = true;
            $timeout(function() {
                utils.getMaterialIcons().then(
                    function success(icons) {
                        vm.icons = icons;
                    }
                );
            });
        } else {
            vm.icons = utils.getCommonMaterialIcons();
        }
    });

    $scope.$on('iconsLoadFinished', function() {
        vm.loadingIcons = false;
    });

    vm.cancel = cancel;
    vm.selectIcon = selectIcon;

    function cancel() {
        $mdDialog.cancel();
    }

    function selectIcon($event, icon) {
        vm.selectedIcon = icon;
        $mdDialog.hide(vm.selectedIcon);
    }
}
