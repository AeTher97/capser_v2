import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import useFieldValidation from "../../../utils/useFieldValidation";
import {validateLength} from "../../../utils/Validators";
import mainStyles from "../../../misc/styles/MainStyles";
import {showError} from "../../../redux/actions/alertActions";
import Dialog from "@material-ui/core/Dialog";
import {Divider, Grid, IconButton, Typography} from "@material-ui/core";
import ValidatedField from "../../misc/ValidatedField";
import CloseIcon from "@material-ui/icons/Close";
import FetchSelectField from "../../misc/FetchSelectField";
import Button from "@material-ui/core/Button";
import {fetchUsername, useUsernameFetch} from "../../../data/UsersFetch";

const TeamEdit = ({applyChange, setClose, open, team = {name: '', playerList: []}}) => {
    const {userId, email} = useSelector(state => state.auth);
    const [disabled, setDisabled] = useState(false);
    const dispatch = useDispatch();
    const external = team.playerList.length > 0;

    const [usernames, setUsernames] = useState([]);

    const classes = mainStyles();

    const nameField = useFieldValidation(team.name, (value) => () => {
        return validateLength(value, 5)
    })
    nameField.showError = true;

    const initialState = [{
        username: email,
        id: userId,
        deletable: false
    }]


    const findUsername = (id) => {
        const obj = usernames.find(o => o.id === id);
        if (obj) {
            return obj.username;
        } else {
            return ''
        }
    }

    useEffect(() => {
        if (external) {
            Promise.all(team.playerList.map(player => {
                return fetchUsername(player)
            })).then((value) => {
                setUsernames(value.map(user => {
                    return {id: user.data.id, username: user.data.username}
                }));
            })
        }
    }, [])

    useEffect(() => {
        if (external) {
            setPlayers(players.map(player => {
                player.username = findUsername(player.id);
                return player;
            }))
        }
    }, [usernames])


    const [players, setPlayers] = useState(external ? team.playerList.map(player => {
        const deletable = player !== userId;
        return {
            username: player,
            id: player,
            deletable: deletable
        }
    }) : initialState)


    const addUser = (user) => {
        const copy = players.slice();
        copy.push({
            username: user.username,
            id: user.id,
            deletable: true
        })
        if (copy.length === 2) {
            setDisabled(true)
        }
        setPlayers(copy);
    }

    const deleteUser = (id) => {
        let copy = players.slice();
        copy = copy.filter(player => player.id !== id);
        setPlayers(copy);
        if (copy.length < 2) {
            setDisabled(false)
        }
    }

    const closeWrapper = () => {
        setClose();
        setDisabled(false);
        setPlayers(initialState)
    }

    const save = () => {
        if (players.length !== 2) {
            dispatch(showError("Team must consist of two players"))
            return;
        }

        if (nameField.validate()()) {
            return;
        }
        applyChange({
            name: nameField.value,
            players: players.map(player => player.id)
        })
        closeWrapper();
    }


    const cancel = () => {
        closeWrapper();
    }

    return (
        <Dialog open={open} fullWidth id={"xd"}>
            <div className={[classes.paddedContent, classes.horizontalShine].join(' ')}
                 style={{backgroundColor: 'black'}}>

                <div className={classes.header}>
                    <Typography variant={"h5"} style={{marginRight: 10, flex: 1}}>Create Team</Typography>
                    <div style={{maxWidth: 150}}>
                        <ValidatedField field={nameField} label={'Team Name'}/>
                    </div>
                </div>

                <div className={classes.header} style={{marginTop: 20}}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={6} style={{minHeight: 400}}>
                            <div>
                                <Typography variant={"h6"}>Players</Typography>
                                <Divider/>
                                {players.map(player => {
                                    return <div key={player.id} style={{padding: 5}} className={classes.header}>
                                        <Typography style={{flex: 1}}>{player.username}</Typography>
                                        {player.deletable &&
                                        <IconButton style={{color: 'red', padding: 0}} onClick={() => {
                                            deleteUser(player.id)
                                        }}>
                                            <CloseIcon fontSize={"small"}/>
                                        </IconButton>}
                                    </div>
                                })}
                            </div>
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <div style={{minHeight: 100}}>
                                <Typography variant={"h6"}>Search</Typography>
                                <Divider style={{marginBottom: 5}}/>
                                <FetchSelectField label={"Search Player"} onChange={addUser}
                                                  url={"/users/search"}
                                                  nameParameter={"username"}
                                                  disabled={disabled}
                                                  clearOnChange/>
                            </div>
                        </Grid>
                    </Grid>
                </div>
                <Button style={{marginRight: 10}} onClick={save}>
                    Save
                </Button>
                <Button variant={"outlined"} onClick={cancel}>
                    Cancel
                </Button>
            </div>
        </Dialog>
    );

};

TeamEdit.propTypes = {};

export default TeamEdit;
