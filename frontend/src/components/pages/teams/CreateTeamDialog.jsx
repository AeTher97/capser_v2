import React from 'react';
import TeamEdit from "./TeamEdit";

const CreateTeamDialog = ({open, setClose, createTeam}) => {
    return (
        <TeamEdit applyChange={createTeam} setClose={setClose} open={open}/>
    )
};

CreateTeamDialog.propTypes = {};

export default CreateTeamDialog;
