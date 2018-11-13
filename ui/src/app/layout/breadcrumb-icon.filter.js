export default function BreadcrumbIcon() {
    return function (bLabel) {
        var labelObj = angular.fromJson(bLabel);
        if (angular.isDefined(labelObj.icon)) {
            return labelObj.icon;
        }
        return null;
    };
}
