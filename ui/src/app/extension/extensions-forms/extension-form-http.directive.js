import 'brace/ext/language_tools';
import 'brace/mode/json';
import 'brace/theme/github';

import './extension-form.scss';

/* eslint-disable angular/log */

import extensionFormHttpTemplate from './extension-form-http.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function ExtensionFormHttpDirective($compile, $templateCache, $translate, types) {

    var linker = function(scope, element) {

        var template = $templateCache.get(extensionFormHttpTemplate);
        element.html(template);

        scope.types = types;
        scope.theForm = scope.$parent.theForm;

        scope.extensionCustomTransformerOptions = {
            useWrapMode: false,
            mode: 'json',
            showGutter: true,
            showPrintMargin: true,
            theme: 'github',
            advanced: {
                enableSnippets: true,
                enableBasicAutocompletion: true,
                enableLiveAutocompletion: true
            },
            onLoad: function(_ace) {
                _ace.$blockScrolling = 1;
            }
        };


        scope.addConverterConfig = function() {
            var newConverterConfig = {converterId:"", converters:[]};
            scope.converterConfigs.push(newConverterConfig);

            scope.converterConfigs[scope.converterConfigs.length - 1].converters = [];
            scope.addConverter(scope.converterConfigs[scope.converterConfigs.length - 1].converters);
        };

        scope.removeConverterConfig = function(config) {
            var index = scope.converterConfigs.indexOf(config);
            if (index > -1) {
                scope.converterConfigs.splice(index, 1);
            }
        };

        scope.addConverter = function(converters) {
            var newConverter = {
                deviceNameJsonExpression:"",
                deviceTypeJsonExpression:"",
                attributes:[],
                timeseries:[]
            };
            converters.push(newConverter);
        };

        scope.removeConverter = function(converter, converters) {
            var index = converters.indexOf(converter);
            if (index > -1) {
                converters.splice(index, 1);
            }
        };

        scope.addAttribute = function(attributes) {
            var newAttribute = {type:"", key:"", value:""};
            attributes.push(newAttribute);
        };

        scope.removeAttribute = function(attribute, attributes) {
            var index = attributes.indexOf(attribute);
            if (index > -1) {
                attributes.splice(index, 1);
            }
        };


        if(scope.isAdd) {
            scope.converterConfigs = scope.config.converterConfigurations;
            scope.addConverterConfig();
        } else {
            scope.converterConfigs = scope.config.converterConfigurations;
        }

        scope.transformerTypeChange = function(attribute) {
            attribute.transformer = "";
        };

        scope.validateTransformer = function (model, editorName) {
            if(model && model.length) {
                try {
                    angular.fromJson(model);
                    scope.theForm[editorName].$setValidity('transformerJSON', true);
                } catch(e) {
                    scope.theForm[editorName].$setValidity('transformerJSON', false);
                }
            }
        };

        scope.collapseValidation = function(index, id) {
            var invalidState = angular.element('#'+id+':has(.ng-invalid)');
            if(invalidState.length) {
                invalidState.addClass('inner-invalid');
            }
        };

        scope.expandValidation = function (index, id) {
            var invalidState = angular.element('#'+id);
            invalidState.removeClass('inner-invalid');
        };
        
        $compile(element.contents())(scope);
    };

    return {
        restrict: "A",
        link: linker,
        scope: {
            config: "=",
            isAdd: "="
        }
    }
}