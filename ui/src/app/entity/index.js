import EntityAliasesController from './alias/entity-aliases.controller';
import EntityAliasDialogController from './alias/entity-alias-dialog.controller';
import EntityTypeSelectDirective from './entity-type-select.directive';
import EntityTypeListDirective from './entity-type-list.directive';
import EntitySubtypeListDirective from './entity-subtype-list.directive';
import EntitySubtypeSelectDirective from './entity-subtype-select.directive';
import EntitySubtypeAutocompleteDirective from './entity-subtype-autocomplete.directive';
import EntityAutocompleteDirective from './entity-autocomplete.directive';
import EntityListDirective from './entity-list.directive';
import EntitySelectDirective from './entity-select.directive';
import EntityFilterDirective from './entity-filter.directive';
import EntityFilterViewDirective from './entity-filter-view.directive';
import AliasesEntitySelectPanelController from './alias/aliases-entity-select-panel.controller';
import AliasesEntitySelectDirective from './alias/aliases-entity-select.directive';
import AddAttributeDialogController from './attribute/add-attribute-dialog.controller';
import AddWidgetToDashboardDialogController from './attribute/add-widget-to-dashboard-dialog.controller';
import AttributeTableDirective from './attribute/attribute-table.directive';
import RelationFiltersDirective from './relation/relation-filters.directive';
import RelationTableDirective from './relation/relation-table.directive';
import RelationTypeAutocompleteDirective from './relation/relation-type-autocomplete.directive';

export default angular.module('thingsboard.entity', [])
    .controller('EntityAliasesController', EntityAliasesController)
    .controller('EntityAliasDialogController', EntityAliasDialogController)
    .controller('AliasesEntitySelectPanelController', AliasesEntitySelectPanelController)
    .controller('AddAttributeDialogController', AddAttributeDialogController)
    .controller('AddWidgetToDashboardDialogController', AddWidgetToDashboardDialogController)
    .directive('tbEntityTypeSelect', EntityTypeSelectDirective)
    .directive('tbEntityTypeList', EntityTypeListDirective)
    .directive('tbEntitySubtypeList', EntitySubtypeListDirective)
    .directive('tbEntitySubtypeSelect', EntitySubtypeSelectDirective)
    .directive('tbEntitySubtypeAutocomplete', EntitySubtypeAutocompleteDirective)
    .directive('tbEntityAutocomplete', EntityAutocompleteDirective)
    .directive('tbEntityList', EntityListDirective)
    .directive('tbEntitySelect', EntitySelectDirective)
    .directive('tbEntityFilter', EntityFilterDirective)
    .directive('tbEntityFilterView', EntityFilterViewDirective)
    .directive('tbAliasesEntitySelect', AliasesEntitySelectDirective)
    .directive('tbAttributeTable', AttributeTableDirective)
    .directive('tbRelationFilters', RelationFiltersDirective)
    .directive('tbRelationTable', RelationTableDirective)
    .directive('tbRelationTypeAutocomplete', RelationTypeAutocompleteDirective)
    .name;
