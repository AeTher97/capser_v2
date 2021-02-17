import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import PeopleOutlineIcon from '@material-ui/icons/PeopleOutline';
import mainStyles from "../../../misc/styles/MainStyles";
import Grid from "@material-ui/core/Grid";
import {Divider, IconButton, Typography} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import CreateTeamDialog from "./CreateTeamDialog";
import {usePlayerTeams} from "../../../data/TeamsData";
import {useSelector} from "react-redux";
import CloseIcon from '@material-ui/icons/Close';
import YesNoDialog from "../../misc/YesNoDialog";
import TeamStats from "./TeamStats";
import LoadingComponent from "../../../utils/LoadingComponent";
import EditIcon from "@material-ui/icons/Edit";
import EditTeamDialog from "./EditTeamDialog";


const TeamsComponent = () => {

    const {userId} = useSelector(state => state.auth);
    const classes = mainStyles();
    const [open, setOpen] = useState(false);

    const [deleteOpen, setDeleteOpen] = useState(false);
    const [editOpen, setEditOpen] = useState(false);
    const [editedTeam, setEditedTeam] = useState(null);

    const [selectedTeam, setSelectedTeam] = useState(null);
    const [question, setQuestion] = useState("")
    const [deleteTeamFunc, setDeleteTeam] = useState(() => () => {
    })

    const {teams, createTeam, deleteTeam, loading, updateTeam} = usePlayerTeams(userId)


    const setClose = () => {
        setOpen(false);
    }

    const setEditClose = () => {
        setEditedTeam(null);
        setEditOpen(false);
    }

    const clickDelete = (id, name) => {
        setQuestion(`Do you really want to delete team ${name}`);
        setDeleteTeam(() => () => {
            deleteTeam(id);
            setSelectedTeam(null);

        })
        setDeleteOpen(true);
    }

    const clickEdit = (team) => {
        setEditedTeam(team);
        setEditOpen(true)
    }


    return (
        <div>
            <PageHeader title={"Teams"} icon={<PeopleOutlineIcon fontSize={"large"}/>}/>
            <div className={classes.root}>
                <Grid container spacing={2}>
                    <Grid item md={4} sm={12} xs={12}>
                        {!loading ? <div style={{minHeight: 500, padding: 0}}
                                         className={classes.standardBorder}>
                            <div className={classes.header} style={{padding: 10, alignItems: 'center'}}>
                                <Typography variant={"h5"} style={{flex: 1}}>Your Teams</Typography>
                                <div>
                                    <Button onClick={() => setOpen(true)}>Create new</Button>
                                </div>
                            </div>
                            <Divider style={{marginTop: 5}}/>
                            {teams.map(team => {
                                if (!team.active) {
                                    return null;
                                }
                                return <div key={team.id} style={{cursor: "pointer", padding: 10}}>
                                    <div className={classes.header} style={{padding: 5}}>
                                        <Typography color={"primary"}
                                                    className={classes.link}
                                                    style={{flex: 1}}
                                                    onClick={() => {
                                                        setSelectedTeam(team)
                                                    }}>
                                            {team.name}
                                        </Typography>
                                        <IconButton style={{padding: 0, marginRight: 10}}
                                                    onClick={() => {
                                                        clickEdit(team)
                                                    }}>
                                            <EditIcon fontSize={"small"}/>
                                        </IconButton>
                                        <IconButton color={"primary"} style={{padding: 0}}
                                                    onClick={() => {
                                                        clickDelete(team.id, team.name)
                                                    }}>
                                            <CloseIcon/>
                                        </IconButton>
                                    </div>
                                </div>
                            })}
                        </div> : <LoadingComponent/>}
                    </Grid>
                    <Grid item md={8} sm={12} xs={12}>
                        <div style={{padding: 10}} className={classes.standardBorder}>
                            <div style={{minHeight: 480}}>
                                {!selectedTeam ? <div style={{
                                    display: 'flex',
                                    justifyContent: 'center',
                                    flexDirection: "column",
                                    alignItems: 'center',
                                    height: 480
                                }}>
                                    <img src={"/dd.svg"} style={{maxWidth: 250}}/>
                                    {teams.length > 0 ?
                                        <Typography variant={"h5"}>Select a team to see stats</Typography> :
                                        <Typography variant={"h5"}>No teams, create one</Typography>}
                                </div> : <TeamStats team={selectedTeam}/>}
                            </div>
                        </div>
                    </Grid>
                </Grid>
            </div>

            <CreateTeamDialog open={open} setClose={setClose} createTeam={createTeam}/>
            {editedTeam && <EditTeamDialog open={editOpen} setClose={setEditClose} editTeam={(value) => {
                updateTeam(editedTeam.id, value)
                setEditedTeam(null)
            }} team={editedTeam}/>}
            <YesNoDialog onYes={deleteTeamFunc} onNo={() => {
            }} question={question} open={deleteOpen} setOpen={setDeleteOpen}/>

        </div>
    );
};

export default TeamsComponent;
