import React from 'react';
import {Typography} from "@material-ui/core";

const LiveGameScreen = () => {
    return (
        <div style={{display: "flex", justifyContent: "center", flexDirection: "column"}}>
            <div style={{maxWidth: 400, flex: 1}}><Typography>XD</Typography>
            </div>
            <div style={{width: 200, height: 200, backgroundColor: "red",
            borderRadius: "0,0,5px,5px"}}>

            </div>
        </div>
    );
};

export default LiveGameScreen;