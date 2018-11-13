/* eslint-disable import/no-unresolved, import/default */

import socialsharePanelTemplate from './socialshare-panel.tpl.html';

/* eslint-enable import/no-unresolved, import/default */


export default angular.module('thingsboard.directives.socialsharePanel', [])
    .directive('tbSocialSharePanel', SocialsharePanel)
    .name;

/*@ngInject*/
function SocialsharePanel() {
    return {
        restrict: "E",
        scope: true,
        bindToController: {
            shareTitle: '@',
            shareText: '@',
            shareLink: '@',
            shareHashTags: '@'
        },
        controller: SocialsharePanelController,
        controllerAs: 'vm',
        templateUrl: socialsharePanelTemplate
    };
}

/*@ngInject*/
function SocialsharePanelController(utils) {

    let vm = this;

    vm.isShareLinkLocal = function() {
        if (vm.shareLink && vm.shareLink.length > 0) {
            return utils.isLocalUrl(vm.shareLink);
        } else {
            return true;
        }
    }

}