import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import {useHistory} from "react-router-dom";
import {useUserData} from "../../data/UserData";
import Tooltip from "@material-ui/core/Tooltip";
import PlayerCard from "./PlayerCard";
import Fade from "@material-ui/core/Fade";
import {useTeamData} from "../../data/TeamsData";
import TeamCard from "./TeamCard";

const TeamTooltip = ({teamId,children}) => {
    const history = useHistory();
    const {team} = useTeamData(teamId);
    return (
        <>{team &&
        <Tooltip title={<TeamCard team={team}/>}
                 TransitionComponent={Fade}>
            <div style={{cursor: 'pointer'}} onClick={() => {
                // history.push(`/teams/${id}`)
            }}>
                {children}
            </div>
        </Tooltip>}
        </>
    );
};

export default TeamTooltip;