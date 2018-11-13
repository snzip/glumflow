import tbKnob from './knob.directive';
import tbSwitch from './switch.directive';
import tbRoundSwitch from './round-switch.directive';
import tbLedIndicator from './led-indicator.directive';

export default angular.module('thingsboard.widgets.rpc', [
    tbKnob,
    tbSwitch,
    tbRoundSwitch,
    tbLedIndicator
]).name;
