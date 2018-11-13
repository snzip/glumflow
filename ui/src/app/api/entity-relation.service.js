export default angular.module('thingsboard.api.entityRelation', [])
    .factory('entityRelationService', EntityRelationService)
    .name;

/*@ngInject*/
function EntityRelationService($http, $q) {

    var service = {
        saveRelation: saveRelation,
        deleteRelation: deleteRelation,
        deleteRelations: deleteRelations,
        getRelation: getRelation,
        findByFrom: findByFrom,
        findInfoByFrom: findInfoByFrom,
        findByFromAndType: findByFromAndType,
        findByTo: findByTo,
        findInfoByTo: findInfoByTo,
        findByToAndType: findByToAndType,
        findByQuery: findByQuery,
        findInfoByQuery: findInfoByQuery
    }

    return service;

    function saveRelation(relation) {
        var deferred = $q.defer();
        var url = '/api/relation';
        $http.post(url, relation).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function deleteRelation(fromId, fromType, relationType, toId, toType) {
        var deferred = $q.defer();
        var url = '/api/relation?fromId=' + fromId;
        url += '&fromType=' + fromType;
        url += '&relationType=' + relationType;
        url += '&toId=' + toId;
        url += '&toType=' + toType;
        $http.delete(url).then(function success() {
            deferred.resolve();
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function deleteRelations(entityId, entityType) {
        var deferred = $q.defer();
        var url = '/api/relations?entityId=' + entityId;
        url += '&entityType=' + entityType;
        $http.delete(url).then(function success() {
            deferred.resolve();
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function getRelation(fromId, fromType, relationType, toId, toType) {
        var deferred = $q.defer();
        var url = '/api/relation?fromId=' + fromId;
        url += '&fromType=' + fromType;
        url += '&relationType=' + relationType;
        url += '&toId=' + toId;
        url += '&toType=' + toType;
        $http.get(url).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findByFrom(fromId, fromType) {
        var deferred = $q.defer();
        var url = '/api/relations?fromId=' + fromId;
        url += '&fromType=' + fromType;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findInfoByFrom(fromId, fromType) {
        var deferred = $q.defer();
        var url = '/api/relations/info?fromId=' + fromId;
        url += '&fromType=' + fromType;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findByFromAndType(fromId, fromType, relationType) {
        var deferred = $q.defer();
        var url = '/api/relations?fromId=' + fromId;
        url += '&fromType=' + fromType;
        url += '&relationType=' + relationType;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findByTo(toId, toType) {
        var deferred = $q.defer();
        var url = '/api/relations?toId=' + toId;
        url += '&toType=' + toType;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findInfoByTo(toId, toType) {
        var deferred = $q.defer();
        var url = '/api/relations/info?toId=' + toId;
        url += '&toType=' + toType;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findByToAndType(toId, toType, relationType) {
        var deferred = $q.defer();
        var url = '/api/relations?toId=' + toId;
        url += '&toType=' + toType;
        url += '&relationType=' + relationType;
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findByQuery(query) {
        var deferred = $q.defer();
        var url = '/api/relations';
        $http.post(url, query).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

    function findInfoByQuery(query, config) {
        var deferred = $q.defer();
        var url = '/api/relations/info';
        $http.post(url, query, config).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail() {
            deferred.reject();
        });
        return deferred.promise;
    }

}
