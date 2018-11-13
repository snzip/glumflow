import RuleChainRoutes from './rulechain.routes';
import RuleChainsController from './rulechains.controller';
import {RuleChainController, AddRuleNodeController, AddRuleNodeLinkController} from './rulechain.controller';
import NodeScriptTestController from './script/node-script-test.controller';
import RuleChainDirective from './rulechain.directive';
import RuleNodeDefinedConfigDirective from './rulenode-defined-config.directive';
import RuleNodeConfigDirective from './rulenode-config.directive';
import RuleNodeDirective from './rulenode.directive';
import LinkDirective from './link.directive';
import MessageTypeAutocompleteDirective from './message-type-autocomplete.directive';
import NodeScriptTest from './script/node-script-test.service';

export default angular.module('thingsboard.ruleChain', [])
    .config(RuleChainRoutes)
    .controller('RuleChainsController', RuleChainsController)
    .controller('RuleChainController', RuleChainController)
    .controller('AddRuleNodeController', AddRuleNodeController)
    .controller('AddRuleNodeLinkController', AddRuleNodeLinkController)
    .controller('NodeScriptTestController', NodeScriptTestController)
    .directive('tbRuleChain', RuleChainDirective)
    .directive('tbRuleNodeDefinedConfig', RuleNodeDefinedConfigDirective)
    .directive('tbRuleNodeConfig', RuleNodeConfigDirective)
    .directive('tbRuleNode', RuleNodeDirective)
    .directive('tbRuleNodeLink', LinkDirective)
    .directive('tbMessageTypeAutocomplete', MessageTypeAutocompleteDirective)
    .factory('ruleNodeScriptTest', NodeScriptTest)
    .name;
