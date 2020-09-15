import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import FetchSelectField from "../../misc/FetchSelectField";
import {Button, Divider, Grid, Typography} from "@material-ui/core";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import mainStyles from "../../../misc/styles/MainStyles";
import addGameStyles from "../../../misc/styles/AddGameStyles";
import {useDispatch, useSelector} from "react-redux";
import {useGamePost} from "../../../data/Game";
import {showSuccess} from "../../../redux/actions/alertActions";
import {useHistory} from "react-router-dom";

const AddSinglesGameComponent = () => {

    const classes = mainStyles()
    const addGameClasses = addGameStyles();
    const [gameMode, setGameMode] = useState("SUDDEN_DEATH");

    const {userId} = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const history = useHistory();


    const [playerScore, setPlayerScore] = useState(0);
    const [playerSinks, setPlayerSinks] = useState(0);
    const [opponentScore, setOpponentScore] = useState(0);

    const [opponentSinks, setOpponentSinks] = useState(0);
    const [opponent, setOpponent] = useState(null);

    const postGame = useGamePost('SINGLES');

    const handleChange = (e) => {
        setGameMode(e.target.value);
    }


    const handleSave = () => {
        console.log(opponent)
        const request = {
            gameMode: gameMode,
            player1Stats: {
                playerId: userId,
                score: playerScore,
                sinks: playerSinks
            },
            player2Stats: {
                playerId: opponent.id,
                score: opponentScore,
                sinks: opponentSinks
            },
            gameEventList: []
        }

        postGame(request).then(() => {
            console.log("posted")
            dispatch(showSuccess("Game posted"))
            history.push('/')
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
        <div>
            <PageHeader title={"Add singles game"} onBack={() => {history.push('/singles')}}/>

            <div style={{padding: 8}}>
                <Grid container  spacing={2}>
                    <Grid item md={4} sm={12} xs={12}>
                        <div className={[classes.column, classes.height700,classes.squareShine, classes.neon].join(' ')}>
                            <Typography variant={"h5"}>Game Data</Typography>
                            <Divider/>
                            <div className={classes.margin}>
                                <Select label={"Game mode"} style={{minWidth: 200}} value={gameMode}
                                        onChange={handleChange}>
                                    <MenuItem value={"SUDDEN_DEATH"}>Sudden Death</MenuItem>
                                    <MenuItem value={"OVERTIME"}>Overtime</MenuItem>
                                </Select>
                            </div>
                        </div>
                    </Grid>
                    <Grid item md={4} sm={12} xs={12}>
                        <div className={[classes.column, classes.height700,classes.squareShine, classes.neon].join(' ')}>
                            <Typography variant={"h5"}>Player data</Typography>

                            <div className={classes.margin}>
                                <Typography>Points</Typography>
                                <Select label={"Points"} style={{minWidth: 200}} value={playerScore} onChange={(e) => {
                                    setPlayerScore(e.target.value)
                                }}>
                                    {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                                </Select>
                            </div>

                            <div className={classes.margin}>
                                <Typography>Sinks</Typography>
                                <Select label={"Sinks"} style={{minWidth: 200}} value={playerSinks} onChange={(e) => {
                                    setPlayerSinks(e.target.value)
                                }}>
                                    {getScoreOptions(21)}
                                </Select>
                            </div>
                        </div>
                    </Grid>

                    <Grid item md={4} sm={12} xs={12}>
                        <div className={[classes.column, classes.height700, classes.squareShine, classes.neon].join(' ')}>
                            <Typography variant={"h5"}>Opponent data</Typography>

                            <div className={classes.margin}>
                                <FetchSelectField label={"Select Opponent"} onChange={(value) => setOpponent(value)}
                                                  url={"/users/search"}
                                                  nameParameter={"username"}/>
                            </div>

                            <div className={classes.margin}>
                                <Typography>Points</Typography>

                                <Select label={"Points"} value={opponentScore} onChange={(e) => {
                                    setOpponentScore(e.target.value)
                                }} className={classes.width200}>
                                    {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                                </Select>
                            </div>
                            <div className={classes.margin}>
                                <Typography>Sinks</Typography>
                                <Select label={"Sinks"} className={classes.width200} value={opponentSinks}
                                        onChange={(e) => {
                                            setOpponentSinks(e.target.value)
                                        }}>
                                    {getScoreOptions(21)}
                                </Select>
                            </div>
                        </div>
                    </Grid>
                </Grid>
            </div>

            <div style={{display: 'flex', justifyContent: 'center', marginTop: 20}}>
                <Button onClick={handleSave}>Add a game</Button>
            </div>
        </div>
    );
};

export default AddSinglesGameComponent;
