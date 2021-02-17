import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import {TableBody, TableRow, Typography, useTheme} from "@material-ui/core";
import LogoComponent from "./LogoComponent";
import TableCell from "@material-ui/core/TableCell";
import Table from "@material-ui/core/Table";
import {getStatsString} from "../../utils/Utils";

const PlayerCard = (props) => {

    const {player, type} = props;
    const classes = mainStyles();

    const stats = player[getStatsString(type)];
    const theme = useTheme();

    return (
        <div className={classes.standardBorder} style={{margin:0, backgroundColor: theme.palette.background.paper}}>
            <LogoComponent/>
            <div className={classes.centeredRow}>
                <Typography color={"primary"} variant={"h6"}>{player.username}</Typography>
            </div>

            <div className={classes.header} style={{minWidth: 175}}>
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
        </div>
    );
};

PlayerCard.propTypes = {};

export default PlayerCard;
