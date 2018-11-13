export default angular.module('thingsboard.api.login', [])
    .factory('loginService', LoginService)
    .name;

/*@ngInject*/
function LoginService($http, $q) {

    var service = {
        activate: activate,
        changePassword: changePassword,
        hasUser: hasUser,
        login: login,
        publicLogin: publicLogin,
        resetPassword: resetPassword,
        sendResetPasswordLink: sendResetPasswordLink,
    }

    return service;

    function hasUser() {
        return true;
    }

    function login(user) {
        var deferred = $q.defer();
        var loginRequest = {
            username: user.name,
            password: user.password
        };
        $http.post('/api/auth/login', loginRequest).then(function success(response) {
            deferred.resolve(response);
        }, function fail(response) {
            deferred.reject(response);
        });
        return deferred.promise;
    }

    function publicLogin(publicId) {
        var deferred = $q.defer();
        var pubilcLoginRequest = {
            publicId: publicId
        };
        $http.post('/api/auth/login/public', pubilcLoginRequest).then(function success(response) {
            deferred.resolve(response);
        }, function fail(response) {
            deferred.reject(response);
        });
        return deferred.promise;
    }

    function sendResetPasswordLink(email) {
        var deferred = $q.defer();
        var url = '/api/noauth/resetPasswordByEmail';
        $http.post(url, {email: email}).then(function success(response) {
            deferred.resolve(response);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function resetPassword(resetToken, password) {
        var deferred = $q.defer();
        var url = '/api/noauth/resetPassword';
        $http.post(url, {resetToken: resetToken, password: password}).then(function success(response) {
            deferred.resolve(response);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function activate(activateToken, password) {
        var deferred = $q.defer();
        var url = '/api/noauth/activate';
        $http.post(url, {activateToken: activateToken, password: password}).then(function success(response) {
            deferred.resolve(response);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function changePassword(currentPassword, newPassword) {
        var deferred = $q.defer();
        var url = '/api/auth/changePassword';
        $http.post(url, {currentPassword: currentPassword, newPassword: newPassword}).then(function success(response) {
            deferred.resolve(response);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }
}
