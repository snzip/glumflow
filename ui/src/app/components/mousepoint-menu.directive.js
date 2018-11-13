export default angular.module('thingsboard.directives.mousepointMenu', [])
    .directive('tbMousepointMenu', MousepointMenu)
    .name;

/*@ngInject*/
function MousepointMenu() {

    var linker = function ($scope, $element, $attrs, RightClickContextMenu) {

        $scope.$mdOpenMousepointMenu = function($event){
            RightClickContextMenu.offsets = function(){
                var offset = $element.offset();
                var x = $event.pageX - offset.left;
                var y = $event.pageY - offset.top;
                if ($attrs.tbOffsetX) {
                    x += Number($attrs.tbOffsetX);
                }
                if ($attrs.tbOffsetY) {
                    y += Number($attrs.tbOffsetY);
                }
                var offsets = {
                    left: x,
                    top: y
                }
                return offsets;
            }
            RightClickContextMenu.open($event);
        };

        $scope.$mdCloseMousepointMenu = function() {
            RightClickContextMenu.close();
        }
    }

    return {
        restrict: "A",
        link: linker,
        require: 'mdMenu'
    };
}
