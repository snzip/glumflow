import React from 'react';
import { utils } from 'react-schema-form';

export default ThingsboardBaseComponent => class extends React.Component {

    constructor(props) {
        super(props);
        this.onChangeValidate = this.onChangeValidate.bind(this);
        let value = this.defaultValue();
        let validationResult = utils.validate(this.props.form, value);
        this.state = {
            value: value,
            valid: !!(validationResult.valid || !value),
            error: !validationResult.valid && value ? validationResult.error.message : null
        };
    }

    componentDidMount() {
        if (typeof this.state.value !== 'undefined') {
            this.props.onChange(this.props.form.key, this.state.value);
        }
    }

    onChangeValidate(e) {
        let value = null;
        if (this.props.form.schema.type === 'integer' || this.props.form.schema.type === 'number') {
            if (e.target.value === null || e.target.value === '') {
                value = undefined;
            } else if (e.target.value.indexOf('.') == -1) {
                value = parseInt(e.target.value);
            } else {
                value = parseFloat(e.target.value);
            }
        } else if(this.props.form.schema.type === 'boolean') {
            value = e.target.checked;
        } else if(this.props.form.schema.type === 'date' || this.props.form.schema.type === 'array') {
            value = e;
        } else { // string
            value = e.target.value;
        }
        let validationResult = utils.validate(this.props.form, value);
        this.setState({
            value: value,
            valid: validationResult.valid,
            error: validationResult.valid ? null : validationResult.error.message
        });
        this.props.onChange(this.props.form.key, value);
    }

    defaultValue() {
        let value = utils.selectOrSet(this.props.form.key, this.props.model);
        if (this.props.form.schema.type === 'boolean') {
            if (typeof value !== 'boolean' && this.props.form['default']) {
                value = this.props.form['default'];
            }
            if (typeof value !== 'boolean' && this.props.form.schema && this.props.form.schema['default']) {
                value = this.props.form.schema['default'];
            }
            if (typeof value !== 'boolean' &&
                this.props.form.schema &&
                this.props.form.required) {
                value = false;
            }
        } else if (this.props.form.schema.type === 'integer' || this.props.form.schema.type === 'number') {
            if (typeof value !== 'number' && this.props.form['default']) {
                value = this.props.form['default'];
            }
            if (typeof value !== 'number' && this.props.form.schema && this.props.form.schema['default']) {
                value = this.props.form.schema['default'];
            }
            if (typeof value !== 'number' && this.props.form.titleMap && this.props.form.titleMap[0].value) {
                value = this.props.form.titleMap[0].value;
            }
            if (value && typeof value === 'string') {
                if (value.indexOf('.') == -1) {
                    value = parseInt(value);
                } else {
                    value = parseFloat(value);
                }
            }
        } else {
            if(!value && this.props.form['default']) {
                value = this.props.form['default'];
            }
            if(!value && this.props.form.schema && this.props.form.schema['default']) {
                value = this.props.form.schema['default'];
            }
            if(!value && this.props.form.titleMap && this.props.form.titleMap[0].value) {
                value = this.props.form.titleMap[0].value;
            }
        }
        return value;
    }

    render() {
        if (this.props.form && this.props.form.schema) {
            return <ThingsboardBaseComponent {...this.props} {...this.state} onChangeValidate={this.onChangeValidate}/>;
        } else {
            return <div></div>;
        }
    }
};
