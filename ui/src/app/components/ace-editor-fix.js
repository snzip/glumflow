export default function fixAceEditor(aceEditor) {
    aceEditor.$blockScrolling = Infinity;
    aceEditor.on("showGutterTooltip", function (tooltip) {
        if (!tooltip.isAttachedToBody) {
            document.body.appendChild(tooltip.$element); //eslint-disable-line
            tooltip.isAttachedToBody = true;
            onElementRemoved(tooltip.$parentNode, () => {
                if (tooltip.$element.parentNode != null) {
                    tooltip.$element.parentNode.removeChild(tooltip.$element);
                }
            });
        }
    });
}

function onElementRemoved(element, callback) {
    if (!document.body.contains(element)) { //eslint-disable-line
        callback();
    } else {
        var observer;
        observer = new MutationObserver(function(mutations) { //eslint-disable-line
            if (!document.body.contains(element)) { //eslint-disable-line
                callback();
                observer.disconnect();
            }
        });
        observer.observe(document.body, {childList: true}); //eslint-disable-line
    }
}
