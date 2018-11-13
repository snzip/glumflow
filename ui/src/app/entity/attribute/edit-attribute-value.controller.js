/*@ngInject*/
export default function EditAttributeValueController($scope, $q, $element, types, attributeValue, save) {

    $scope.valueTypes = types.valueType;

    $scope.model = {};

    $scope.model.value = attributeValue;

    if ($scope.model.value === true || $scope.model.value === false) {
        $scope.valueType = types.valueType.boolean;
    } else if (angular.isNumber($scope.model.value)) {
        if ($scope.model.value.toString().indexOf('.') == -1) {
            $scope.valueType = types.valueType.integer;
        } else {
            $scope.valueType = types.valueType.double;
        }
    } else {
        $scope.valueType = types.valueType.string;
    }

    $scope.submit = submit;
    $scope.dismiss = dismiss;

    function dismiss() {
        $element.remove();
    }

    function update() {
        if($scope.editDialog.$invalid) {
            return $q.reject();
        }

        if(angular.isFunction(save)) {
            return $q.when(save($scope.model));
        }

        return $q.resolve();
    }

    function submit() {
        update().then(function () {
            $scope.dismiss();
        });
    }

    $scope.$watch('valueType', function(newVal, prevVal) {
        if (newVal != prevVal) {
            if ($scope.valueType === types.valueType.boolean) {
                $scope.model.value = false;
            } else {
                $scope.model.value = null;
            }
        }
    });
}
