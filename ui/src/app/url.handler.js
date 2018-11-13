export default function UrlHandler($injector, $location) {
    var userService = $injector.get('userService');
    if (userService.isUserLoaded() === true) {
        userService.gotoDefaultPlace();
    } else {
        var $rootScope = $injector.get('$rootScope');
        if ($rootScope.userLoadedHandle) {
            $rootScope.userLoadedHandle();
        }
        $rootScope.userLoadedHandle = $rootScope.$on('userLoaded', function () {
            $rootScope.userLoadedHandle();
            UrlHandler($injector, $location);
        });
    }
}