import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import {getStatsString} from "../../utils/Utils";
import {Typography, useTheme} from "@material-ui/core";
import ProfilePicture from "../profile/ProfilePicture";
import TeamPicture from "../profile/TeamPicture";

const TeamCard = ({team}) => {
    const classes = mainStyles();

    const stats = team.teamWithStats.doublesStats;
    const theme = useTheme();

    return (
        <div className={classes.standardBorder}
             style={{margin: 0, backgroundColor: theme.palette.background.paper, boxShadow: '0px 0px 10px 5px black'}}>
            <div style={{display: 'flex', justifyContent: 'center'}}>
                <TeamPicture size={'medium'} player1Hash={team.players[0].avatarHash} player2Hash={team.players[1].avatarHash}/>
            </div>
            <div className={classes.centeredRow} style={{maxWidth: 200}}>
                <Typography noWrap style={{overflow: 'hidden', textOverflow: 'ellipsis'}} color={"primary"} variant={"h6"}>{team.teamWithStats.name}</Typography>
            </div>

            <div className={classes.header} style={{minWidth: 190}}>
                <Typography variant={"body2"} style={{flex: 1}}>Points</Typography>
                <Typography variant={"body2"}>{stats.points.toFixed(2)}</Typography>
            </div>
            <div className={classes.header}>
                <Typography variant={"body2"} style={{flex: 1}}>Average Rebuttals</Typography>
                <Typography variant={"body2"}>{stats.avgRebuttals.toFixed(2)}</Typography>
            </div>
            <div className={classes.header}>
                <Typography variant={"body2"} style={{flex: 1}}>Games Played</Typography>
                <Typography variant={"body2"}>{stats.gamesPlayed}</Typography>
            </div>
            <div className={classes.header}>
                <Typography variant={"body2"} style={{flex: 1}}>Games Won</Typography>
                <Typography variant={"body2"}>{stats.gamesWon}</Typography>
            </div>
            <div className={classes.header}>
                <Typography variant={"body2"} style={{flex: 1}}>Games Lost</Typography>
                <Typography variant={"body2"}>{stats.gamesLost}</Typography>
            </div>
            <div className={classes.header}>
                <Typography variant={"body2"} style={{flex: 1}}>Win/Loss Ratio</Typography>
                <Typography variant={"body2"}>{stats.winLossRatio.toFixed(2)}</Typography>
            </div>

            <div className={classes.header}>
                <Typography variant={"body2"} style={{flex: 1}}>Naked Laps</Typography>
                <Typography variant={"body2"}>{stats.nakedLaps}</Typography>
            </div>
        </div>)
};

export default TeamCard;