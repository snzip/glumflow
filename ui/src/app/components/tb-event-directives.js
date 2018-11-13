const SPECIAL_CHARS_REGEXP = /([\:\-\_]+(.))/g;
const MOZ_HACK_REGEXP = /^moz([A-Z])/;
const PREFIX_REGEXP = /^((?:x|data)[\:\-_])/i;

var tbEventDirectives = {};

angular.forEach(
    'click dblclick mousedown mouseup mouseover mouseout mousemove mouseenter mouseleave contextmenu keydown keyup keypress submit focus blur copy cut paste'.split(' '),
    function(eventName) {
        var directiveName = directiveNormalize('tb-' + eventName);
        tbEventDirectives[directiveName] = ['$parse', '$rootScope', function($parse) {
            return {
                restrict: 'A',
                compile: function($element, attr) {
                    var fn = $parse(attr[directiveName], /* interceptorFn */ null, /* expensiveChecks */ true);
                    return function ngEventHandler(scope, element) {
                        element.on(eventName, function(event) {
                            var callback = function() {
                                fn(scope, {$event:event});
                            };
                            callback();
                        });
                    };
                }
            };
        }];
    }
);

export default angular.module('thingsboard.directives.event', [])
    .directive(tbEventDirectives)
    .name;

function camelCase(name) {
    return name.
    replace(SPECIAL_CHARS_REGEXP, function(_, separator, letter, offset) {
        return offset ? letter.toUpperCase() : letter;
    }).
    replace(MOZ_HACK_REGEXP, 'Moz$1');
}

function directiveNormalize(name) {
    return camelCase(name.replace(PREFIX_REGEXP, ''));
}