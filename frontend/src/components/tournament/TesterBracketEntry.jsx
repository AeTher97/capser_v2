import React, {useRef, useState} from 'react';
import {Tooltip, Typography, useTheme} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import AddOutlinedIcon from '@material-ui/icons/AddOutlined';
import {findPlayerStats} from "../game/list/SoloGamesList";
import {useHasRole} from "../../utils/SecurityUtils";
import SkipNextOutlinedIcon from '@material-ui/icons/SkipNextOutlined';
import {getRequestGameTypeString} from "../../utils/Utils";
import {useHistory} from "react-router-dom";
import PlayerTooltip from "../tooltips/PlayerTooltip";
import PeopleIcon from '@material-ui/icons/People';
import TeamTooltip from "../tooltips/TeamTooltip";
import {useDragLayer, useDrop} from "react-dnd";

const type = "Player";

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


const getFlexType = (bracketEntry, teams) => {
    let player1 = teams ? bracketEntry.team1 : bracketEntry.player1;
    let player2 = teams ? bracketEntry.team2 : bracketEntry.player2;
    let flexType;
    if (!player1 && !player2) {
        flexType = 'center';
    } else if (!player2) {
        flexType = 'flex-start';
    } else if (!player1) {
        flexType = 'flex-end'
    }
    return flexType;
}

const getShowPlus = (isOwner, bracketEntry, hasRole, teams, started) => {
    if (teams) {
        return isOwner && bracketEntry.team1 && bracketEntry.team2 && !bracketEntry.game && !bracketEntry.forfeited && hasRole('ADMIN') && started;
    } else {
        return isOwner && bracketEntry.player1 && bracketEntry.player2 && !bracketEntry.game && !bracketEntry.forfeited && hasRole('ADMIN') && started;
    }
}


const TooltipContents = ({game, player, forfeitedId, teams, competitorNameOverride}) => {
    return <div style={{display: "flex", flexDirection: "row", alignItems: 'center', color: 'white'}}>
        {teams && <PeopleIcon style={{position: 'relative', left: -5}} fontSize={"small"}/>}
        <Typography noWrap style={{
            flex: 1,
            textOverflow: "ellipsis", overflow: "hidden",
            opacity: teams ? (game && player.id !== game.winnerId) || (player && forfeitedId === player.id) ? 0.5 : 1 : (game && player.id !== game.winner) || (player && forfeitedId === player.id) ? 0.5 : 1
        }}>{competitorNameOverride ? competitorNameOverride : (teams ? getTeamName(player) : getUsername(player))}</Typography>
        {player && game && <Typography noWrap
                                       style={{marginRight: 15}}>{teams ? findTeamStats(game, player.id) : findPlayerStats(game, player.id).score}</Typography>}
    </div>
}


const PlayerPart = ({
                        player,
                        game,
                        gameType,
                        forfeitedId,
                        entryId,
                        entryStyles,
                        mainStyles,
                        border,
                        teams,
                        onHighlight,
                        onHighlightEnd,
                        highlighted,
                        setPlayer,
                        bottom
                    }) => {

    const ref = useRef(null);
    const hasRole = useHasRole();

    const [competitorNameOverride, setCompetitorNameOverride] = useState(null);


    const collectedProps = useDragLayer(
        monitor => ({
            isDragging: monitor.isDragging()
        })
    )

    const [{didDrop, isOver, isDraggging}, drop] = useDrop({
        accept: type,
        hover(player, monitor) {
            if (!ref.current || !hasRole('ADMIN')) {
                return;
            }
            setCompetitorNameOverride(player.name);
        },
        drop(player) {
            if (!ref.current || !hasRole('ADMIN')) {
                return;
            }
            setPlayer(entryId, bottom, player.id, player.name);
        }, collect: (monitor) => ({
            didDrop: monitor.didDrop(),
            isOver: monitor.isOver()
        })
    });

    if (!didDrop && !isOver && collectedProps.isDragging && competitorNameOverride) {
        setCompetitorNameOverride(null);
    }

    drop(ref);

    return (
        <div
            ref={ref}
            className={[entryStyles.padding, border ? entryStyles.border : null, player && highlighted && highlighted === player.id ? mainStyles.twichHighlighted : ''].join(' ')}
            style={{margin: 0}} onMouseEnter={() => {
            if (player) {
                onHighlight(player.id);
            }
        }}
            onMouseLeave={() => {
                if (player) {
                    onHighlightEnd(player.id);
                }
            }}
        >
            {!teams && player &&
                <PlayerTooltip playerId={player.id} gameType={gameType}>
                    <TooltipContents game={game} forfeitedId={forfeitedId} teams={teams} player={player}/>
                </PlayerTooltip>}
            {teams && player &&
                <TeamTooltip teamId={player.id}>
                    <TooltipContents game={game} forfeitedId={forfeitedId} teams={teams} player={player}/>
                </TeamTooltip>}
            {competitorNameOverride && !player &&
                <TooltipContents game={game} forfeitedId={forfeitedId} teams={teams} player={player}
                                 competitorNameOverride={competitorNameOverride}/>
            }

        </div>)
}

const TesterBracketEntry = React.memo((
    {
        bracketEntry,
        gameType,
        showPath,
        isOwner,
        openAddGameDialog,
        openSkipDialog,
        pathElongation = 0,
        teams,
        onHighlight,
        onHighlightEnd,
        highlighted,
        setPlayer,
        started
    }
) => {
    const theme = useTheme();
    const borderColor = theme.palette.divider;
    const entryStyle = entryStyles(pathElongation, borderColor)();
    const classes = mainStyles();
    const hasRole = useHasRole();

    const [plusColor, setPlusColor] = useState('red');
    const [skipColor, setSkipColor] = useState(plusBaseColor);

    const history = useHistory();


    const showPlus = getShowPlus(isOwner, bracketEntry, hasRole, teams, started);


    return (
        <div style={{display: "flex", flexDirection: "row", justifyContent: "flex-start"}}>
            <div style={{
                height: 71,
                display: "flex",
                flexDirection: "column",
                justifyContent: getFlexType(bracketEntry, teams)
            }}>
                <div
                    className={[entryStyle.entry, !bracketEntry.bye ? classes.standardBorder : classes.disabledBorder].join(' ')}
                    style={{padding: 0, margin: 0}}>
                    <PlayerPart
                        onHighlight={onHighlight}
                        onHighlightEnd={onHighlightEnd}
                        cord={bracketEntry.coordinate}
                        forfeitedId={bracketEntry.forfeitedId}
                        entryId={bracketEntry.id}
                        game={bracketEntry.game}
                        player={teams ? bracketEntry.team1 : bracketEntry.player1}
                        gameType={gameType}
                        entryStyles={entryStyle}
                        mainStyles={classes}
                        teams={teams}
                        highlighted={highlighted}
                        border={true}
                        bottom={false}
                        setPlayer={setPlayer}/>
                    <PlayerPart
                        onHighlight={onHighlight}
                        onHighlightEnd={onHighlightEnd}
                        highlighted={highlighted}
                        cord={bracketEntry.coordinate}
                        forfeitedId={bracketEntry.forfeitedId}
                        entryId={bracketEntry.id}
                        game={bracketEntry.game}
                        player={teams ? bracketEntry.team2 : bracketEntry.player2}
                        gameType={gameType}
                        teams={teams}
                        entryStyles={entryStyle}
                        mainStyles={classes}
                        setPlayer={setPlayer}
                        bottom={true}
                    />
                </div>


                <div
                    className={entryStyle.additionalButtons}
                    style={{
                        top: -49, left: 137,
                    }}>
                    <Typography>{bracketEntry.coordinate} {bracketEntry.bracketEntryType}</Typography>
                </div>


                {bracketEntry.forfeited && <div className={entryStyle.additionalButtons}
                                                style={{
                                                    color: "grey",
                                                    top: -90,
                                                    left: 80,
                                                }} onMouseEnter={() => setSkipColor(plusActiveColor)}
                                                onMouseLeave={() => setSkipColor(plusBaseColor)}>
                    <Typography color={"inherit"}>Forfeited</Typography>
                </div>}


            </div>
            {showPath && <div className={entryStyle.addedPathStyles}/>}
        </div>
    );
});


const entryStyles = (pathElongation, borderColor) => makeStyles(theme => (
    {
        entry: {
            width: 150,
        },
        border: {
            padding: 5,
            borderBottom: '1px solid ' + borderColor
        },
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

export default TesterBracketEntry;
