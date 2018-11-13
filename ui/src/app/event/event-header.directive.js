/* eslint-disable import/no-unresolved, import/default */

import eventHeaderLcEventTemplate from './event-header-lc-event.tpl.html';
import eventHeaderStatsTemplate from './event-header-stats.tpl.html';
import eventHeaderErrorTemplate from './event-header-error.tpl.html';
import eventHeaderDebugRuleNodeTemplate from './event-header-debug-rulenode.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function EventHeaderDirective($compile, $templateCache, types) {

    var linker = function (scope, element, attrs) {

        var getTemplate = function(eventType) {
            var template = '';
            switch(eventType) {
                case types.eventType.lcEvent.value:
                    template = eventHeaderLcEventTemplate;
                    break;
                case types.eventType.stats.value:
                    template = eventHeaderStatsTemplate;
                    break;
                case types.eventType.error.value:
                    template = eventHeaderErrorTemplate;
                    break;
                case types.debugEventType.debugRuleNode.value:
                    template = eventHeaderDebugRuleNodeTemplate;
                    break;
                case types.debugEventType.debugRuleChain.value:
                    template = eventHeaderDebugRuleNodeTemplate;
                    break;
            }
            return $templateCache.get(template);
        }

        scope.loadTemplate = function() {
            element.html(getTemplate(attrs.eventType));
            $compile(element.contents())(scope);
        }

        attrs.$observe('eventType', function() {
            scope.loadTemplate();
        });

    }

    return {
        restrict: "A",
        replace: false,
        link: linker,
        scope: false
    };
}
