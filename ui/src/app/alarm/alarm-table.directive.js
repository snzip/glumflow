import './alarm.scss';

/* eslint-disable import/no-unresolved, import/default */

import alarmTableTemplate from './alarm-table.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function AlarmTableDirective($compile, $templateCache, $rootScope, types, alarmService) {

    var linker = function (scope, element) {

        var template = $templateCache.get(alarmTableTemplate);

        element.html(template);

        scope.types = types;

        scope.alarmSearchStatus = types.alarmSearchStatus.any;

        var pageSize = 20;
        var startTime = 0;
        var endTime = 0;

        scope.timewindow = {
            history: {
                timewindowMs: 24 * 60 * 60 * 1000 // 1 day
            }
        };

        scope.topIndex = 0;

        scope.theAlarms = {
            getItemAtIndex: function (index) {
                if (index > scope.alarms.data.length) {
                    scope.theAlarms.fetchMoreItems_(index);
                    return null;
                }
                var item = scope.alarms.data[index];
                if (item) {
                    item.indexNumber = index + 1;
                }
                return item;
            },

            getLength: function () {
                if (scope.alarms.hasNext) {
                    return scope.alarms.data.length + scope.alarms.nextPageLink.limit;
                } else {
                    return scope.alarms.data.length;
                }
            },

            fetchMoreItems_: function () {
                if (scope.alarms.hasNext && !scope.alarms.pending) {
                    if (scope.entityType && scope.entityId && scope.alarmSearchStatus) {
                        var promise = alarmService.getAlarms(scope.entityType, scope.entityId,
                            scope.alarms.nextPageLink, scope.alarmSearchStatus, null, true, false);
                        if (promise) {
                            scope.alarms.pending = true;
                            promise.then(
                                function success(alarms) {
                                    scope.alarms.data = scope.alarms.data.concat(alarms.data);
                                    scope.alarms.nextPageLink = alarms.nextPageLink;
                                    scope.alarms.hasNext = alarms.hasNext;
                                    if (scope.alarms.hasNext) {
                                        scope.alarms.nextPageLink.limit = pageSize;
                                    }
                                    scope.alarms.pending = false;
                                },
                                function fail() {
                                    scope.alarms.hasNext = false;
                                    scope.alarms.pending = false;
                                });
                        } else {
                            scope.alarms.hasNext = false;
                        }
                    } else {
                        scope.alarms.hasNext = false;
                    }
                }
            }
        };

        scope.reload = reload;

        scope.$watch("entityId", function(newVal, prevVal) {
            if (newVal && !angular.equals(newVal, prevVal)) {
                resetFilter();
                reload();
            }
        });



        function destroyWatchers() {
            if (scope.alarmSearchStatusWatchHandle) {
                scope.alarmSearchStatusWatchHandle();
                scope.alarmSearchStatusWatchHandle = null;
            }
            if (scope.timewindowWatchHandle) {
                scope.timewindowWatchHandle();
                scope.timewindowWatchHandle = null;
            }
        }

        function initWatchers() {
            scope.alarmSearchStatusWatchHandle = scope.$watch("alarmSearchStatus", function(newVal, prevVal) {
                if (newVal && !angular.equals(newVal, prevVal)) {
                    reload();
                }
            });
            scope.timewindowWatchHandle = scope.$watch("timewindow", function(newVal, prevVal) {
                if (newVal && !angular.equals(newVal, prevVal)) {
                    reload();
                }
            }, true);
        }

        function resetFilter() {
            destroyWatchers();
            scope.timewindow = {
                history: {
                    timewindowMs: 24 * 60 * 60 * 1000 // 1 day
                }
            };
            scope.alarmSearchStatus = types.alarmSearchStatus.any;
            initWatchers();
        }

        function updateTimeWindowRange () {
            if (scope.timewindow.history.timewindowMs) {
                var currentTime = (new Date).getTime();
                startTime = currentTime - scope.timewindow.history.timewindowMs;
                endTime = currentTime;
            } else {
                startTime = scope.timewindow.history.fixedTimewindow.startTimeMs;
                endTime = scope.timewindow.history.fixedTimewindow.endTimeMs;
            }
        }

        function reload () {
            scope.topIndex = 0;
            scope.selected = [];
            updateTimeWindowRange();
            scope.alarms = {
                data: [],
                nextPageLink: {
                    limit: pageSize,
                    startTime: startTime,
                    endTime: endTime
                },
                hasNext: true,
                pending: false
            };
            scope.theAlarms.getItemAtIndex(pageSize);
        }

        scope.noData = function() {
            return scope.alarms.data.length == 0 && !scope.alarms.hasNext;
        }

        scope.hasData = function() {
            return scope.alarms.data.length > 0;
        }

        scope.loading = function() {
            return $rootScope.loading;
        }

        scope.hasScroll = function() {
            var repeatContainer = scope.repeatContainer[0];
            if (repeatContainer) {
                var scrollElement = repeatContainer.children[0];
                if (scrollElement) {
                    return scrollElement.scrollHeight > scrollElement.clientHeight;
                }
            }
            return false;
        }

        reload();

        initWatchers();

        $compile(element.contents())(scope);
    }

    return {
        restrict: "E",
        link: linker,
        scope: {
            entityType: '=',
            entityId: '='
        }
    };
}
