import React from 'react';

class ThingsboardFieldSet extends React.Component {

    render() {
        let forms = this.props.form.items.map(function(form, index){
            return this.props.builder(form, this.props.model, index, this.props.onChange, this.props.onColorClick, this.props.mapper, this.props.builder);
        }.bind(this));

        return (
            <div style={{paddingTop: '20px'}}>
                <div className="tb-head-label">
                    {this.props.form.title}
                </div>
                <div>
                    {forms}
                </div>
            </div>
        );
    }
}

export default ThingsboardFieldSet;
