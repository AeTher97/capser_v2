import React from 'react';
import {useHistory} from "react-router-dom";
import Tooltip from "@material-ui/core/Tooltip";
import Fade from "@material-ui/core/Fade";
import TeamCardWrapper from "./TeamCardWrapper";

const TeamTooltip = ({teamId, children}) => {
    const history = useHistory();
    return (
        <Tooltip title={<TeamCardWrapper teamId={teamId}/>}>
            <div style={{cursor: 'pointer'}} onClick={() => {
                // history.push(`/teams/${id}`)
            }}>
                {children}
            </div>
        </Tooltip>
    );
};

export default TeamTooltip;