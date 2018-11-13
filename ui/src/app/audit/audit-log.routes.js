/* eslint-disable import/no-unresolved, import/default */

import auditLogsTemplate from './audit-logs.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AuditLogRoutes($stateProvider) {
    $stateProvider
        .state('home.auditLogs', {
            url: '/auditLogs',
            module: 'private',
            auth: ['TENANT_ADMIN'],
            views: {
                "content@home": {
                    templateUrl: auditLogsTemplate,
                    controller: 'AuditLogsController',
                    controllerAs: 'vm'
                }
            },
            data: {
                searchEnabled: false,
                pageTitle: 'audit-log.audit-logs'
            },
            ncyBreadcrumb: {
                label: '{"icon": "track_changes", "label": "audit-log.audit-logs"}'
            }
        });
}
