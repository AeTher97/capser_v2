import React from 'react';
import PropTypes from 'prop-types';
import {IconButton, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';

const PageHeader = ({title, font, showLogo, icon, noSpace}) => {
    const classes = mainStyles();
    return (
        <div style={{paddingLeft: 10}} className={[classes.header, classes.horizontalShine].join(' ')}>
            {icon && <div style={{padding: 10, color: 'white'}}>{icon}</div>}
            {showLogo ? <img src={"/logo192.png"} style={{maxWidth: 80, padding: 10}}/> :
                <div style={{minHeight: 100}}/>}}
            <Typography variant={"h4"}>{title}</Typography>
        </div>
    );
};

PageHeader.propTypes = {
    title: PropTypes.string.isRequired
};

export default PageHeader;
