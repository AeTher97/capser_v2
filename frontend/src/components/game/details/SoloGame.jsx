import React from 'react';
import {useSoloGame} from "../../../data/SoloGamesData";
import {useLocation, useParams} from 'react-router-dom';
import LoadingComponent from "../../../utils/LoadingComponent";
import {Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../../misc/styles/MainStyles";
import {getGameModeString, getGameTypeString} from "../../../utils/Utils";
import ProfilePicture from "../../profile/ProfilePicture";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import {getGameIcon} from "./GameComponent";
import PlayerTooltip from "../../tooltips/PlayerTooltip";
import Timeline from "./Timeline";


const PlayerSplash = ({avatarHash, username, gameType, playerId}) => {
    return (
        <div style={{margin: 40, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
            <ProfilePicture size={'large'} avatarHash={avatarHash}/>
            <PlayerTooltip gameType={gameType} playerId={playerId}>
                <Typography style={{marginTop: 10}} variant={"h5"}>{username}</Typography>
            </PlayerTooltip>
        </div>
    )
}

const PlayerStats = ({playerStats, winner, name}) => {
    const classes = mainStyles();
    const theme = useTheme();

    return (<div
        className={classes.standardBorder}
        style={{flex: 1, minWidth: 200, borderWidth: winner ? 3 : 1}}>
        <Typography color={"primary"} variant={"h5"}>{name}</Typography>
        {playerStats.nakedLap &&
            <Typography variant={"caption"} color={"primary"}>Naked lap</Typography>}
        <Typography>Score: {playerStats.score}</Typography>
        <Typography>Points change: {playerStats.pointsChange.toFixed(2)}</Typography>
        <Typography>Beers downed: {playerStats.beersDowned}</Typography>
        <Typography>Rebuttals: {playerStats.rebuttals}</Typography>
        <Typography>Sinks: {playerStats.sinks}</Typography>
        <div style={{display: 'flex', justifyContent: 'center'}}>
            <div style={{position: 'relative', maxWidth: 0}}>
                {winner && <Typography variant={"h6"} color={"primary"} style={{
                    position: 'absolute',
                    top: -190,
                    padding: 5,
                    left: -32,
                    backgroundColor: theme.palette.background.default
                }}>Winner</Typography>}
            </div>
        </div>
    </div>)
}

const SoloGame = () => {

    const {gameId} = useParams();
    const location = useLocation();
    const classes = mainStyles();
    const theme = useTheme();
    const small = useXtraSmallSize();

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
                    <div style={{borderBottom: '1px solid ' + theme.palette.divider, minHeight: 94}}/>

                    <div style={{display: 'flex', justifyContent: 'center', flexWrap: "wrap"}}>
                        <div
                            style={{position: 'relative', top: -100, maxWidth: 800, flex: 1}}>
                            <div className={classes.header}
                                 style={{justifyContent: 'center', flexDirection: small ? 'column' : 'row'}}>
                                <PlayerSplash avatarHash={game.player1Data.avatarHash} username={game.player1Name}
                                              winner={game.winner === game.player1} playerId={game.player1}
                                              gameType={game.gameType}/>
                                <Typography variant={"h3"}>VS</Typography>
                                <PlayerSplash avatarHash={game.player2Data.avatarHash} username={game.player2Name}
                                              winner={game.winner === game.player2} playerId={game.player2}
                                              gameType={game.gameType}/>
                                />
                            </div>
                            <div className={classes.standardBorder}>
                                <Typography variant={"h6"} color={"primary"}>Game</Typography>
                                <Typography style={{
                                    alignItems: 'center',
                                    display: 'flex'
                                }}>{getGameIcon(game.gameType)} {getGameTypeString(game.gameType)}</Typography>
                                <Typography>{new Date(game.time).toUTCString()}</Typography>
                                <Typography>{getGameModeString(game.gameMode)}</Typography>
                            </div>
                            <div style={{display: 'flex', flexWrap: 'wrap'}}>
                                <PlayerStats name={game.player1Name} game={game} playerStats={player1Stats}
                                             winner={game.winner === game.player1}/>
                                <PlayerStats name={game.player2Name} game={game} playerStats={player2Stats}
                                             winner={game.winner === game.player2}/>
                            </div>

                        </div>
                        {game.gameEventList && game.gameEventList.length > 0 && small &&
                            <div style={{display: "flex", position: "relative", top: -90}}>
                                <Timeline timeline={game.gameEventList} leftPlayer={game.player1}
                                          leftPlayerName={game.player1Name}
                                          rightPlayerName={game.player2Name}
                                /></div>}

                    </div>
                    {game.gameEventList && game.gameEventList.length > 0 && !small &&
                        <div style={{display: "flex", position: "relative", top: -90}}>
                            <Timeline timeline={game.gameEventList} leftPlayer={game.player1}
                                      leftPlayerName={game.player1Name}
                                      rightPlayerName={game.player2Name}
                            /></div>}

                </> :
                <LoadingComponent/>}

        </div>
    );
};


export default SoloGame;
