import React from 'react';
import {useLocation, useParams} from "react-router-dom";
import mainStyles from "../../../misc/styles/MainStyles";
import {Typography, useTheme} from "@material-ui/core";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import {getGameIcon} from "../../game/GameComponent";
import {getGameModeString, getGameTypeString} from "../../../utils/Utils";
import LoadingComponent from "../../../utils/LoadingComponent";
import {useTeamGame} from "../../../data/MultipleGamesData";
import TeamPicture from "../../profile/TeamPicture";
import BoldTyphography from "../../misc/BoldTyphography";
import TeamTooltip from "../../misc/TeamTooltip";


const TeamSplash = ({teamDetails}) => {
    return (
        <div style={{margin: 40, display: 'flex', flexDirection: 'column', maxWidth: 270, alignItems: 'center'}}>
            <TeamPicture size={'large'} player1Hash={teamDetails.players[0].avatarHash}
                         player2Hash={teamDetails.players[1].avatarHash}/>
            <TeamTooltip teamId={teamDetails.teamWithStats.id}>
                <Typography noWrap
                            style={{maxWidth: '100%', marginTop: 10, overflow: 'hidden', textOverflow: 'ellipsis'}}
                            variant={"h5"}>{teamDetails.teamWithStats.name}</Typography>
            </TeamTooltip>
        </div>
    )
}

const TeamStats = ({game, team}) => {
    const classes = mainStyles();
    const theme = useTheme();

    const team1 = game.team1DatabaseId === team;
    const teamDetails = team1 ? game.team1Details : game.team2Details;
    const statsList = teamDetails.teamWithStats.playerList.map(id => game.gamePlayerStats.find(obj => obj.playerId === id));
    const nakedLap = statsList[0].nakedLap || statsList[1].nakedLap;

    return (<div
        className={classes.standardBorder}
        style={{flex: 1, minWidth: 200, borderWidth: team === game.winnerId ? 3 : 1}}>
        <Typography style={{maxWidth: '90%', textOverflow: 'ellipsis', overflow: 'hidden'}} noWrap color={"primary"}
                    variant={"h5"}>{teamDetails.teamWithStats.name}</Typography>
        {nakedLap &&
        <Typography variant={"caption"} color={"primary"}>Naked lap</Typography>}
        <Typography>Score: {team1 ? game.team1Score : game.team2Score}</Typography>
        <Typography>Points change: {statsList[0].pointsChange.toFixed(2)}</Typography>
        <Typography>Beers downed: {statsList[0].beersDowned}</Typography>
        <BoldTyphography>Points by Player</BoldTyphography>
        {statsList.map(stats => {
            const player = teamDetails.players.find(obj => obj.id === stats.playerId)
            return (<Typography key={stats.id}>{player.username}: {stats.score}</Typography>)
        })}
        <BoldTyphography>Rebuttals by Player</BoldTyphography>
        {statsList.map(stats => {
            const player = teamDetails.players.find(obj => obj.id === stats.playerId)
            return (<Typography key={stats.id}>{player.username}: {stats.rebuttals}</Typography>)
        })}
        <BoldTyphography>Sinks by Player</BoldTyphography>
        {statsList.map(stats => {
            const player = teamDetails.players.find(obj => obj.id === stats.playerId)
            return (<Typography key={stats.id}>{player.username}: {stats.sinks}</Typography>)
        })}
        <div style={{display: 'flex', justifyContent: 'center'}}>
            <div style={{position: 'relative', maxWidth: 0}}>
                {team === game.winnerId && <Typography variant={"h6"} color={"primary"} style={{
                    position: 'absolute',
                    top: nakedLap ? -380 : -357,
                    padding: 5,
                    left: -32,
                    backgroundColor: theme.palette.background.default
                }}>Winner</Typography>}
            </div>
        </div>
    </div>)
}

const DoublesGame = () => {
    const {gameId} = useParams();
    const location = useLocation();
    const classes = mainStyles();
    const theme = useTheme();
    const small = useXtraSmallSize();

    const {loading, game} = useTeamGame(gameId)


    let player1Stats;
    let player2Stats;
    if (!loading && game) {
        player1Stats = game.gamePlayerStats.find(obj => obj.playerId === game.player1)
        player2Stats = game.gamePlayerStats.find(obj => obj.playerId === game.player2)
    }

    return (
        <div>
            {!loading && game ? <>
                    }
                    <div style={{borderBottom: '1px solid ' + theme.palette.divider, minHeight: 94}}/>

                    <div style={{display: 'flex', justifyContent: 'center'}}>
                        <div
                            style={{position: 'relative', top: -100, maxWidth: 800, flex: 1}}>
                            <div className={classes.header}
                                 style={{justifyContent: 'center', flexDirection: small ? 'column' : 'row'}}>
                                <TeamSplash teamDetails={game.team1Details}/>
                                <Typography variant={"h3"}>VS</Typography>
                                <TeamSplash teamDetails={game.team2Details}/>
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
                            <div style={{display: 'flex', flexWrap: 'wrap', maxWidth: '100%'}}>
                                <TeamStats game={game} team={game.team1DatabaseId}/>
                                <TeamStats game={game} team={game.team2DatabaseId}/>
                            </div>
                        </div>
                    </div>
                </> :
                <LoadingComponent/>}
        </div>
    );
};

DoublesGame.propTypes = {};

export default DoublesGame;
