import React from 'react';
import {Typography} from "@material-ui/core";

const BoldTypography = (props) => {
    return (
        <Typography {...props} style={{fontWeight: 600, ...props.style}}>
            {props.children}
        </Typography>);
};


export default BoldTypography;
