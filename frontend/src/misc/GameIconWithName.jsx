import React from 'react';
import {getGameTypeString} from "../utils/Utils";
import {getGameIcon} from "../components/game/GameComponent";
import BoldTyphography from "../components/misc/BoldTyphography";

const GameIconWithName = ({gameType}) => {
    return (
        <div style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
            color: "white",
            alignItems: 'center'
        }}>
            <div style={{marginRight: 5, display: "flex", flexDirection: "row", justifyContent: "center"}}>
                {getGameIcon(gameType)}
            </div>
            <BoldTyphography>{getGameTypeString(gameType)}</BoldTyphography>
        </div>
    );
};

export default GameIconWithName;
