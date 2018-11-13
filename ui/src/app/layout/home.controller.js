import $ from 'jquery';

/* eslint-disable import/no-unresolved, import/default */

import logoSvg from '../../svg/logo_title_white.svg';

/* eslint-enable import/no-unresolved, import/default */

/* eslint-disable angular/angularelement */

/*@ngInject*/
export default function HomeController(types, loginService, userService, deviceService, Fullscreen, $scope, $element, $rootScope, $document, $state,
                                       $window, $log, $mdMedia, $animate, $timeout) {

    var siteSideNav = $('.tb-site-sidenav', $element);

    var vm = this;

    vm.Fullscreen = Fullscreen;
    vm.logoSvg = logoSvg;

    if (angular.isUndefined($rootScope.searchConfig)) {
        $rootScope.searchConfig = {
            searchEnabled: false,
            searchByEntitySubtype: false,
            searchEntityType: null,
            showSearch: false,
            searchText: "",
            searchEntitySubtype: ""
        };
    }

    vm.isShowSidenav = false;
    vm.isLockSidenav = false;

    vm.displaySearchMode = displaySearchMode;
    vm.displayEntitySubtypeSearch = displayEntitySubtypeSearch;
    vm.openSidenav = openSidenav;
    vm.goBack = goBack;
    vm.searchTextUpdated = searchTextUpdated;
    vm.sidenavClicked = sidenavClicked;
    vm.toggleFullscreen = toggleFullscreen;
    vm.openSearch = openSearch;
    vm.closeSearch = closeSearch;

    $scope.$on('$stateChangeSuccess', function (evt, to, toParams, from) {
        watchEntitySubtype(false);
        if (angular.isDefined(to.data.searchEnabled)) {
            $scope.searchConfig.searchEnabled = to.data.searchEnabled;
            $scope.searchConfig.searchByEntitySubtype = to.data.searchByEntitySubtype;
            $scope.searchConfig.searchEntityType = to.data.searchEntityType;
            if ($scope.searchConfig.searchEnabled === false || to.name !== from.name) {
                $scope.searchConfig.showSearch = false;
                $scope.searchConfig.searchText = "";
                $scope.searchConfig.searchEntitySubtype = "";
            }
        } else {
            $scope.searchConfig.searchEnabled = false;
            $scope.searchConfig.searchByEntitySubtype = false;
            $scope.searchConfig.searchEntityType = null;
            $scope.searchConfig.showSearch = false;
            $scope.searchConfig.searchText = "";
            $scope.searchConfig.searchEntitySubtype = "";
        }
        watchEntitySubtype($scope.searchConfig.searchByEntitySubtype);
    });

    vm.isGtSm = $mdMedia('gt-sm');
    if (vm.isGtSm) {
        vm.isLockSidenav = true;
        $animate.enabled(siteSideNav, false);
    }

    $scope.$watch(function() { return $mdMedia('gt-sm'); }, function(isGtSm) {
        vm.isGtSm = isGtSm;
        vm.isLockSidenav = isGtSm;
        vm.isShowSidenav = isGtSm;
        if (!isGtSm) {
            $timeout(function() {
                $animate.enabled(siteSideNav, true);
            }, 0, false);
        } else {
            $animate.enabled(siteSideNav, false);
        }
    });

    function watchEntitySubtype(enableWatch) {
        if ($scope.entitySubtypeWatch) {
            $scope.entitySubtypeWatch();
        }
        if (enableWatch) {
            $scope.entitySubtypeWatch = $scope.$watch('searchConfig.searchEntitySubtype', function (newVal, prevVal) {
                if (!angular.equals(newVal, prevVal)) {
                    $scope.$broadcast('searchEntitySubtypeUpdated');
                }
            });
        }
    }

    function displaySearchMode() {
        return $scope.searchConfig.searchEnabled &&
            $scope.searchConfig.showSearch;
    }

    function displayEntitySubtypeSearch() {
        return $scope.searchConfig.searchByEntitySubtype && vm.isGtSm;
    }

    function toggleFullscreen() {
        if (Fullscreen.isEnabled()) {
            Fullscreen.cancel();
        } else {
            Fullscreen.all();
        }
    }

    function openSearch() {
        if ($scope.searchConfig.searchEnabled) {
            $scope.searchConfig.showSearch = true;
            $timeout(() => {
                angular.element('#tb-search-text-input', $element).focus();
            });
        }
    }

    function closeSearch() {
        if ($scope.searchConfig.searchEnabled) {
            $scope.searchConfig.showSearch = false;
            if ($scope.searchConfig.searchText.length) {
                $scope.searchConfig.searchText = '';
                searchTextUpdated();
            }
        }
    }

    function searchTextUpdated() {
        $scope.$broadcast('searchTextUpdated');
    }

    function openSidenav() {
        vm.isShowSidenav = true;
    }

    function goBack() {
        $window.history.back();
    }

    function closeSidenav() {
        vm.isShowSidenav = false;
    }

    function sidenavClicked() {
        if (!vm.isLockSidenav) {
            closeSidenav();
        }
    }

}

/* eslint-enable angular/angularelement */