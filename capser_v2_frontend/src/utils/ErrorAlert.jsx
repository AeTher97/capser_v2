import React from 'react';
import PropTypes from 'prop-types';

const ErrorAlert = (props) => {
    const {error} = props;

    return (
        !!error && (<div> {error.message}</div>)
    );
};

ErrorAlert.propTypes = {
    error: PropTypes.shape({
        message: PropTypes.string.isRequired
    })
};

export default ErrorAlert;
