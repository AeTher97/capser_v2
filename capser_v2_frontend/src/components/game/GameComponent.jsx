import React, {useEffect, useRef, useState} from 'react';
import PropTypes from 'prop-types';
import {Grid, Typography} from "@material-ui/core";
import {getGameTypeString, getRequestGameTypeString} from "../../utils/Utils";
import mainStyles from "../../misc/styles/MainStyles";
import {makeStyles} from "@material-ui/core/styles";
import BoldTyphography from "../misc/BoldTyphography";
import {DoublesIcon, EasyIcon, SinglesIcon, UnrankedIcon} from "../../misc/icons/CapsIcons";
import {useHistory} from "react-router-dom";

const GameComponent = ({game, vertical = true}) => {

    const [expanded, setExpanded] = useState(false);
    const columnRef = useRef();
    const additionalInfoRef = useRef();
    const containerDiv = useRef();
    const [maxHeight, setMaxHeight] = useState(0);
    const [baseHeight, setBaseHeight] = useState(0);
    const history = useHistory();


    const findPlayerStats = (game, id) => {
        return game.gamePlayerStats.find(o => o.playerId === id)
    }

    const gameStyle = gameStyles();
    const classes = mainStyles();


    const getIcon = (type) => {
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
            team1Rebuttals += findPlayerStats(game, player).rebuttals;
        })
        game.team2.playerList.forEach(player => {
            team2Rebuttals += findPlayerStats(game, player).rebuttals;
        })
        game.team1.playerList.forEach(player => {
            team1Sinks += findPlayerStats(game, player).sinks;
        })
        game.team2.playerList.forEach(player => {
            team2Sinks += findPlayerStats(game, player).sinks;
        })
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
        <div ref={columnRef}
             className={[classes.standardBorder, gameStyle.expanding, expanded ? gameStyle.elevated : gameStyle.notElevated].join(' ')}
             style={{
                 borderRadius: 7,
                 backgroundColor: '#05070a',
                 maxHeight: maxHeight,
                 zIndex: expanded ? 1000 : 0
             }}
             onMouseEnter={() => {
                 setExpanded(true)
             }} onMouseLeave={() => {
            setExpanded(false)
        }}
            >
            <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>

                <Typography variant={"h6"} style={{fontWeight: 600}} className={gameStyle.margins}
                            color={"primary"}>{team1Name} vs {team2Name}</Typography>
                <div className={[classes.centeredRowNoFlex, gameStyle.margins].join(' ')} style={{color: "white"}}>
                    {getIcon(game.gameType)}
                    <Typography style={{marginLeft: 5}}>{getGameTypeString(game.gameType)}</Typography>
                </div>
                <div style={{display: 'block'}} className={gameStyle.margins}>
                    <Typography style={{fontWeight: 600}}>{team1Score} : {team2Score}</Typography>
                </div>
                <Typography>{new Date(game.time).toDateString()}</Typography>

            </div>

            <div className={[expanded ? gameStyle.visible : gameStyle.transparent, gameStyle.overlay].join(' ')}
                 ref={additionalInfoRef}
                 style={{paddingLeft: 15}}>
                {game.gameType !== "UNRANKED" &&
                <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                    <div>
                        <BoldTyphography>Points Change</BoldTyphography>
                    </div>
                    <div className={classes.centeredRowNoFlex}>
                        <Typography className={classes.margin}>{team1Name}</Typography>
                        <BoldTyphography
                            color={team1PointsChange > 0 ? 'green' : 'red'}>{team1PointsChange}</BoldTyphography>
                        <Typography className={classes.margin}>{team2Name}</Typography>
                        <BoldTyphography
                            color={team2PointsChange > 0 ? 'green' : 'red'}>{team2PointsChange}</BoldTyphography>
                    </div>
                </div>}
                <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                    <div>
                        <BoldTyphography>Rebuttals</BoldTyphography>
                    </div>
                    <div className={classes.centeredRowNoFlex}>
                        <Typography className={classes.margin}>{team1Name}</Typography>
                        <BoldTyphography
                            color={team1Rebuttals >= team2Rebuttals ? 'green' : 'red'}>{team1Rebuttals}</BoldTyphography>
                        <Typography className={classes.margin}>{team2Name}</Typography>
                        <BoldTyphography
                            color={team2Rebuttals >= team1Rebuttals ? 'green' : 'red'}>{team2Rebuttals}</BoldTyphography>
                    </div>
                </div>
                <div className={vertical ? classes.centeredColumn : classes.centeredRowNoFlex}>
                    <div>
                        <BoldTyphography>Sinks</BoldTyphography>
                    </div>
                    <div className={classes.centeredRowNoFlex}>
                        <Typography className={classes.margin}>{team1Name}</Typography>
                        <BoldTyphography
                            color={team1Sinks >= team2Sinks ? 'green' : 'red'}>{team1Sinks}</BoldTyphography>
                        <Typography className={classes.margin}>{team2Name}</Typography>
                        <BoldTyphography
                            color={team2Sinks >= team1Sinks ? 'green' : 'red'}>{team2Sinks}</BoldTyphography>
                    </div>
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
        transition: 'all 0.2s',
        // position: "relative"
    },
    elevated: {
        boxShadow: '0px 5px 10px 5px rgba(0, 0, 0,0.8)',
    },
    notElevated: {
        boxShadow: '0 0px black'
    },
    visible: {
        transition: 'all 0.2s',
        opacity: 1
    },
    transparent: {
        transition: 'all 0.2s',
        opacity: 0
    },
    margins: {
        marginLeft: 10,
        marginRight: 10
    }
}))

GameComponent.propTypes = {};

export default GameComponent;
