import 'brace/ext/language_tools';
import 'brace/mode/json';
import 'brace/theme/github';
import beautify from 'js-beautify';

import './relation-dialog.scss';

const js_beautify = beautify.js;

/*@ngInject*/
export default function RelationDialogController($scope, $mdDialog, types, entityRelationService, isAdd, direction, relation, showingCallback) {

    var vm = this;

    vm.types = types;
    vm.isAdd = isAdd;
    vm.direction = direction;

    showingCallback.onShowing = function(scope, element) {
        updateEditorSize(element);
    }

    vm.relationAdditionalInfoOptions = {
        useWrapMode: false,
        mode: 'json',
        showGutter: false,
        showPrintMargin: false,
        theme: 'github',
        advanced: {
            enableSnippets: false,
            enableBasicAutocompletion: false,
            enableLiveAutocompletion: false
        },
        onLoad: function (_ace) {
            vm.editor = _ace;
        }
    };

    vm.relation = relation;
    if (vm.isAdd) {
        vm.relation.type = types.entityRelationType.contains;
        vm.targetEntityId = {};
    } else {
        if (vm.direction == vm.types.entitySearchDirection.from) {
            vm.targetEntityId = vm.relation.to;
        } else {
            vm.targetEntityId = vm.relation.from;
        }
    }

    vm.save = save;
    vm.cancel = cancel;

    vm.additionalInfo = '';

    if (vm.relation.additionalInfo) {
        vm.additionalInfo = angular.toJson(vm.relation.additionalInfo);
        vm.additionalInfo = js_beautify(vm.additionalInfo, {indent_size: 4});
    }

    $scope.$watch('vm.additionalInfo', () => {
        $scope.theForm.$setValidity("additionalInfo", true);
    });

    function updateEditorSize(element) {
        var newHeight = 200;
        angular.element('#tb-relation-additional-info', element).height(newHeight.toString() + "px");
        vm.editor.resize();
    }

    function cancel() {
        $mdDialog.cancel();
    }

    function save() {
        if (vm.isAdd) {
            if (vm.direction == vm.types.entitySearchDirection.from) {
                vm.relation.to = vm.targetEntityId;
            } else {
                vm.relation.from = vm.targetEntityId;
            }
        }
        $scope.theForm.$setPristine();

        var valid = true;
        if (vm.additionalInfo && vm.additionalInfo.length) {
            try {
                vm.relation.additionalInfo = angular.fromJson(vm.additionalInfo);
            } catch(e) {
                valid = false;
            }
        } else {
            vm.relation.additionalInfo = null;
        }

        $scope.theForm.$setValidity("additionalInfo", valid);

        if (valid) {
            entityRelationService.saveRelation(vm.relation).then(
                function success() {
                    $mdDialog.hide();
                }
            );
        }
    }

}
