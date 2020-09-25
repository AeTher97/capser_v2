import React, {useEffect, useState} from 'react';
import {Divider, Typography} from "@material-ui/core";
import mainStyles from "../../../misc/styles/MainStyles";
import Grid from "@material-ui/core/Grid";
import {useUsernameFetch} from "../../../data/UsersFetch";

const TeamStats = ({team}) => {
    const stats = team.doublesStats;

    const [usernames, setUsernames] = useState([]);
    const fetchUsername = useUsernameFetch();

    const findUsername = (id) => {
        const obj = usernames.find(o => o.id === id);
        if (obj) {
            return obj.username;
        } else {
            return ''
        }
    }


    useEffect(() => {
            Promise.all(team.playerList.map(player => {
                return fetchUsername(player)
            })).then((value) => {
                setUsernames(value.map(user => {
                    return {id: user.data.id, username: user.data.username}
                }));
            })

    }, [team])

    const classes = mainStyles();
    return (
        <div>
            <Grid container>
                <Grid item md={6} sm={12}>
                    <Typography variant={"h5"}>{team.name} Players</Typography>
                    <Divider/>
                    <div className={classes.paddedContent} >
                        {team.playerList.map(player => {
                                return <Typography key={player} variant={"h6"}>{findUsername(player)}</Typography>
                            }
                        )}
                    </div>
                </Grid>
                <Grid item md={6} sm={12}>
                    <Typography variant={"h5"}>{team.name} Stats</Typography>
                    <Divider/>
                    <div className={classes.paddedContent} >
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Points</Typography>
                            <Typography>{stats.points.toFixed(2)}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Games Played</Typography>
                            <Typography>{stats.gamesPlayed}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Games Won</Typography>
                            <Typography>{stats.gamesWon}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Games Lost</Typography>
                            <Typography>{stats.gamesLost}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Win/Loss Ratio</Typography>
                            <Typography>{stats.winLossRatio.toFixed(2)}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Average Rebuttals</Typography>
                            <Typography>{stats.avgRebuttals.toFixed(2)}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Beers Downed</Typography>
                            <Typography>{stats.beersDowned}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Naked Laps</Typography>
                            <Typography>{stats.nakedLaps}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Points Made/Lost</Typography>
                            <Typography>{stats.pointsMadeLostRatio.toFixed(2)}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Sinks Made/Lost</Typography>
                            <Typography>{stats.sinksMadeLostRatio.toFixed(2)}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Total Points Made</Typography>
                            <Typography>{stats.totalPointsMade}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Total Points Lost</Typography>
                            <Typography>{stats.totalPointsLost}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Total Sinks Made</Typography>
                            <Typography>{stats.totalSinksMade}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Total Sinks Lost</Typography>
                            <Typography>{stats.totalSinksLost}</Typography>
                        </div>
                        <div className={classes.header}>
                            <Typography style={{flex: 1}}>Total Rebuttals</Typography>
                            <Typography>{stats.totalRebuttals}</Typography>
                        </div>

                    </div>
                </Grid>


            </Grid>

        </div>
    );
};

TeamStats.propTypes = {};

export default TeamStats;
