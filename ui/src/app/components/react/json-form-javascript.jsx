import React from 'react';
import ThingsboardAceEditor from './json-form-ace-editor.jsx';
import 'brace/mode/javascript';
import beautify from 'js-beautify';

const js_beautify = beautify.js;

class ThingsboardJavaScript extends React.Component {

    constructor(props) {
        super(props);
        this.onTidyJavascript = this.onTidyJavascript.bind(this);
    }

    onTidyJavascript(javascript) {
        return js_beautify(javascript, {indent_size: 4, wrap_line_length: 60});
    }

    render() {
        return (
                <ThingsboardAceEditor {...this.props} mode='javascript' onTidy={this.onTidyJavascript} {...this.state}></ThingsboardAceEditor>
            );
    }
}

export default ThingsboardJavaScript;
