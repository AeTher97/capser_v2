import React from 'react';
import Tooltip from "@material-ui/core/Tooltip";
import {useHistory} from "react-router-dom";
import PlayerCardWrapper from "./PlayerCardWrapper";

const PlayerTooltip = ({playerId, children, gameType}) => {

    const history = useHistory();
    return (
        <Tooltip title={<PlayerCardWrapper type={gameType} playerId={playerId}/>}
         >
            <div style={{cursor: 'pointer'}} onClick={() => {
                history.push(`/players/${playerId}`)
            }}>
                {children}
            </div>
        </Tooltip>);
};


export default PlayerTooltip;
