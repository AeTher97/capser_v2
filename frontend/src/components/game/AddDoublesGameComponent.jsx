import React, {useEffect, useState} from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import {useDispatch, useSelector} from "react-redux";
import {useHistory} from "react-router-dom";
import {showError, showSuccess} from "../../redux/actions/alertActions";
import MenuItem from "@material-ui/core/MenuItem";
import {Button, Divider, Typography} from "@material-ui/core";
import Select from "@material-ui/core/Select";
import FetchSelectField from "../../utils/FetchSelectField";
import {useMultipleGamePost} from "../../data/MultipleGamesData";
import {usePlayerTeams} from "../../data/TeamsData";
import {fetchUsername} from "../../data/UsersFetch";

const AddDoublesGameComponent = ({
                                     showBorder = true,
                                     externalSave,
                                     team1,
                                     team2,
                                     overrideTeam1Name,
                                     overrideTeam2Name,
                                     chooseTeams,
                                     onCancel
                                 }) => {
    const classes = mainStyles()
    const [gameMode, setGameMode] = useState("SUDDEN_DEATH");
    const {postGame} = useMultipleGamePost("DOUBLES");


    const {userId} = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const history = useHistory();

    const {teams, loading} = usePlayerTeams(externalSave ? null : userId);


    const [teamScore, setTeamScore] = useState(0);
    const [opposingTeamScore, setOpposingTeamScore] = useState(0);
    const [usernames, setUsernames] = useState([]);

    const [team, setTeam] = useState('none');
    const [opposingTeam, setOpposingTeam] = useState(null);

    const [teamStats, setTeamStats] = useState([]);
    const [opposingTeamStats, setOpposingTeamStats] = useState([]);

    const findUsername = (id) => {
        try {
            return usernames.find(username => username.id === id).username;
        } catch (e) {
            return ''
        }
    }

    useEffect(() => {
        if (team1 && team2) {
            handleTeamChange({target: {value: team1}})
            handleOpponentTeamChange(team2)
            updateUsernames([...team1.playerList, ...team2.playerList])
        }
    }, [externalSave, team1, team2])


    const handleChange = (e) => {
        setGameMode(e.target.value);
    }
    const updateUsernames = (playerList) => {
        Promise.all(playerList.map(player => fetchUsername(player))).then(result => {
            let copy = usernames.slice();
            copy = copy.concat(result.map(request => request.data));
            setUsernames(copy);
        })
    }
    const handleTeamChange = (e) => {
        if (e.target.value === 'none') {
            setTeam('none');
            setTeamStats([]);
            updateUsernames([]);
            return;
        }
        if (externalSave) {
            setTeam(e.target.value.id);
            updateUsernames(e.target.value.playerList);
            setTeamStats(e.target.value.playerList.map(player => {
                return {
                    playerId: player,
                    score: 0,
                    sinks: 0
                }
            }))
        } else {
            setTeam(e.target.value);
            updateUsernames(teams.find(team => team.id === e.target.value).playerList);
            setTeamStats(teams.find(team => team.id === e.target.value).playerList.map(player => {
                return {
                    playerId: player,
                    score: 0,
                    sinks: 0
                }
            }))
        }

    }

    if (teams.length > 0 && team === null) {
        setTeam(teams[0].id)
        updateUsernames(teams[0].playerList)
        setTeamStats(teams[0].playerList.map(player => {
            return {
                playerId: player,
                score: 0,
                sinks: 0
            }
        }))
    }

    const handleOpponentTeamChange = (e) => {
        if (!e) {
            updateUsernames([]);
            setOpposingTeam(null);
            setOpposingTeamStats([]);
            return;
        }
        setOpposingTeam(e);
        updateUsernames(e.playerList);
        setOpposingTeamStats(e.playerList.map(player => {
            return {
                playerId: player,
                score: 0,
                sinks: 0
            }
        }))
    }

    const handleSave = () => {

        if (!opposingTeam) {
            dispatch(showError("You have to choose an opposing team"));
            return;
        }


        const request = {
            gameMode: gameMode,
            team1Score: teamScore,
            team2Score: opposingTeamScore,
            team1: externalSave ? team1.id : team,
            team2: opposingTeam.id,
            team1Players: externalSave ? team1.playerList : teams.find(o => o.id === team).playerList,
            team2Players: opposingTeam.playerList,
            playerStatsDtos: opposingTeamStats.concat(teamStats),
            gameEventList: []
        }

        if (externalSave) {
            externalSave(request);
            return;
        }

        postGame(request).then(() => {
            dispatch(showSuccess("Game posted"))
            history.push('/')
        }).catch(e => {
            dispatch(showError(e.response.data.error));
        });
    }


    const getScoreOptions = (number) => {
        const numbers = [];
        for (let i = 0; i <= number; i++) {
            numbers.push(i);
        }
        return (numbers.map(number => {
            return (
                <MenuItem key={number} value={number}>
                    {number}
                </MenuItem>)
        }))
    }


    const setTeamStatsValue = (id, value, name) => {
        const copy = teamStats.slice();
        setTeamStats(copy.map(stats => {
            if (stats.playerId === id) {
                stats[name] = value;
            }
            return stats;
        }))
    }

    const setOpposingTeamStatsValue = (id, value, name) => {
        const copy = opposingTeamStats.slice();
        setOpposingTeamStats(copy.map(stats => {
            if (stats.playerId === id) {
                stats[name] = value;
            }
            return stats;
        }))
    }

    return (
        <div style={{display: "flex", justifyContent: "center"}}>
            <div style={{maxWidth: 600, flex: 1}}>

                <div style={{padding: 8, display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around'}}
                     className={showBorder ? classes.standardBorder : null}>
                    <div>
                        <div
                            className={[classes.column].join(' ')}>
                            <Typography variant={"h5"}>Game Data</Typography>
                            <Divider/>
                            <div className={classes.margin}>
                                <Select style={{minWidth: 200}} value={gameMode}
                                        onChange={handleChange}>
                                    <MenuItem value={"SUDDEN_DEATH"}>Sudden Death</MenuItem>
                                    <MenuItem value={"OVERTIME"}>Overtime</MenuItem>
                                </Select>
                        </div>
                    </div>
                    <div
                        className={[classes.column].join(' ')}>
                        <Typography variant={"h5"}>{!externalSave ? 'Player Team Data' : overrideTeam1Name}</Typography>
                        <div className={classes.margin}>
                            {teams.length === 0 && !externalSave && !loading &&
                            <Typography color={"primary"} variant={"caption"}>No teams, create one in Teams
                                page</Typography>}
                            {teams.length > 0 &&
                            <Select className={classes.width200} value={team} onChange={handleTeamChange}>
                                <MenuItem value={'none'}>Select your team</MenuItem>
                                {teams.map(team => <MenuItem key={team.id} value={team.id}>{team.name}</MenuItem>)}
                            </Select>}
                        </div>
                        <div className={classes.margin}>
                            <Typography>Points</Typography>
                            <Select style={{minWidth: 200}} value={teamScore} onChange={(e) => {
                                setTeamScore(e.target.value)
                            }}>
                                {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                            </Select>
                        </div>
                        {team && teamStats.map(stats => <div key={stats.playerId}><Typography
                            variant={"h6"}>{findUsername(stats.playerId)} Stats</Typography>
                            <div className={classes.margin}>
                                <Typography>Points</Typography>
                                <Select style={{minWidth: 200}}
                                        value={stats.score}
                                        onChange={(e) => {
                                            setTeamStatsValue(stats.playerId, e.target.value, 'score');
                                        }}>
                                    {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                                </Select>
                            </div>

                            <div className={classes.margin}>
                                <Typography>Sinks</Typography>
                                <Select style={{minWidth: 200}}
                                        value={stats.sinks}
                                        onChange={(e) => {
                                            setTeamStatsValue(stats.playerId, e.target.value, 'sinks');
                                        }}>
                                    {getScoreOptions(21)}
                                </Select>
                            </div>
                        </div>)}

                    </div>
                    </div>

                    <div
                        className={[classes.column].join(' ')}>
                        <Typography
                            variant={"h5"}>{!externalSave ? 'Opposing Team Data' : overrideTeam2Name}</Typography>

                        {!externalSave && <div className={classes.margin}>
                            <FetchSelectField label={"Opposing Team"}
                                              onChange={handleOpponentTeamChange}
                                              url={"/teams/search"}
                                              nameParameter={"name"}/>
                        </div>}

                        <div className={classes.margin}>
                            <Typography>Points</Typography>

                            <Select value={opposingTeamScore} onChange={(e) => {
                                setOpposingTeamScore(e.target.value)
                            }} className={classes.width200}>
                                {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                            </Select>
                        </div>
                        {opposingTeam && opposingTeamStats.map(stats => <div key={stats.playerId}><Typography
                            variant={"h6"}>{findUsername(stats.playerId)} Stats</Typography>

                            <div className={classes.margin}>
                                <Typography>Points</Typography>
                                <Select style={{minWidth: 200}}
                                        value={stats.score}
                                        onChange={(e) => {
                                            setOpposingTeamStatsValue(stats.playerId, e.target.value, 'score');
                                        }}>
                                    {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                                </Select>
                            </div>

                            <div className={classes.margin}>
                                <Typography>Sinks</Typography>
                                <Select style={{minWidth: 200}}
                                        value={stats.sinks}
                                        onChange={(e) => {
                                            setOpposingTeamStatsValue(stats.playerId, e.target.value, 'sinks');
                                        }}>
                                    {getScoreOptions(21)}
                                </Select>
                            </div>
                        </div>)}
                    </div>
                </div>
                <div style={{display: 'flex', justifyContent: 'center', marginTop: 0}}>
                    <Button onClick={handleSave}>Add a game</Button>
                    {onCancel &&
                    <Button variant={"outlined"} style={{marginLeft: 10}} onClick={onCancel}>Cancel</Button>}
                </div>
                {!externalSave && <div style={{height: 100}}/>}
            </div>
        </div>
    );
};

AddDoublesGameComponent.propTypes = {};

export default AddDoublesGameComponent;
