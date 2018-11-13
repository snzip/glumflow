import React from 'react';
import ThingsboardBaseComponent from './json-form-base-component.jsx';
import DatePicker from 'material-ui/DatePicker/DatePicker';

class ThingsboardDate extends React.Component {

    constructor(props) {
        super(props);
        this.onDatePicked = this.onDatePicked.bind(this);
    }


    onDatePicked(empty, date) {
        this.props.onChangeValidate(date);
    }

    render() {

        var fieldClass = "tb-date-field";
        if (this.props.form.required) {
            fieldClass += " tb-required";
        }
        if (this.props.form.readonly) {
            fieldClass += " tb-readonly";
        }

        return (
            <div style={{width: '100%', display: 'block'}}>
                <DatePicker
                    className={fieldClass}
                    mode={'landscape'}
                    autoOk={true}
                    hintText={this.props.form.title}
                    onChange={this.onDatePicked}
                    onShow={null}
                    onDismiss={null}
                    disabled={this.props.form.readonly}
                    style={this.props.form.style || {width: '100%'}}/>

            </div>
        );
    }
}

export default ThingsboardBaseComponent(ThingsboardDate);