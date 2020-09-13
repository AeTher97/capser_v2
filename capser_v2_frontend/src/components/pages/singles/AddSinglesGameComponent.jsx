import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import FetchSelectField from "../../misc/FetchSelectField";
import {Button, Divider, Grid, Typography} from "@material-ui/core";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import mainStyles from "../../../misc/styles/MainStyles";

const AddSinglesGameComponent = () => {

    const classes = mainStyles()
    const [gameMode, setGameMode] = useState("SUDDEN_DEATH");

    const [playerScore, setPlayerScore] = useState(0);
    const [playerSinks, setPlayerSinks] = useState(0);
    const [opponentScore, setOpponentScore] = useState(0);

    const [opponentSinks, setOpponentSinks] = useState(0);

    const handleChange = (e) => {
        setGameMode(e.target.value);
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
            <PageHeader title={"Add singles game"}/>
            <div className={classes.leftOrientedWrapper}>
                <Typography variant={"h5"}>Game Data</Typography>
                <Divider/>

                <Select label={"Game mode"} style={{minWidth: 200}} value={gameMode} onChange={handleChange}>
                    <MenuItem value={"SUDDEN_DEATH"}>Sudden Death</MenuItem>
                    <MenuItem value={"OVERTIME"}>Overtime</MenuItem>
                </Select>
                <Grid container spacing={2}>
                    <Grid item sm={6}>
                        <div className={classes.column}>
                            <Typography variant={"h5"}>Player data</Typography>


                            <Typography>Points</Typography>

                            <Select label={"Points"} style={{minWidth: 200}} value={playerScore} onChange={(e) => {
                                setPlayerScore(e.target.value)
                            }}>
                                {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                            </Select>
                            <Typography>Sinks</Typography>

                            <Select label={"Sinks"} style={{minWidth: 200}} value={playerSinks} onChange={(e) => {
                                setPlayerSinks(e.target.value)
                            }}>
                                {getScoreOptions(21)}
                            </Select>
                        </div>
                    </Grid>

                    <Grid item sm={6}>
                        <Typography variant={"h5"}>Opponent data</Typography>
                        <FetchSelectField label={"Opponent"} onChange={(value) => console.log(value)}
                                          url={"/users/search"}
                                          nameParameter={"username"}/>

                        <Typography>Points</Typography>

                        <Select label={"Points"} style={{minWidth: 200}} value={opponentScore} onChange={(e) => {
                            setOpponentScore(e.target.value)
                        }}>
                            {getScoreOptions(gameMode === 'SUDDEN_DEATH' ? 11 : 21)}
                        </Select>
                        <Typography>Sinks</Typography>

                        <Select label={"Sinks"} style={{minWidth: 200}} value={opponentSinks} onChange={(e) => {
                            setOpponentSinks(e.target.value)
                        }}>
                            {getScoreOptions(21)}
                        </Select>
                    </Grid>
                </Grid>
                <Button>Add a game</Button>
            </div>
        </div>
    );
};

export default AddSinglesGameComponent;
