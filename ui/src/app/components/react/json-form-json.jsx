import React from 'react';
import ThingsboardAceEditor from './json-form-ace-editor.jsx';
import 'brace/mode/json';
import beautify from 'js-beautify';

const js_beautify = beautify.js;

class ThingsboardJson extends React.Component {

    constructor(props) {
        super(props);
        this.onTidyJson = this.onTidyJson.bind(this);
    }

    onTidyJson(json) {
        return js_beautify(json, {indent_size: 4});
    }

    render() {
        return (
            <ThingsboardAceEditor {...this.props} mode='json' onTidy={this.onTidyJson} {...this.state}></ThingsboardAceEditor>
        );
    }
}

export default ThingsboardJson;
