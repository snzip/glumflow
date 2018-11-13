/* eslint-disable import/no-unresolved, import/default */

import ruleChainFieldsetTemplate from './rulechain-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function RuleChainDirective($compile, $templateCache, $mdDialog, $document, $q, $translate, types, toast) {
    var linker = function (scope, element) {
        var template = $templateCache.get(ruleChainFieldsetTemplate);
        element.html(template);

        scope.onRuleChainIdCopied = function() {
            toast.showSuccess($translate.instant('rulechain.idCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            ruleChain: '=',
            isEdit: '=',
            isReadOnly: '=',
            theForm: '=',
            onSetRootRuleChain: '&',
            onExportRuleChain: '&',
            onDeleteRuleChain: '&'
        }
    };
}
