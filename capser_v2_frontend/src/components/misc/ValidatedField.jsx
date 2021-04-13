import React from 'react';
import PropTypes from 'prop-types';
import {TextField, Typography} from "@material-ui/core";

const ValidatedField = props => {

    const {
        field: {
            value,
            handleBlur = () => null,
            handleChange = () => null,
            disabled,
            error,
            showError
        },
        label
    } = props;


    return (
        <>
            <TextField
                {...props}
                value={value}
                onBlur={handleBlur}
                onChange={handleChange}
                disabled={disabled}
                style={{flex: 1, width: "100%", marginBottom: showError ? 20 : 0}}
                label={!value ? label : ''}
            />
            {!!error && showError && <div style={{position: "relative", top: -17}}>
                <Typography style={{position: "absolute"}} variant={"caption"} color={"primary"}>{error}</Typography>
            </div>}
        </>
    );
};

ValidatedField.propTypes = {
    field: PropTypes.shape({
        value: PropTypes.string.isRequired,
        handleBlur: PropTypes.func,
        error: PropTypes.string,
        handleChange: PropTypes.func,
        disabled: PropTypes.bool,
        showError: PropTypes.bool
    }).isRequired,
    type: PropTypes.oneOf(['password', '', 'number', 'tel']),
    disabled: PropTypes.bool,
    label: PropTypes.string.isRequired,

};

export default ValidatedField;
