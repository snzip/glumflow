export default angular.module('thingsboard.api.customer', [])
    .factory('customerService', CustomerService)
    .name;

/*@ngInject*/
function CustomerService($http, $q, types) {

    var service = {
        getCustomers: getCustomers,
        getCustomer: getCustomer,
        getShortCustomerInfo: getShortCustomerInfo,
        applyAssignedCustomersInfo: applyAssignedCustomersInfo,
        applyAssignedCustomerInfo: applyAssignedCustomerInfo,
        deleteCustomer: deleteCustomer,
        saveCustomer: saveCustomer
    }

    return service;

    function getCustomers(pageLink, config) {
        var deferred = $q.defer();
        var url = '/api/customers?limit=' + pageLink.limit;
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

    function getCustomer(customerId, config) {
        var deferred = $q.defer();
        var url = '/api/customer/' + customerId;
        $http.get(url, config).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function getShortCustomerInfo(customerId) {
        var deferred = $q.defer();
        var url = '/api/customer/' + customerId + '/shortInfo';
        $http.get(url, null).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function applyAssignedCustomersInfo(items) {
        var deferred = $q.defer();
        var assignedCustomersMap = {};
        function loadNextCustomerInfoOrComplete(i) {
            i++;
            if (i < items.length) {
                loadNextCustomerInfo(i);
            } else {
                deferred.resolve(items);
            }
        }

        function loadNextCustomerInfo(i) {
            var item = items[i];
            item.assignedCustomer = {};
            if (item.customerId && item.customerId.id != types.id.nullUid) {
                item.assignedCustomer.id = item.customerId.id;
                var assignedCustomer = assignedCustomersMap[item.customerId.id];
                if (assignedCustomer){
                    item.assignedCustomer = assignedCustomer;
                    loadNextCustomerInfoOrComplete(i);
                } else {
                    getShortCustomerInfo(item.customerId.id).then(
                        function success(info) {
                            assignedCustomer = {
                                id: item.customerId.id,
                                title: info.title,
                                isPublic: info.isPublic
                            };
                            assignedCustomersMap[assignedCustomer.id] = assignedCustomer;
                            item.assignedCustomer = assignedCustomer;
                            loadNextCustomerInfoOrComplete(i);
                        },
                        function fail() {
                            loadNextCustomerInfoOrComplete(i);
                        }
                    );
                }
            } else {
                loadNextCustomerInfoOrComplete(i);
            }
        }
        if (items.length > 0) {
            loadNextCustomerInfo(0);
        } else {
            deferred.resolve(items);
        }
        return deferred.promise;
    }

    function applyAssignedCustomerInfo(items, customerId) {
        var deferred = $q.defer();
        getShortCustomerInfo(customerId).then(
            function success(info) {
                var assignedCustomer = {
                    id: customerId,
                    title: info.title,
                    isPublic: info.isPublic
                }
                items.forEach(function(item) {
                    item.assignedCustomer = assignedCustomer;
                });
                deferred.resolve(items);
            },
            function fail() {
                deferred.reject();
            }
        );
        return deferred.promise;
    }

    function saveCustomer(customer) {
        var deferred = $q.defer();
        var url = '/api/customer';
        $http.post(url, customer).then(function success(response) {
            deferred.resolve(response.data);
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

    function deleteCustomer(customerId) {
        var deferred = $q.defer();
        var url = '/api/customer/' + customerId;
        $http.delete(url).then(function success() {
            deferred.resolve();
        }, function fail(response) {
            deferred.reject(response.data);
        });
        return deferred.promise;
    }

}
