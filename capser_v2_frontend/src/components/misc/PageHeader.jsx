import React from 'react';
import PropTypes from 'prop-types';
import {IconButton, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';

const PageHeader = ({title, font, showLogo, onBack}) => {
    const classes = mainStyles();
    return (
        <div className={[classes.header, classes.horizontalShine].join(' ')}>
            {onBack && <IconButton onClick={onBack} style={{marginLeft: 30}}><ArrowBackIosIcon/></IconButton>}
            {showLogo ? <img src={"/logo192.png"} style={{maxWidth: 80, padding: 10}}/> :
                <div style={{minHeight: 100, minWidth: 20}}/>}
            <Typography variant={"h4"}>{title}</Typography>
        </div>
    );
};

PageHeader.propTypes = {
    title: PropTypes.string.isRequired
};

export default PageHeader;
