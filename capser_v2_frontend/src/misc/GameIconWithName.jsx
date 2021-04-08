import React from 'react';
import {Typography} from "@material-ui/core";
import {getGameTypeString} from "../utils/Utils";
import {getGameIcon} from "../components/game/GameComponent";

const GameIconWithName = ({gameType}) => {
    return (
        <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
            {getGameIcon(gameType)}
            <Typography
                style={{marginLeft: 5, textAlign: 'center'}}>{getGameTypeString(gameType)}</Typography>
        </div>
    );
};

export default GameIconWithName;
