/* eslint-disable import/no-unresolved, import/default */

import deviceFieldsetTemplate from './device-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function DeviceDirective($compile, $templateCache, toast, $translate, types, clipboardService, deviceService, customerService) {
    var linker = function (scope, element) {
        var template = $templateCache.get(deviceFieldsetTemplate);
        element.html(template);

        scope.types = types;
        scope.isAssignedToCustomer = false;
        scope.isPublic = false;
        scope.assignedCustomer = null;

        scope.$watch('device', function(newVal) {
            if (newVal) {
                if (scope.device.customerId && scope.device.customerId.id !== types.id.nullUid) {
                    scope.isAssignedToCustomer = true;
                    customerService.getShortCustomerInfo(scope.device.customerId.id).then(
                        function success(customer) {
                            scope.assignedCustomer = customer;
                            scope.isPublic = customer.isPublic;
                        }
                    );
                } else {
                    scope.isAssignedToCustomer = false;
                    scope.isPublic = false;
                    scope.assignedCustomer = null;
                }
            }
        });

        scope.onDeviceIdCopied = function() {
            toast.showSuccess($translate.instant('device.idCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        scope.copyAccessToken = function(e) {
            const trigger = e.delegateTarget || e.currentTarget;
            if (scope.device.id) {
                deviceService.getDeviceCredentials(scope.device.id.id, true).then(
                    function success(credentials) {
                        var credentialsId = credentials.credentialsId;
                        clipboardService.copyToClipboard(trigger, credentialsId).then(
                            () => {
                                toast.showSuccess($translate.instant('device.accessTokenCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
                            }
                        );
                    }
                );
            }
        };

        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            device: '=',
            isEdit: '=',
            deviceScope: '=',
            theForm: '=',
            onAssignToCustomer: '&',
            onMakePublic: '&',
            onUnassignFromCustomer: '&',
            onManageCredentials: '&',
            onDeleteDevice: '&'
        }
    };
}
