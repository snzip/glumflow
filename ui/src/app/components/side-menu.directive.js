import './side-menu.scss';

import thingsboardMenu from '../services/menu.service';
import thingsboardMenuLink from './menu-link.directive';

/* eslint-disable import/no-unresolved, import/default */

import sidemenuTemplate from './side-menu.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

export default angular.module('thingsboard.directives.sideMenu', [thingsboardMenu, thingsboardMenuLink])
    .directive('tbSideMenu', SideMenu)
    .name;

/*@ngInject*/
function SideMenu($compile, $templateCache, menu) {

    var linker = function (scope, element) {

        scope.sections = menu.getSections();

        var template = $templateCache.get(sidemenuTemplate);

        element.html(template);

        $compile(element.contents())(scope);
    }

    return {
        restrict: "E",
        link: linker,
        scope: {}
    };
}
