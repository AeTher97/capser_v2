import React from 'react';
import PropTypes from 'prop-types';
import mainStyles from "../../misc/styles/MainStyles";

const LogoComponent = ({size = "medium"}) => {
    const classes = mainStyles();

    return (
        <div className={classes.centeredRow}>
            <img src={"/logo192.png"} style={{maxWidth: 75}}/>
        </div>
    );
};

LogoComponent.propTypes = {
    size: PropTypes.oneOf(["small", "medium", "large"])
};

export default LogoComponent;
