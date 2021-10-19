import React from 'react';
import Tooltip from "@material-ui/core/Tooltip";
import mainStyles from "../../misc/styles/MainStyles";
import {useHistory} from "react-router-dom";
import PlayerCardWrapper from "./PlayerCardWrapper";

const PlayerTooltip = ({playerId, children, gameType}) => {

    const classes = mainStyles();
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
