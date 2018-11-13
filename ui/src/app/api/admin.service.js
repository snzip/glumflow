export default angular.module('thingsboard.api.admin', [])
    .factory('adminService', AdminService)
    .name;

/*@ngInject*/
function AdminService($http, $q) {

    var service = {
        getAdminSettings: getAdminSettings,
        saveAdminSettings: saveAdminSettings,
        sendTestMail: sendTestMail,
        checkUpdates: checkUpdates
    }

    return service;

    function getAdminSettings(key) {
        var deferred = $q.defer();
        var url = '/api/admin/settings/' + key;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function saveAdminSettings(settings) {
        var deferred = $q.defer();
        var url = '/api/admin/settings';
        $http.post(url, settings).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function sendTestMail(settings) {
        var deferred = $q.defer();
        var url = '/api/admin/settings/testMail';
        $http.post(url, settings).then(function success() {
            deferred.resolve();
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function checkUpdates() {
        var deferred = $q.defer();
        var url = '/api/admin/updates';
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }
}
