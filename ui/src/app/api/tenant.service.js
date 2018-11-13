export default angular.module('thingsboard.api.tenant', [])
    .factory('tenantService', TenantService)
    .name;

/*@ngInject*/
function TenantService($http, $q) {

    var service = {
        deleteTenant: deleteTenant,
        getTenant: getTenant,
        getTenants: getTenants,
        saveTenant: saveTenant,
    }

    return service;

    function getTenants (pageLink, config) {
        var deferred = $q.defer();
        var url = '/api/tenants?limit=' + pageLink.limit;
        if (angular.isDefined(pageLink.textSearch)) {
            url += '&textSearch=' + pageLink.textSearch;
        }
        if (angular.isDefined(pageLink.idOffset)) {
            url += '&idOffset=' + pageLink.idOffset;
        }
        if (angular.isDefined(pageLink.textOffset)) {
            url += '&textOffset=' + pageLink.textOffset;
        }
        $http.get(url, config).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function getTenant (tenantId, config) {
        var deferred = $q.defer();
        var url = '/api/tenant/' + tenantId;
        $http.get(url, config).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function saveTenant (tenant) {
        var deferred = $q.defer();
        var url = '/api/tenant';
        $http.post(url, tenant).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function deleteTenant (tenantId) {
        var deferred = $q.defer();
        var url = '/api/tenant/' + tenantId;
        $http.delete(url).then(function success() {
            deferred.resolve();
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

}
