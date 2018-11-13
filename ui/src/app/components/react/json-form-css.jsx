import React from 'react';
import ThingsboardAceEditor from './json-form-ace-editor.jsx';
import 'brace/mode/css';
import beautify from 'js-beautify';

const css_beautify = beautify.css;

class ThingsboardCss extends React.Component {

    constructor(props) {
        super(props);
        this.onTidyCss = this.onTidyCss.bind(this);
    }

    onTidyCss(css) {
        return css_beautify(css, {indent_size: 4});
    }

    render() {
        return (
            <ThingsboardAceEditor {...this.props} mode='css' onTidy={this.onTidyCss} {...this.state}></ThingsboardAceEditor>
        );
    }
}

export default ThingsboardCss;
