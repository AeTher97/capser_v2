import React from 'react';
import PropTypes from 'prop-types';
import {Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";

const PageHeader = ({title, font, showLogo, icon, noSpace, fontWeight = 400}) => {
    const classes = mainStyles();
    const theme = useTheme();

    const style = font ? {fontFamily: font, fontWeight: fontWeight} : {};
    return (
        <div style={{paddingLeft: 10, borderBottom: '1px solid ' + theme.palette.divider, height: 94, fontFamily: font}}
             className={[classes.header].join(' ')}>
            {icon && <div style={{padding: 10, color: 'white'}}>{icon}</div>}
            {showLogo ? <img src={"/logo192.png"} style={{maxWidth: 80, padding: 10}}/> :
                <div style={{minHeight: 100}}/>}}
            <Typography variant={"h4"} style={style}>{title}</Typography>
        </div>
    );
};

PageHeader.propTypes = {
    title: PropTypes.string.isRequired
};

export default PageHeader;
