import React from 'react';
import {Divider, Grid, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import PageHeader from "../misc/PageHeader";
import {useDashboard} from "../../data/DashboardData";
import LoadingComponent from "../../utils/LoadingComponent";
import {getGameTypeString} from "../../utils/Utils";

const HomeComponent = () => {

        const classes = mainStyles();
        const theme = useTheme();

        const {games, posts, gamesLoading, postsLoading} = useDashboard();

        const findPlayerStats = (game, id) => {
            return game.gamePlayerStats.find(o => o.playerId === id)
        }


        return (
            <>
                <PageHeader title={"Global Caps League"} showLogo/>
                <Grid className={classes.root}>
                    {!gamesLoading && !postsLoading ? <Grid container>
                        <Grid item sm={8} className={classes.squareShine}>
                            <div className={classes.leftOrientedWrapperNoPadding}>
                                <Typography variant={"h5"}>News Feed</Typography>
                                <div style={{padding: 10}}>
                                    <Divider/>
                                    {posts.map(post => {
                                        return (
                                            <div key={post.title} style={{textAlign: "left"}}>
                                                <Typography variant={"h5"}
                                                            style={{color: theme.palette.primary.main}}
                                                            align={"left"}
                                                            className={classes.textHeading}>{post.title}</Typography>
                                                <Typography align={"left"} variant={"caption"}
                                                            className={classes.textSubheading}>{new Date(post.date).toDateString()}</Typography>
                                                <Typography align={"left"}
                                                            className={classes.text}>{post.description}</Typography>
                                                <Typography align={"left"} variant={"caption"}
                                                            className={classes.text}>{post.signature}</Typography>
                                                <Divider style={{marginTop: 10}}/>
                                            </div>)
                                    })}
                                </div>
                            </div>
                        </Grid>
                        <Grid item sm={4} style={{textAlign: "left"}} className={classes.squareShine}>
                            <Typography variant={"h5"}>Latest Games</Typography>
                            <div style={{padding: 10}}>
                                <Divider style={{marginBottom: 10}}/>
                                <Grid container spacing={2}>
                                    {games.map(game => {
                                        if (game.gameType === 'DOUBLES') {
                                            return (
                                                <Grid key={game.player1 + game.player2 + game.time} item xs={12}>
                                                    <div className={classes.neon} style={{padding: 10}}>
                                                        <Typography
                                                            color={"primary"}>{getGameTypeString(game.gameType)}</Typography>
                                                        <div className={classes.header}>
                                                            <Typography>{game.team1Score} : {game.team2Score} {game.team1Name.name} vs {game.team2Name.name}</Typography>
                                                        </div>
                                                        <Typography>{new Date(game.time).toDateString()}</Typography>
                                                    </div>
                                                </Grid>
                                            )
                                        }
                                        const player1Stats = findPlayerStats(game, game.player1)
                                        const player2Stats = findPlayerStats(game, game.player2)
                                        return (
                                            <Grid key={game.player1 + game.player2 + game.time} item xs={12}>
                                                <div className={classes.neon} style={{padding: 10}}>
                                                    <Typography
                                                        color={"primary"}>{getGameTypeString(game.gameType)}</Typography>
                                                    <div className={classes.header}>
                                                        <Typography>{player1Stats.score} : {player2Stats.score} {game.player1Name.username} vs {game.player2Name.username}</Typography>
                                                    </div>
                                                    <Typography>{new Date(game.time).toDateString()}</Typography>
                                                </div>
                                            </Grid>
                                        )
                                    })}
                                </Grid>
                            </div>

                        </Grid>
                    </Grid> : <LoadingComponent/>}
                </Grid>

            </>);
    }
;

export default HomeComponent;
