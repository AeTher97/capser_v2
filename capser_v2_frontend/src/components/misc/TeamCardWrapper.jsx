import React from 'react';
import {useTeamData} from "../../data/TeamsData";
import TeamCard from "./TeamCard";

const TeamCardWrapper = ({teamId}) => {

    const {team} = useTeamData(teamId)
    return (
        <>
            {team && <TeamCard team={team}/>}
        </>
    );
};

export default TeamCardWrapper;