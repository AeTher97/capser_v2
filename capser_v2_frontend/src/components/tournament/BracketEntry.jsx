import React, {useState} from 'react';
import {Tooltip, Typography, useTheme} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import AddOutlinedIcon from '@material-ui/icons/AddOutlined';
import {findPlayerStats} from "../pages/singles/SinglesGamesList";
import {useHasRole} from "../../utils/SecurityUtils";
import SkipNextOutlinedIcon from '@material-ui/icons/SkipNextOutlined';
import {getRequestGameTypeString} from "../../utils/Utils";
import {useHistory} from "react-router-dom";
import PlayerTooltip from "../misc/PlayerTooltip";
import PeopleIcon from '@material-ui/icons/People';

export const findTeamStats = (game, id) => {
    if (game.team1DatabaseId === id) {
        return game.team1Score;
    } else {
        return game.team2Score;
    }
}

const plusBaseColor = "#404040"
const plusActiveColor = "#6b6b6b"

const getUsername = (player) => {
    if (!player) {
        return ""
    } else {
        return player.username;
    }
}

const getTeamName = (team) => {
    if (!team) {
        return ""
    } else {
        return team.name;
    }
}


const getFlexType = (bracketEntry) => {
    let flexType;
    if (!bracketEntry.player1 && !bracketEntry.player2) {
        flexType = 'center';
    } else if (!bracketEntry.player2) {
        flexType = 'flex-start';
    } else if (!bracketEntry.player1) {
        flexType = 'flex-end'
    }
    return flexType;
}

const getShowPlus = (isOwner, bracketEntry, hasRole, teams) => {
    if (teams) {
        return isOwner && bracketEntry.team1 && bracketEntry.team2 && !bracketEntry.game && !bracketEntry.forfeited && hasRole('ADMIN');

    } else {
        return isOwner && bracketEntry.player1 && bracketEntry.player2 && !bracketEntry.game && !bracketEntry.forfeited && hasRole('ADMIN');
    }
}


const PlayerPart = ({player, game, gameType, forfeitedId, entryStyles, border, teams}) => {

    return (
        <div className={[entryStyles.padding, border ? entryStyles.border : null].join(' ')}>
            {!teams && player &&
            <PlayerTooltip playerId={player.id} gameType={gameType}>
                <div style={{display: "flex", flexDirection: "row"}}>
                    <Typography style={{
                        flex: 1,
                        opacity: (game && player.id !== game.winner) || (player && forfeitedId === player.id) ? 0.5 : 1
                    }}>{teams ? getTeamName(player) : getUsername(player)}</Typography>
                    {player && game && <Typography
                        style={{marginRight: 15}}>{findPlayerStats(game, player.id).score}</Typography>}
                </div>
            </PlayerTooltip>}
            {teams && player &&
            <div style={{display: "flex", flexDirection: "row", alignItems: 'center', color: 'white'}}>
                <PeopleIcon fontSize={"small"}/>
                <Typography style={{
                    flex: 1,
                    opacity: (game && player.id !== game.winner) || (player && forfeitedId === player.id) ? 0.5 : 1
                }}>{teams ? getTeamName(player) : getUsername(player)}</Typography>
                {player && game && <Typography
                    style={{marginRight: 15}}>{findTeamStats(game, player.id)}</Typography>}
            </div>}
        </div>)
}

const BracketEntry = ({
                          bracketEntry,
                          gameType,
                          showPath,
                          isOwner,
                          openAddGameDialog,
                          openSkipDialog,
                          pathElongation = 0,
                          teams
                      }) => {
        const theme = useTheme();
        const borderColor = theme.palette.divider;
        const entryStyle = entryStyles(pathElongation, borderColor)();
        const classes = mainStyles();
        const hasRole = useHasRole();

        const [plusColor, setPlusColor] = useState('red');
        const [skipColor, setSkipColor] = useState(plusBaseColor);

        const history = useHistory();


        const showPlus = getShowPlus(isOwner, bracketEntry, hasRole, teams);


        return (
            <div style={{display: "flex", flexDirection: "row", justifyContent: "flex-start"}}>
                <div style={{
                    height: 71,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: getFlexType(bracketEntry)
                }}>
                    <div
                        className={[entryStyle.entry, !bracketEntry.bye ? classes.standardBorder : classes.disabledBorder].join(' ')}
                        style={{padding: 0, margin: 0}}>
                        <PlayerPart
                            forfeitedId={bracketEntry.forfeitedId}
                            game={bracketEntry.game}
                            player={teams ? bracketEntry.team1 : bracketEntry.player1}
                            gameType={gameType}
                            entryStyles={entryStyle}
                            teams={teams}
                            border={true}/>
                        <PlayerPart
                            forfeitedId={bracketEntry.forfeitedId}
                            game={bracketEntry.game}
                            player={teams ? bracketEntry.team2 : bracketEntry.player2}
                            gameType={gameType}
                            teams={teams}
                            entryStyles={entryStyle}/>
                    </div>


                    {showPlus && <>
                        <div
                            className={entryStyle.additionalButtons}
                            style={{
                                backgroundColor: plusColor,
                                top: -49, left: 137,
                                height: plusColor === "red" ? 24 : 22,
                                width: plusColor === "red" ? 24 : 22,
                                border: plusColor === "red" ? "none" : "1px solid white",
                                padding: plusColor === "red" ? 2 : 1.25,
                            }} onMouseEnter={() => setPlusColor("#c70000")} onMouseLeave={() => setPlusColor("red")}>
                            <AddOutlinedIcon
                                onClick={() => {
                                    if (teams) {
                                        openAddGameDialog(bracketEntry.id, bracketEntry.team1, bracketEntry.team2, null, null)
                                    } else {
                                        openAddGameDialog(bracketEntry.id, bracketEntry.player1, bracketEntry.player2, null, null)
                                    }
                                }}/>
                        </div>

                        <div
                            className={entryStyle.additionalButtons}
                            style={{
                                backgroundColor: skipColor,
                                top: plusColor === "red" ? -77 : -76,
                                left: 170,
                                height: skipColor === plusBaseColor ? 24 : 22,
                                width: skipColor === plusBaseColor ? 24 : 22,
                                border: skipColor === plusBaseColor ? "none" : "1px solid white",
                                padding: skipColor === plusBaseColor ? 2 : 1.25
                            }} onMouseEnter={() => setSkipColor(plusActiveColor)}
                            onMouseLeave={() => setSkipColor(plusBaseColor)}>
                            <SkipNextOutlinedIcon
                                onClick={() => {
                                    if (teams) {
                                        openSkipDialog(bracketEntry.id, bracketEntry.team1, bracketEntry.team2)

                                    } else {
                                        openSkipDialog(bracketEntry.id, bracketEntry.player1, bracketEntry.player2)
                                    }
                                }}/>
                        </div>
                    </>}

                    {bracketEntry.forfeited && <div className={entryStyle.additionalButtons}
                                                    style={{
                                                        color: "grey",
                                                        top: -90,
                                                        left: 80,
                                                    }} onMouseEnter={() => setSkipColor(plusActiveColor)}
                                                    onMouseLeave={() => setSkipColor(plusBaseColor)}>
                        <Typography color={"inherit"}>Forfeited</Typography>
                    </div>}

                    {!showPlus && bracketEntry.game && <Tooltip
                        title={<div style={{padding: 5, backgroundColor: theme.palette.divider}}>Detailed game info</div>}
                        onClick={() => history.push(`/${getRequestGameTypeString(bracketEntry.game.gameType)}/${bracketEntry.game.id}`)}>
                        <div
                            className={entryStyle.additionalButtons}
                            style={{
                                backgroundColor: plusBaseColor,
                                top: -47,
                                left: 140,
                                width: 20,
                                minHeight: 20,
                                display: 'flex',
                                fontSize: 12,
                                justifyContent: 'center',
                                alignItems: 'center',
                                border: '1px solid grey',
                            }} onMouseEnter={() => setSkipColor(plusActiveColor)}
                            onMouseLeave={() => setSkipColor(plusBaseColor)}>
                            i
                        </div>
                    </Tooltip>}

                </div>
                {showPath && <div className={entryStyle.addedPathStyles}/>}
            </div>
        );
    }
;

BracketEntry.propTypes =
    {}
;

const entryStyles = (pathElongation, borderColor) => makeStyles(theme => (
    {
        entry: {
            width: 150,
        }
        ,
        border: {
            padding: 5,
            borderBottom: '1px solid ' + borderColor
        }
        ,
        padding: {
            padding: 5,
            paddingLeft: 10
        },
        addedPathStyles: {
            width: 49 + pathElongation,
            height: 35,
            display: "inline-block",
            borderBottom: `1px solid ${borderColor}`
        },
        additionalButtons: {
            position: "relative",
            borderRadius: '50%',
            zIndex: 10,
            cursor: "pointer",
            color: 'white',

        }
    }
))

export default BracketEntry;
