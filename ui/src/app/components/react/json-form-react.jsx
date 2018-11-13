import './json-form.scss';

import React from 'react';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import thingsboardTheme from './styles/thingsboardTheme';
import ThingsboardSchemaForm from './json-form-schema-form.jsx';

class ReactSchemaForm extends React.Component {

    getChildContext() {
        return {
            muiTheme: this.state.muiTheme
        };
    }

    constructor(props) {
        super(props);
        this.state = {
            muiTheme: getMuiTheme(thingsboardTheme)
        };
    }

    render () {
        if (this.props.form.length > 0) {
            return <ThingsboardSchemaForm {...this.props} />;
        } else {
            return <div></div>;
        }
    }
}

ReactSchemaForm.propTypes = {
        schema: React.PropTypes.object,
        form: React.PropTypes.array,
        model: React.PropTypes.object,
        option: React.PropTypes.object,
        onModelChange: React.PropTypes.func,
        onColorClick: React.PropTypes.func
}

ReactSchemaForm.defaultProps = {
    schema: {},
    form: [ "*" ]
}

ReactSchemaForm.childContextTypes = {
        muiTheme: React.PropTypes.object
}

export default ReactSchemaForm;
