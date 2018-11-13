export default angular.module('thingsboard.api.event', [])
    .factory('eventService', EventService)
    .name;

/*@ngInject*/
function EventService($http, $q) {

    var service = {
        getEvents: getEvents
    }

    return service;

    function getEvents (entityType, entityId, eventType, tenantId, pageLink) {
        var deferred = $q.defer();
        var url = '/api/events/'+entityType+'/'+entityId+'/'+eventType+'?tenantId=' + tenantId + '&limit=' + pageLink.limit;

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
