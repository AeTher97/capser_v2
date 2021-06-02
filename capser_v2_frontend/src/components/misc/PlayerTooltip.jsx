import React from 'react';
import Tooltip from "@material-ui/core/Tooltip";
import PlayerCard from "./PlayerCard";
import mainStyles from "../../misc/styles/MainStyles";
import {useUserData} from "../../data/UserData";
import Fade from "@material-ui/core/Fade";
import {useHistory} from "react-router-dom";

const PlayerTooltip = ({playerId, children, gameType}) => {

    const classes = mainStyles();
    const history = useHistory();
    const {data} = useUserData(playerId);
    return (
        <Tooltip title={<PlayerCard player={data} type={gameType}/>}
                 TransitionComponent={Fade}>
            <div style={{cursor: 'pointer'}} onClick={() => {
                history.push(`/players/${playerId}`)
            }}>
                {children}
            </div>
        </Tooltip>
    );
};

PlayerTooltip.propTypes = {

};

export default PlayerTooltip;
