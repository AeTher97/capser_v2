import React from 'react';
import PropTypes from 'prop-types';
import {Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";

const PageHeader = ({title, font, showLogo}) => {
    const classes = mainStyles();

    return (
        <div className={classes.header} style={{
            backgroundColor: 'rgba(0,0,0,0.8)',
            backgroundImage: `url(/bars.svg)`,
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'right'
        }}>
            {showLogo ? <img src={"/logo192.png"} style={{maxWidth: 80, padding: 10}}/> : <div  style={{minHeight: 100, minWidth: 20}}/>}
            <Typography variant={"h4"}>{title}</Typography>
        </div>
    );
};

PageHeader.propTypes = {
    title: PropTypes.string.isRequired
};

export default PageHeader;
