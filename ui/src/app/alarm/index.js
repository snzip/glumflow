import AlarmDetailsDialogController from './alarm-details-dialog.controller';
import AlarmHeaderDirective from './alarm-header.directive';
import AlarmRowDirective from './alarm-row.directive';
import AlarmTableDirective from './alarm-table.directive';

export default angular.module('thingsboard.alarm', [])
    .controller('AlarmDetailsDialogController', AlarmDetailsDialogController)
    .directive('tbAlarmHeader', AlarmHeaderDirective)
    .directive('tbAlarmRow', AlarmRowDirective)
    .directive('tbAlarmTable', AlarmTableDirective)
    .name;
