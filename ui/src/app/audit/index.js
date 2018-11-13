import AuditLogRoutes from './audit-log.routes';
import AuditLogsController from './audit-logs.controller';
import AuditLogDetailsDialogController from './audit-log-details-dialog.controller';
import AuditLogHeaderDirective from './audit-log-header.directive';
import AuditLogRowDirective from './audit-log-row.directive';
import AuditLogTableDirective from './audit-log-table.directive';

export default angular.module('thingsboard.auditLog', [])
    .config(AuditLogRoutes)
    .controller('AuditLogsController', AuditLogsController)
    .controller('AuditLogDetailsDialogController', AuditLogDetailsDialogController)
    .directive('tbAuditLogHeader', AuditLogHeaderDirective)
    .directive('tbAuditLogRow', AuditLogRowDirective)
    .directive('tbAuditLogTable', AuditLogTableDirective)
    .name;
