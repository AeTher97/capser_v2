import React from 'react';
import {useTheme} from "@material-ui/core";

const BracketPath = ({height, width, left, top, pathType, bye = false}) => {
    const theme = useTheme();
    const color = bye ? '#1e2428' : theme.palette.divider
    return (<>
        {pathType==="top" ? <div style={{borderBottom: "1px solid " + theme.palette.divider,borderLeft: "1px solid " + color, height: height, width: width, left:left, top:top, position: "absolute"}}/> :
            <div style={{borderLeft: "1px solid " + color, height: height, width: width, left:left, top:top, position: "absolute"}}/>}
    </>);
};

export default BracketPath;
