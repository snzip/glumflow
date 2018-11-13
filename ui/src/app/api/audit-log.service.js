export default angular.module('thingsboard.api.auditLog', [])
    .factory('auditLogService', AuditLogService)
    .name;

/*@ngInject*/
function AuditLogService($http, $q) {

    var service = {
        getAuditLogsByEntityId: getAuditLogsByEntityId,
        getAuditLogsByUserId: getAuditLogsByUserId,
        getAuditLogsByCustomerId: getAuditLogsByCustomerId,
        getAuditLogs: getAuditLogs
    }

    return service;

    function getAuditLogsByEntityId (entityType, entityId, pageLink) {
        var deferred = $q.defer();
        var url = `/api/audit/logs/entity/${entityType}/${entityId}?limit=${pageLink.limit}`;

        if (angular.isDefined(pageLink.startTime) && pageLink.startTime != null) {
            url += '&startTime=' + pageLink.startTime;
        }
        if (angular.isDefined(pageLink.endTime) && pageLink.endTime != null) {
            url += '&endTime=' + pageLink.endTime;
        }
        if (angular.isDefined(pageLink.idOffset) && pageLink.idOffset != null) {
            url += '&offset=' + pageLink.idOffset;
        }
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function getAuditLogsByUserId (userId, pageLink) {
        var deferred = $q.defer();
        var url = `/api/audit/logs/user/${userId}?limit=${pageLink.limit}`;

        if (angular.isDefined(pageLink.startTime) && pageLink.startTime != null) {
            url += '&startTime=' + pageLink.startTime;
        }
        if (angular.isDefined(pageLink.endTime) && pageLink.endTime != null) {
            url += '&endTime=' + pageLink.endTime;
        }
        if (angular.isDefined(pageLink.idOffset) && pageLink.idOffset != null) {
            url += '&offset=' + pageLink.idOffset;
        }
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function getAuditLogsByCustomerId (customerId, pageLink) {
        var deferred = $q.defer();
        var url = `/api/audit/logs/customer/${customerId}?limit=${pageLink.limit}`;

        if (angular.isDefined(pageLink.startTime) && pageLink.startTime != null) {
            url += '&startTime=' + pageLink.startTime;
        }
        if (angular.isDefined(pageLink.endTime) && pageLink.endTime != null) {
            url += '&endTime=' + pageLink.endTime;
        }
        if (angular.isDefined(pageLink.idOffset) && pageLink.idOffset != null) {
            url += '&offset=' + pageLink.idOffset;
        }
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function getAuditLogs (pageLink) {
        var deferred = $q.defer();
        var url = `/api/audit/logs?limit=${pageLink.limit}`;

        if (angular.isDefined(pageLink.startTime) && pageLink.startTime != null) {
            url += '&startTime=' + pageLink.startTime;
        }
        if (angular.isDefined(pageLink.endTime) && pageLink.endTime != null) {
            url += '&endTime=' + pageLink.endTime;
        }
        if (angular.isDefined(pageLink.idOffset) && pageLink.idOffset != null) {
            url += '&offset=' + pageLink.idOffset;
        }
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

}
