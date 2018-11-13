/* eslint-disable import/no-unresolved, import/default */

import assetFieldsetTemplate from './asset-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AssetDirective($compile, $templateCache, toast, $translate, types, assetService, customerService) {
    var linker = function (scope, element) {
        var template = $templateCache.get(assetFieldsetTemplate);
        element.html(template);

        scope.types = types;
        scope.isAssignedToCustomer = false;
        scope.isPublic = false;
        scope.assignedCustomer = null;

        scope.$watch('asset', function(newVal) {
            if (newVal) {
                if (scope.asset.customerId && scope.asset.customerId.id !== types.id.nullUid) {
                    scope.isAssignedToCustomer = true;
                    customerService.getShortCustomerInfo(scope.asset.customerId.id).then(
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

        scope.onAssetIdCopied = function() {
            toast.showSuccess($translate.instant('asset.idCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };


        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            asset: '=',
            isEdit: '=',
            assetScope: '=',
            theForm: '=',
            onAssignToCustomer: '&',
            onMakePublic: '&',
            onUnassignFromCustomer: '&',
            onDeleteAsset: '&'
        }
    };
}
