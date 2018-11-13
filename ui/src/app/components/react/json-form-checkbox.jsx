import React from 'react';
import ThingsboardBaseComponent from './json-form-base-component.jsx';
import Checkbox from 'material-ui/Checkbox';

class ThingsboardCheckbox extends React.Component {
    render() {
        return (
            <Checkbox
                name={this.props.form.key.slice(-1)[0]}
                value={this.props.form.key.slice(-1)[0]}
                defaultChecked={this.props.value || false}
                label={this.props.form.title}
                disabled={this.props.form.readonly}
                onCheck={(e, checked) => {this.props.onChangeValidate(e)}}
                style={{paddingTop: '14px'}}
            />
        );
    }
}

export default ThingsboardBaseComponent(ThingsboardCheckbox);