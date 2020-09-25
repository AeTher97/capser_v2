import React from 'react';
import PropTypes from 'prop-types';
import TeamEdit from "./TeamEdit";

const EditTeamDialog =  ({open, setClose, editTeam, team}) => {
    return (
        <TeamEdit applyChange={editTeam} setClose={setClose} open={open} team={team}/>
    )
};

EditTeamDialog.propTypes = {

};

export default EditTeamDialog;
