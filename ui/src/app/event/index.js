import thingsboardApiEvent from '../api/event.service';

import EventContentDialogController from './event-content-dialog.controller';
import EventHeaderDirective from './event-header.directive';
import EventRowDirective from './event-row.directive';
import EventTableDirective from './event-table.directive';

export default angular.module('thingsboard.event', [
    thingsboardApiEvent
])
    .controller('EventContentDialogController', EventContentDialogController)
    .directive('tbEventHeader', EventHeaderDirective)
    .directive('tbEventRow', EventRowDirective)
    .directive('tbEventTable', EventTableDirective)
    .name;
