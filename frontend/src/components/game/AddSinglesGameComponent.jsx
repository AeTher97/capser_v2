import React, {useEffect, useState} from 'react';
import {Button, Divider, Typography} from "@material-ui/core";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import mainStyles from "../../misc/styles/MainStyles";
import {useDispatch, useSelector} from "react-redux";
import {useSoloGamePost} from "../../data/SoloGamesData";
import {showError, showSuccess} from "../../redux/actions/alertActions";
import {useHistory} from "react-router-dom";
import UserFetchSelectField from "../../utils/UserFetchSelectField";

const AddSinglesGameComponent = ({
                                     type, choosePlayers = true, displayGameDataSection = true, showBorder = true,
                                     handleSaveExternal, overridePlayer1Name, overridePlayer2Name, onCancel,
                                     player1Sinks, player2Sinks, player1Points, player2Points, gameEventsList,
                                     disableEditing = false
                                 }) => {

    const classes = mainStyles()
    const [gameMode, setGameMode] = useState("SUDDEN_DEATH");
    const {postGame} = useSoloGamePost(type);


    const {userId} = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const history = useHistory();


    const [playerScore, setPlayerScore] = useState(player1Points ? player1Points : 0);
    const [playerSinks, setPlayerSinks] = useState(player1Sinks ? player1Sinks : 0);

    const [opponentScore, setOpponentScore] = useState(player2Points ? player2Points : 0);
    const [opponentSinks, setOpponentSinks] = useState(player2Sinks ? player2Sinks : 0);

    const [opponent, setOpponent] = useState(null);

    useEffect(() => {
        if(player1Points){
            setPlayerScore(player1Points);
        }
        if(player2Points){
            setOpponentScore(player2Points);
        }
        if(player1Sinks){
            setPlayerSinks(player1Sinks);
        }
        if(player2Sinks){
            setOpponentSinks(player2Sinks);
        }
    }, [player1Points, player2Points, player1Sinks, player2Sinks, gameEventsList]);


    const handleChange = (e) => {
        setGameMode(e.target.value);
    }

    const handleSave = () => {

        if (!opponent && choosePlayers) {
            dispatch(showError("You have to choose an opponent"));
            return;
        }

        const player1Stats = {
            playerId: userId,
            score: playerScore,
            sinks: playerSinks
        };

        let player2Stats;
        if (opponent) {
            player2Stats = {
                playerId: opponent.id,
                score: opponentScore,
                sinks: opponentSinks
            }
        } else {
            player2Stats = {
                playerId: undefined,
                score: opponentScore,
                sinks: opponentSinks
            }
        }

        const request = {
            gameMode: gameMode,
            player1Stats: player1Stats,
            player2Stats: player2Stats,
            gameEvents: gameEventsList ? gameEventsList.map(event => {
                if(event.userId === null){
                    event.userId = opponent.id;
                }
                return event;
            }) : []
        }
        if (handleSaveExternal) {
            handleSaveExternal(request);
            return;
        }

        console.log(request)
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

    return (
        <div style={{display: "flex", justifyContent: "center"}}>
            <div style={{maxWidth: 400, flex: 1}}>

                <div style={{padding: 8}} className={showBorder ? classes.standardBorder : null}>
                    {displayGameDataSection && <div
                        className={[classes.column].join(' ')}>
                        <Typography variant={"h5"}>Game Data</Typography>
                        <Divider/>
                        <div className={classes.margin}>
                            <Select style={{minWidth: 200}} value={gameMode}
                                    onChange={handleChange} disabled={disableEditing}>
                                <MenuItem value={"SUDDEN_DEATH"}>Sudden Death</MenuItem>
                                <MenuItem value={"OVERTIME"}>Overtime</MenuItem>
                            </Select>
                        </div>
                    </div>}

                    <div
                        className={[classes.column].join(' ')}>
                        <Typography variant={"h5"}>{overridePlayer1Name || "Player data"}</Typography>

                        <div className={classes.margin}>
                            <Typography>Points</Typography>
                            <Select style={{minWidth: 200}} value={playerScore} onChange={(e) => {
                                setPlayerScore(e.target.value)
                            }}disabled={disableEditing}>
                                {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                            </Select>
                        </div>

                        <div className={classes.margin}>
                            <Typography>Sinks</Typography>
                            <Select style={{minWidth: 200}} value={playerSinks} onChange={(e) => {
                                setPlayerSinks(e.target.value)
                            }} disabled={disableEditing}>
                                {getScoreOptions(21)}
                            </Select>
                        </div>
                    </div>

                    <div
                        className={[classes.column].join(' ')}>
                        <Typography variant={"h5"}>{overridePlayer2Name || "Opponent data"}</Typography>
                        {choosePlayers && <div className={classes.margin}>
                            <UserFetchSelectField label={"Select Opponent"} onChange={(value) => setOpponent(value)}/>
                        </div>}

                        <div className={classes.margin}>
                            <Typography>Points</Typography>

                            <Select value={opponentScore} onChange={(e) => {
                                setOpponentScore(e.target.value)
                            }} className={classes.width200} disabled={disableEditing}>
                                {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                            </Select>
                        </div>
                        <div className={classes.margin}>
                            <Typography>Sinks</Typography>
                            <Select className={classes.width200} value={opponentSinks}
                                    onChange={(e) => {
                                        setOpponentSinks(e.target.value)
                                    }} disabled={disableEditing}>
                                {getScoreOptions(27)}
                            </Select>
                        </div>
                    </div>
                </div>

                <div style={{display: 'flex', justifyContent: 'center', marginTop: 0}}>
                    <Button onClick={handleSave} disabled={!opponent && !handleSaveExternal}>Add a game</Button>
                    {onCancel &&
                        <Button variant={"outlined"} style={{marginLeft: 10}}  onClick={onCancel}>Cancel</Button>}
                </div>
                <div style={{height: 50}}/>
            </div>
        </div>
    );
};

export default AddSinglesGameComponent;
