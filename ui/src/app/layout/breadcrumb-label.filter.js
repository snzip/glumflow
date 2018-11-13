/*@ngInject*/
export default function BreadcrumbLabel($translate) {
    var labels = {};

    var breadcrumbLabel = function (bLabel) {

        var labelObj;
        labelObj = angular.fromJson(bLabel);
        if (labelObj) {
            var translate = !(labelObj.translate && labelObj.translate === 'false');
            var key = translate ? $translate.use() : 'orig';
            if (!labels[labelObj.label]) {
                labels[labelObj.label] = {};
            }
            if (!labels[labelObj.label][key]) {
                labels[labelObj.label][key] = labelObj.label;
                if (translate) {
                    $translate([labelObj.label]).then(
                        function (translations) {
                            labels[labelObj.label][key] = translations[labelObj.label];
                        }
                    )
                }
            }
            return labels[labelObj.label][key];
        } else {
            return '';
        }
    };

    breadcrumbLabel.$stateful = true;

    return breadcrumbLabel;
}
