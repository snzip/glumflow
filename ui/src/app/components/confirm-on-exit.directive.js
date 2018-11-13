export default angular.module('thingsboard.directives.confirmOnExit', [])
    .directive('tbConfirmOnExit', ConfirmOnExit)
    .name;

/*@ngInject*/
function ConfirmOnExit($state, $mdDialog, $window, $filter, $parse, userService) {
    return {
        link: function ($scope, $element, $attributes) {
            $scope.confirmForm = $scope.$eval($attributes.confirmForm);
            $window.onbeforeunload = function () {
                if (userService.isAuthenticated() && (($scope.confirmForm && $scope.confirmForm.$dirty) || $scope.$eval($attributes.isDirty))) {
                    return $filter('translate')('confirm-on-exit.message');
                }
            }
            $scope.$on('$stateChangeStart', function (event, next, current, params) {
                if (userService.isAuthenticated() && (($scope.confirmForm && $scope.confirmForm.$dirty) || $scope.$eval($attributes.isDirty))) {
                    event.preventDefault();
                    var confirm = $mdDialog.confirm()
                        .title($filter('translate')('confirm-on-exit.title'))
                        .htmlContent($filter('translate')('confirm-on-exit.html-message'))
                        .ariaLabel($filter('translate')('confirm-on-exit.title'))
                        .cancel($filter('translate')('action.cancel'))
                        .ok($filter('translate')('action.ok'));
                    $mdDialog.show(confirm).then(function () {
                        if ($scope.confirmForm) {
                            $scope.confirmForm.$setPristine();
                        } else {
                            var remoteSetter = $parse($attributes.isDirty).assign;
                            remoteSetter($scope, false);
                            //$scope.isDirty = false;
                        }
                        $state.go(next.name, params);
                    }, function () {
                    });
                }
            });
        },
        scope: false
    };
}