/* eslint-disable import/no-unresolved, import/default */

import nodeScriptTestTemplate from './node-script-test.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function NodeScriptTest($q, $mdDialog, $document, ruleChainService) {

    var service = {
        testNodeScript: testNodeScript
    };

    return service;

    function testNodeScript($event, script, scriptType, functionTitle, functionName, argNames, ruleNodeId) {
        var deferred = $q.defer();
        if ($event) {
            $event.stopPropagation();
        }

        var msg, metadata, msgType;
        if (ruleNodeId) {
            ruleChainService.getLatestRuleNodeDebugInput(ruleNodeId).then(
                (debugIn) => {
                    if (debugIn) {
                        if (debugIn.data) {
                            msg = angular.fromJson(debugIn.data);
                        }
                        if (debugIn.metadata) {
                            metadata = angular.fromJson(debugIn.metadata);
                        }
                        msgType = debugIn.msgType;
                    }
                    openTestScriptDialog($event, script, scriptType, functionTitle,
                                         functionName, argNames, msg, metadata, msgType).then(
                        (script) => {
                            deferred.resolve(script);
                        },
                        () => {
                            deferred.reject();
                        }
                    );
                },
                () => {
                    deferred.reject();
                }
            );
        } else {
            openTestScriptDialog($event, script, scriptType, functionTitle,
                functionName, argNames).then(
                (script) => {
                    deferred.resolve(script);
                },
                () => {
                    deferred.reject();
                }
            );
        }
        return deferred.promise;
    }

    function openTestScriptDialog($event, script, scriptType, functionTitle, functionName, argNames, msg, metadata, msgType) {
        var deferred = $q.defer();
        if (!msg) {
            msg = {
                temperature: 22.4,
                humidity: 78
            };
        }
        if (!metadata) {
            metadata = {
                deviceType: "default",
                deviceName: "Test Device",
                ts: new Date().getTime() + ""
            };
        }
        if (!msgType) {
            msgType = "POST_TELEMETRY_REQUEST";
        }

        var onShowingCallback = {
            onShowed: () => {
            }
        };

        var inputParams = {
            script: script,
            scriptType: scriptType,
            functionName: functionName,
            argNames: argNames
        };

        $mdDialog.show({
            controller: 'NodeScriptTestController',
            controllerAs: 'vm',
            templateUrl: nodeScriptTestTemplate,
            parent: angular.element($document[0].body),
            locals: {
                msg: msg,
                metadata: metadata,
                msgType: msgType,
                functionTitle: functionTitle,
                inputParams: inputParams,
                onShowingCallback: onShowingCallback
            },
            fullscreen: true,
            multiple: true,
            targetEvent: $event,
            onComplete: () => {
                onShowingCallback.onShowed();
            }
        }).then(
            (script) => {
                deferred.resolve(script);
            },
            () => {
                deferred.reject();
            }
        );
        return deferred.promise;
    }

}