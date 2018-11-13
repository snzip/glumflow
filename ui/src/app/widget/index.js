import './widget-editor.scss';

import 'angular-hotkeys';
import 'angular-ui-ace';

import uiRouter from 'angular-ui-router';
import thingsboardApiUser from '../api/user.service';
import thingsboardApiWidget from '../api/widget.service';
import thingsboardTypes from '../common/types.constant';
import thingsboardToast from '../services/toast';
import thingsboardConfirmOnExit from '../components/confirm-on-exit.directive';
import thingsboardDashboard from '../components/dashboard.directive';
import thingsboardExpandFullscreen from '../components/expand-fullscreen.directive';
import thingsboardCircularProgress from '../components/circular-progress.directive';

import WidgetLibraryRoutes from './widget-library.routes';
import WidgetLibraryController from './widget-library.controller';
import SelectWidgetTypeController from './select-widget-type.controller';
import WidgetEditorController from './widget-editor.controller';
import WidgetsBundleController from './widgets-bundle.controller';
import WidgetsBundleDirective from './widgets-bundle.directive';
import SaveWidgetTypeAsController from './save-widget-type-as.controller';

export default angular.module('thingsboard.widget-library', [
    uiRouter,
    thingsboardApiWidget,
    thingsboardApiUser,
    thingsboardTypes,
    thingsboardToast,
    thingsboardConfirmOnExit,
    thingsboardDashboard,
    thingsboardExpandFullscreen,
    thingsboardCircularProgress,
    'cfp.hotkeys',
    'ui.ace'
])
    .config(WidgetLibraryRoutes)
    .controller('WidgetLibraryController', WidgetLibraryController)
    .controller('SelectWidgetTypeController', SelectWidgetTypeController)
    .controller('WidgetEditorController', WidgetEditorController)
    .controller('WidgetsBundleController', WidgetsBundleController)
    .controller('SaveWidgetTypeAsController', SaveWidgetTypeAsController)
    .directive('tbWidgetsBundle', WidgetsBundleDirective)
    .name;
