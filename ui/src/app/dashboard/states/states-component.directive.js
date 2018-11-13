/*@ngInject*/
export default function StatesComponent($compile, $templateCache, $controller, statesControllerService) {

    var linker = function (scope, element) {

        function destroyStateController() {
            if (scope.statesController && angular.isFunction(scope.statesController.$onDestroy)) {
                scope.statesController.$onDestroy();
            }
        }

        function init() {

            var stateController = scope.dashboardCtrl.dashboardCtx.stateController;

            stateController.openState = function(id, params, openRightLayout) {
                if (scope.statesController) {
                    scope.statesController.openState(id, params, openRightLayout);
                }
            }

            stateController.updateState = function(id, params, openRightLayout) {
                if (scope.statesController) {
                    scope.statesController.updateState(id, params, openRightLayout);
                }
            }

            stateController.resetState = function() {
                if (scope.statesController) {
                    scope.statesController.resetState();
                }
            }

            stateController.preserveState = function() {
                if (scope.statesController) {
                    var state = scope.statesController.getStateObject();
                    statesControllerService.preserveStateControllerState(scope.statesControllerId, state);
                }
            }

            stateController.cleanupPreservedStates = function() {
                statesControllerService.cleanupPreservedStates();
            }

            stateController.navigatePrevState = function(index) {
                if (scope.statesController) {
                    scope.statesController.navigatePrevState(index);
                }
            }

            stateController.getStateId = function() {
                if (scope.statesController) {
                    return scope.statesController.getStateId();
                } else {
                    return '';
                }
            }

            stateController.getStateIndex = function() {
                if (scope.statesController) {
                    return scope.statesController.getStateIndex();
                } else {
                    return -1;
                }
            }

            stateController.getStateIdAtIndex = function(index) {
                if (scope.statesController) {
                    return scope.statesController.getStateIdAtIndex(index);
                } else {
                    return '';
                }
            }

            stateController.getStateParams = function() {
                if (scope.statesController) {
                    return scope.statesController.getStateParams();
                } else {
                    return {};
                }
            }

            stateController.getStateParamsByStateId = function(id) {
                if (scope.statesController) {
                    return scope.statesController.getStateParamsByStateId(id);
                } else {
                    return null;
                }
            }

            stateController.getEntityId = function(entityParamName) {
                if (scope.statesController) {
                    return scope.statesController.getEntityId(entityParamName);
                } else {
                    return null;
                }
            }

        }

        scope.$on('$destroy', function callOnDestroyHook() {
            destroyStateController();
        });

        scope.$watch('scope.dashboardCtrl', function() {
            if (scope.dashboardCtrl.dashboardCtx) {
                init();
            }
        })

        scope.$watch('statesControllerId', function(newValue) {
            if (newValue) {
                if (scope.statesController) {
                    destroyStateController();
                }
                var statesControllerInfo = statesControllerService.getStateController(scope.statesControllerId);
                if (!statesControllerInfo) {
                    //fallback to default
                    statesControllerInfo = statesControllerService.getStateController('default');
                }
                var template = $templateCache.get(statesControllerInfo.templateUrl);
                element.html(template);

                var preservedState = statesControllerService.withdrawStateControllerState(scope.statesControllerId);

                var locals = {
                    preservedState: preservedState
                };
                angular.extend(locals, {$scope: scope, $element: element});
                var controller = $controller(statesControllerInfo.controller, locals, true, 'vm');
                controller.instance = controller();
                scope.statesController = controller.instance;
                scope.statesController.dashboardCtrl = scope.dashboardCtrl;
                scope.statesController.states = scope.states;
                $compile(element.contents())(scope);
            }
        });

        scope.$watch('states', function() {
            if (scope.statesController) {
                scope.statesController.states = scope.states;
            }
        });

    }

    return {
        restrict: "E",
        link: linker,
        scope: {
            statesControllerId: '=',
            dashboardCtrl: '=',
            states: '='
        }
    };
}
