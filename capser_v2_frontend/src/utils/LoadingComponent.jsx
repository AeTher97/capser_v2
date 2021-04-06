import React from 'react';
import PropTypes from 'prop-types';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Typography} from "@material-ui/core";

const LoadingComponent = ({size = "medium", wrapper = true, noPadding=false, showText = true, fullHeight=false}) => {

    const classes = loadingStyles();
    let selected;

    switch (size) {
        case 'small' :
            selected = classes.small;
            break;
        case 'medium' :
            selected = classes.medium;
            break;
        case 'large' :
            selected = classes.large;
            break;

    }
    return (
        <div style={{padding: noPadding ? 0 : 50}}>
            {wrapper ? <div style={{display: 'flex', justifyContent: 'center', flexDirection: 'column',alignItems: 'center', width: '100%', height: fullHeight ? '100%':null}}>
                <img src={"/loading.svg"} className={selected} style={{margin: 10}}/>
                {showText && <Typography variant={"h6"} style={{fontWeight: 'bold'}}>Loading...</Typography>}

            </div> :  <img src={"/loading.svg"} className={selected} style={{margin: 10}}/>}
        </div>
    );
};

LoadingComponent.propTypes = {
    size: PropTypes.oneOf(['small', 'medium', 'large']),
    wrapper: PropTypes.bool
};

const loadingStyles = makeStyles(theme => ({
    small: {
        width: 25,
        height: 25
    },
    medium: {
        width: 50,
        height: 50
    },
    large: {
        width: 100,
        height: 100
    }
}))
export default LoadingComponent;
