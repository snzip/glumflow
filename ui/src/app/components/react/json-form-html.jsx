import React from 'react';
import ThingsboardAceEditor from './json-form-ace-editor.jsx';
import 'brace/mode/html';
import beautify from 'js-beautify';

const html_beautify = beautify.html;

class ThingsboardHtml extends React.Component {

    constructor(props) {
        super(props);
        this.onTidyHtml = this.onTidyHtml.bind(this);
    }

    onTidyHtml(html) {
        return html_beautify(html, {indent_size: 4});
    }

    render() {
        return (
            <ThingsboardAceEditor {...this.props} mode='html' onTidy={this.onTidyHtml} {...this.state}></ThingsboardAceEditor>
        );
    }
}

export default ThingsboardHtml;
