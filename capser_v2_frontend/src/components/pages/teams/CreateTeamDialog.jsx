import React, {useState} from 'react';
import Dialog from "@material-ui/core/Dialog";
import {Divider, Grid, IconButton, Typography} from "@material-ui/core";
import mainStyles from "../../../misc/styles/MainStyles";
import FetchSelectField from "../../misc/FetchSelectField";
import Button from "@material-ui/core/Button";
import {useDispatch, useSelector} from "react-redux";
import CloseIcon from '@material-ui/icons/Close';
import useFieldValidation from "../../../utils/useFieldValidation";
import {validateLength} from "../../../utils/Validators";
import ValidatedField from "../../misc/ValidatedField";
import {showError} from "../../../redux/actions/alertActions";
import TeamEdit from "./TeamEdit";

const CreateTeamDialog = ({open, setClose, createTeam}) => {
    return (
        <TeamEdit applyChange={createTeam} setClose={setClose} open={open}/>
    )
};

CreateTeamDialog.propTypes = {};

export default CreateTeamDialog;
