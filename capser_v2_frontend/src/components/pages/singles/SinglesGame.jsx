import React from 'react';
import {useSoloGame} from "../../../data/SoloGamesData";
import {useHistory, useLocation, useParams} from 'react-router-dom';
import PageHeader from "../../misc/PageHeader";
import LoadingComponent from "../../../utils/LoadingComponent";
import {Grid, IconButton, Typography} from "@material-ui/core";
import mainStyles from "../../../misc/styles/MainStyles";
import {getGameModeString, getGameTypeString} from "../../../utils/Utils";
import ArrowBackIcon from '@material-ui/icons/ArrowBack';


const SinglesGame = () => {

    const {gameId} = useParams();
    const location = useLocation();
    const classes = mainStyles();
    const history = useHistory();

    const {loading, game} = useSoloGame(location.pathname.split('/')[1], gameId)


    let player1Stats;
    let player2Stats;
    if (!loading && game) {
        player1Stats = game.gamePlayerStats.find(obj => obj.playerId === game.player1)
        player2Stats = game.gamePlayerStats.find(obj => obj.playerId === game.player2)
    }

    return (
        <div>
            {!loading && game ? <>
                    <PageHeader title={game.player1Name + ' vs ' + game.player2Name + '  '}/>
                    <div className={classes.root}>
                        <div className={classes.header}>
                            <IconButton onClick={() => history.push(`/${location.pathname.split('/')[1]}`)}>
                                <ArrowBackIcon/>
                            </IconButton>
                        </div>
                        <div className={classes.paddedContent}>
                            <Typography variant={"h6"} color={"primary"}>{getGameTypeString(game.gameType)}</Typography>
                            <Typography>{new Date(game.time).toUTCString()}</Typography>
                            <Typography>{getGameModeString(game.gameMode)}</Typography>
                        </div>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={12} md={6}>
                                <div
                                    className={[classes.paddedContent, game.winner === game.player1 ? classes.neon : null].join(' ')}>
                                    <Typography color={"primary"} variant={"h5"}>{game.player1Name}</Typography>
                                    {player1Stats.nakedLap &&
                                    <Typography variant={"h6"} color={"primary"}>Naked lap</Typography>}
                                    <Typography>Score: {player1Stats.score}</Typography>
                                    <Typography>Points change: {player1Stats.pointsChange.toFixed(2)}</Typography>
                                    <Typography>Beers downed: {player1Stats.beersDowned}</Typography>
                                    <Typography>Rebuttals: {player1Stats.rebuttals}</Typography>
                                    <Typography>Sinks: {player1Stats.sinks}</Typography>
                                </div>
                            </Grid>
                            <Grid item xs={12} sm={12} md={6}>
                                <div
                                    className={[classes.paddedContent, game.winner === game.player2 ? classes.neon : null].join(' ')}>
                                    <Typography color={"primary"} variant={"h5"}>{game.player2Name}</Typography>
                                    {player2Stats.nakedLap &&
                                    <Typography variant={"h6"} color={"primary"}>Naked lap</Typography>}
                                    <Typography>Score: {player2Stats.score}</Typography>
                                    <Typography>Points change: {player2Stats.pointsChange.toFixed(2)}</Typography>
                                    <Typography>Beers downed: {player2Stats.beersDowned}</Typography>
                                    <Typography>Rebuttals: {player2Stats.rebuttals}</Typography>
                                    <Typography>Sinks: {player2Stats.sinks}</Typography>
                                </div>
                            </Grid>
                        </Grid>
                    </div>
                </> :
                <LoadingComponent/>}
        </div>
    );
};


export default SinglesGame;
