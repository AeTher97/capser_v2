import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import {useHistory} from "react-router-dom";
import {useUserData} from "../../data/UserData";
import Tooltip from "@material-ui/core/Tooltip";
import PlayerCard from "./PlayerCard";
import Fade from "@material-ui/core/Fade";
import {useTeamData} from "../../data/TeamsData";
import TeamCard from "./TeamCard";
import TeamCardWrapper from "./TeamCardWrapper";

const TeamTooltip = ({teamId,children}) => {
    const history = useHistory();
    return (
        <Tooltip title={<TeamCardWrapper teamId={teamId}/>}
                 TransitionComponent={Fade}>
            <div style={{cursor: 'pointer'}} onClick={() => {
                // history.push(`/teams/${id}`)
            }}>
                {children}
            </div>
        </Tooltip>
    );
};

export default TeamTooltip;