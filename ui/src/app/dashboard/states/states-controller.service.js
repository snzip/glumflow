/* eslint-disable import/no-unresolved, import/default */

import defaultStateControllerTemplate from './default-state-controller.tpl.html';
import entityStateControllerTemplate from './entity-state-controller.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

import DefaultStateController from './default-state-controller';
import EntityStateController from './entity-state-controller';

/*@ngInject*/
export default function StatesControllerService() {

    var statesControllers = {};
    statesControllers['default'] = {
        controller: DefaultStateController,
        templateUrl: defaultStateControllerTemplate
    };
    statesControllers['entity'] = {
        controller: EntityStateController,
        templateUrl: entityStateControllerTemplate
    };

    var service = {
        registerStatesController: registerStatesController,
        getStateControllers: getStateControllers,
        getStateController: getStateController,
        preserveStateControllerState: preserveStateControllerState,
        withdrawStateControllerState: withdrawStateControllerState,
        cleanupPreservedStates: cleanupPreservedStates
    };

    return service;

    function registerStatesController(id, stateControllerInfo) {
        statesControllers[id] = stateControllerInfo;
    }

    function getStateControllers() {
        return statesControllers;
    }

    function getStateController(id) {
        return statesControllers[id];
    }

    function preserveStateControllerState(id, state) {
        statesControllers[id].state = angular.copy(state);
    }

    function withdrawStateControllerState(id) {
        var state = statesControllers[id].state;
        statesControllers[id].state = null;
        return state;
    }

    function cleanupPreservedStates() {
        for (var id in statesControllers) {
            statesControllers[id].state = null;
        }
    }

}
