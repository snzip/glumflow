/*@ngInject*/
export default function DashboardSelectPanelController(mdPanelRef, $scope, $filter, dashboards, dashboardId, onDashboardSelected) {

    var vm = this;
    vm._mdPanelRef = mdPanelRef;
    vm.dashboards = dashboards;
    vm.dashboardId = dashboardId;

    vm.dashboardSelected = dashboardSelected;

    function dashboardSelected(dashboardId) {
        if (onDashboardSelected) {
            onDashboardSelected(dashboardId);
        }
    }
}
