import React from 'react';
import {useLocation} from "react-router-dom";
import {Typography, useTheme} from "@material-ui/core";
import QueryString from "qs";
import {usePlayerComparison} from "../../data/PlayersData";
import mainStyles from "../../misc/styles/MainStyles";
import {useXtraSmallSize} from "../../utils/SizeQuery";
import LoadingComponent from "../../utils/LoadingComponent";
import {PlayerSplash} from "../game/details/SoloGame";

const Comparison = () => {

    const classes = mainStyles();
    const theme = useTheme();
    const small = useXtraSmallSize();
    const location = useLocation();
    const player1 = QueryString.parse(location.search, {ignoreQueryPrefix: true}).player1;
    const player2 = QueryString.parse(location.search, {ignoreQueryPrefix: true}).player2;
    const gameType = QueryString.parse(location.search, {ignoreQueryPrefix: true}).gameType;

    const {loading, comparison} = usePlayerComparison(player1, player2, gameType);

    console.log(comparison)
    return (
        <div>
            {!loading && comparison ? <>
                    <div style={{borderBottom: '1px solid ' + theme.palette.divider, minHeight: 94}}/>

                    <div style={{display: 'flex', justifyContent: 'center', flexWrap: "wrap"}}>
                        <div
                            style={{position: 'relative', top: -100, maxWidth: 800, flex: 1}}>
                            <div className={classes.header}
                                 style={{justifyContent: 'center', flexDirection: small ? 'column' : 'row'}}>
                                <PlayerSplash avatarHash={comparison.player1.avatarHash}
                                              username={comparison.player1.username}
                                              gameType={gameType}
                                              playerId={player1}/>
                                <Typography variant={"h3"}>VS</Typography>
                                <PlayerSplash avatarHash={comparison.player2.avatarHash}
                                              username={comparison.player2.username}
                                              gameType={gameType}
                                              playerId={player2}/>
                            </div>
                            <div className={classes.standardBorder}>
                                <Typography variant={"h6"} color={"primary"}>Player comparison</Typography>
                                <Typography>Games played: {comparison.gamesPlayed}</Typography>
                                <Typography>Games won: {comparison.player1Stats.gamesWon}:{comparison.player2Stats.gamesWon}</Typography>
                                <Typography>Games lost: {comparison.player1Stats.gamesLost}:{comparison.player2Stats.gamesLost}</Typography>
                                <Typography>Points made: {comparison.player1Stats.pointsMade}:{comparison.player2Stats.pointsMade}</Typography>
                                <Typography>Rebuttals made: {comparison.player1Stats.rebuttalsMade}:{comparison.player2Stats.rebuttalsMade}</Typography>
                                <Typography>Sinks made: {comparison.player1Stats.sinksMade}:{comparison.player2Stats.sinksMade}</Typography>
                                <Typography>Points change: {comparison.player1Stats.pointsChange.toFixed(2)}:{comparison.player2Stats.pointsChange.toFixed(2)}</Typography>
                                <Typography>Naked laps: {comparison.player1Stats.nakedLaps}:{comparison.player2Stats.nakedLaps}</Typography>
                                <Typography>Beers downed: {comparison.player1Stats.beersDowned}:{comparison.player2Stats.beersDowned}</Typography>
                            </div>

                        </div>
                    </div>


                </> :
                <LoadingComponent/>}

        </div>
    );
};


export default Comparison;