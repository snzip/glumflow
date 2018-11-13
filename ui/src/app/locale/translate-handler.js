 export default angular.module('thingsboard.locale', [])
                       .factory('tbMissingTranslationHandler', ThingsboardMissingTranslateHandler)
                       .name;

/*@ngInject*/
function ThingsboardMissingTranslateHandler($log, types) {

    return function (translationId) {
        if (translationId && !translationId.startsWith(types.translate.customTranslationsPrefix)) {
            $log.warn('Translation for ' + translationId + ' doesn\'t exist');
        }
    };

}