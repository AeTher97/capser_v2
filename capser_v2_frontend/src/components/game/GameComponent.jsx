import React, {useEffect, useRef, useState} from 'react';
import {Link, Typography, useTheme} from "@material-ui/core";
import {getGameTypeString, getRequestGameTypeString} from "../../utils/Utils";
import mainStyles from "../../misc/styles/MainStyles";
import {makeStyles} from "@material-ui/core/styles";
import BoldTyphography from "../misc/BoldTyphography";
import {DoublesIcon, EasyIcon, SinglesIcon, UnrankedIcon} from "../../misc/icons/CapsIcons";
import {useHistory} from "react-router-dom";
import {useXtraSmallSize} from "../../utils/SizeQuery";

export const getGameIcon = (type) => {
    switch (type) {
        case "EASY_CAPS":
            return <EasyIcon fontSize={"small"}/>
        case "SINGLES":
            return <SinglesIcon fontSize={"small"}/>
        case "DOUBLES":
            return <DoublesIcon fontSize={"small"}/>
        case "UNRANKED":
            return <UnrankedIcon fontSize={"small"}/>
    }
}

const GameComponent = ({game, vertical = true}) => {

    const [expanded, setExpanded] = useState(false);
    const [maxHeight, setMaxHeight] = useState(0);
    const [delay, setDelay] = useState(0);
    const columnRef = useRef();
    const additionalInfoRef = useRef();
    const containerDiv = useRef();
    const [baseHeight, setBaseHeight] = useState(0);
    const history = useHistory();
    const theme = useTheme();
    const small = useXtraSmallSize();


    const findPlayerStats = (game, id) => {
        return game.gamePlayerStats.find(o => o.playerId === id)
    }

    const gameStyle = gameStyles();
    const classes = mainStyles();


    useEffect(() => {
        if (columnRef.current && additionalInfoRef.current) {
            if (expanded) {
                setMaxHeight(columnRef.current.scrollHeight)
            } else {
                setMaxHeight(columnRef.current.scrollHeight - additionalInfoRef.current.scrollHeight - 30)
            }
        }
        if (columnRef.current) {
            setBaseHeight(columnRef.current.scrollHeight - additionalInfoRef.current.scrollHeight)
        } else {
            setBaseHeight(0);
        }
    }, [expanded, columnRef])

    let team1Name;
    let team2Name;
    let team1Score;
    let team2Score;
    let team1PointsChange;
    let team2PointsChange;
    let team1Rebuttals = 0;
    let team2Rebuttals = 0;
    let team1Sinks = 0;
    let team2Sinks = 0;

    if (game.gameType === 'DOUBLES') {
        team1Name = game.team1Name.name;
        team2Name = game.team2Name.name;
        team1Score = game.team1Score;
        team2Score = game.team2Score;
        const player1Stats = findPlayerStats(game, game.team1.playerList[0]);
        const player2Stats = findPlayerStats(game, game.team2.playerList[0]);
        game.team1.playerList.forEach(player => {
            team1Sinks += findPlayerStats(game, player).sinks;
        })
        game.team2.playerList.forEach(player => {
            team2Sinks += findPlayerStats(game, player).sinks;
        })
        if (team1Sinks !== 0 && team2Sinks !== 0) {
            team1Rebuttals = team2Sinks - team2Score;
            team2Rebuttals = team1Sinks - team1Score;
        }
        team1PointsChange = player1Stats.pointsChange;
        team2PointsChange = player2Stats.pointsChange;
    } else {
        const player1Stats = findPlayerStats(game, game.player1)
        const player2Stats = findPlayerStats(game, game.player2)
        team1Name = game.player1Name.username || game.player1Name;
        team2Name = game.player2Name.username || game.player2Name;
        team1Score = player1Stats.score;
        team2Score = player2Stats.score;
        team1Rebuttals = player1Stats.rebuttals;
        team2Rebuttals = player2Stats.rebuttals;
        team1PointsChange = player1Stats.pointsChange;
        team2PointsChange = player2Stats.pointsChange;
        team1Sinks = player1Stats.sinks;
        team2Sinks = player2Stats.sinks;
    }

    return (
        <div
            style={{height: baseHeight}}
            ref={containerDiv}
            onMouseUp={() => {
                if (!small) {
                    if (game.gameType !== 'DOUBLES') {
                        history.push(`/${getRequestGameTypeString(game.gameType)}/${game.id}`)
                    }
                } else {
                    setExpanded(!expanded);

                }
            }}
            onTouchEnd={(e) => {
            }}>
            <div ref={columnRef}
                 className={[classes.standardBorder, gameStyle.expanding, expanded ? gameStyle.elevated : gameStyle.notElevated].join(' ')}
                 style={{
                     borderRadius: 7,
                     margin: 0,
                     backgroundColor: theme.palette.background.default,
                     maxHeight: maxHeight,
                     zIndex: expanded ? 1000 : 0
                 }}
                 onMouseEnter={(e) => {
                     if (!small) {
                         setDelay(setTimeout(() => {
                             setExpanded(true)
                         }, 500))
                     }
                 }} onMouseLeave={() => {
                clearTimeout(delay);
                setExpanded(false)
            }}>
                <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>

                    <Typography variant={"h6"} style={{fontWeight: 600}} className={gameStyle.margins}
                                color={"primary"}>{team1Name} vs {team2Name}</Typography>
                    <div className={[classes.centeredRowNoFlex, gameStyle.margins].join(' ')} style={{color: "white"}}>
                        {getGameIcon(game.gameType)}
                        <Typography style={{marginLeft: 5}}>{getGameTypeString(game.gameType)}</Typography>
                    </div>
                    <div style={{display: 'block'}} className={gameStyle.margins}>
                        <Typography style={{fontWeight: 600}}>{team1Score} : {team2Score}</Typography>
                    </div>
                    <Typography>{new Date(game.time).toDateString()}</Typography>

                </div>

                <div className={[expanded ? gameStyle.visible : gameStyle.transparent, gameStyle.overlay].join(' ')}
                     ref={additionalInfoRef}
                     style={{paddingLeft: vertical ? 0 : 15, paddingTop: 10}}>
                    {game.gameType !== "UNRANKED" &&
                    <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                        <div>
                            <BoldTyphography>Points Change</BoldTyphography>
                        </div>
                        <div className={classes.centeredRowNoFlex}>
                            <Typography className={classes.margin}>{team1Name}</Typography>
                            <div style={{color: team1PointsChange > 0 ? 'green' : 'red'}}>
                                <BoldTyphography color={"inherit"}
                                >{team1PointsChange}</BoldTyphography>
                            </div>
                            <Typography className={classes.margin}>{team2Name}</Typography>
                            <div style={{color: team2PointsChange > 0 ? 'green' : 'red'}}>
                                <BoldTyphography
                                    color={"inherit"}>{team2PointsChange}</BoldTyphography>
                            </div>

                        </div>
                    </div>}
                    <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                        <div>
                            <BoldTyphography>Rebuttals</BoldTyphography>
                        </div>
                        <div className={classes.centeredRowNoFlex}>
                            <Typography className={classes.margin}>{team1Name}</Typography>
                            <div style={{color: team1Rebuttals >= team2Rebuttals ? 'green' : 'red'}}>
                                <BoldTyphography color={"inherit"}>{team1Rebuttals}</BoldTyphography>
                            </div>
                            <Typography className={classes.margin}>{team2Name}</Typography>
                            <div style={{color: team2Rebuttals >= team1Rebuttals ? 'green' : 'red'}}>
                                <BoldTyphography
                                    color={"inherit"}>{team2Rebuttals}</BoldTyphography>
                            </div>
                        </div>
                    </div>
                    <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                        <div>
                            <BoldTyphography>Sinks</BoldTyphography>
                        </div>
                        <div className={classes.centeredRowNoFlex}>
                            <Typography className={classes.margin}>{team1Name}</Typography>
                            <div style={{color: team1Sinks >= team2Sinks ? 'green' : 'red'}}>
                                <BoldTyphography color={"inherit"}>{team1Sinks}</BoldTyphography>
                            </div>
                            <Typography className={classes.margin}>{team2Name}</Typography>
                            <div style={{color: team2Sinks >= team1Sinks ? 'green' : 'red'}}>
                                <BoldTyphography
                                    color={"inherit"}>{team2Sinks}</BoldTyphography>
                            </div>
                        </div>
                    </div>
                    {small && <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                        <Link onClick={() => {
                            if (game.gameType !== 'DOUBLES') {
                                history.push(`/${getRequestGameTypeString(game.gameType)}/${game.id}`)
                            }
                        }}>Details</Link>
                    </div>}
                </div>
            </div>
        </div>
    );
};

const gameStyles = makeStyles(theme => ({
    hidden: {
        display: 'none'
    },
    expanding: {
        cursor: "pointer",
        overflow: "hidden",
        transition: 'all 0.15s',
        position: "relative"
    },
    elevated: {
        boxShadow: '0px 5px 10px 5px rgba(0, 0, 0,0.8)',
    },
    notElevated: {
        boxShadow: '0 0px black'
    },
    visible: {
        transition: 'all 0.15s',
        opacity: 1
    },
    transparent: {
        transition: 'all 0.15s',
        opacity: 0
    },
    margins: {
        marginLeft: 10,
        marginRight: 10
    }
}))

GameComponent.propTypes = {};

export default GameComponent;
