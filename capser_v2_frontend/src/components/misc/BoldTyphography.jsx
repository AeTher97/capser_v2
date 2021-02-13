import React from 'react';
import PropTypes from 'prop-types';
import {Typography} from "@material-ui/core";

const BoldTypography = (props) => {
    return (
        <Typography style={{fontWeight: 600, color: props.color}} {...props}>
            {props.children}
        </Typography>);
};



export default BoldTypography;
