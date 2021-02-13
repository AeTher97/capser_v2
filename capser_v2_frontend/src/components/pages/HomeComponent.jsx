import React from 'react';
import {Divider, Grid, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import PageHeader from "../misc/PageHeader";
import {useDashboard} from "../../data/DashboardData";
import LoadingComponent from "../../utils/LoadingComponent";
import {getGameTypeString} from "../../utils/Utils";
import GameComponent from "../game/GameComponent";

const HomeComponent = () => {

    const classes = mainStyles();
    const theme = useTheme();

    const {games, posts, gamesLoading, postsLoading} = useDashboard();


    return (
        <>
            <PageHeader title={"Global Caps League"} showLogo/>
            <Grid className={classes.root} container>
                {!gamesLoading && !postsLoading ? <Grid container>
                    <Grid item sm={8} xs={12}>
                        <div className={classes.leftOrientedWrapperNoPadding}>
                            <Typography variant={"h5"}>News Feed</Typography>
                            <div style={{padding: 10}}>
                                <Divider/>
                                {posts.map(post => {
                                    return (
                                        <div key={post.title} style={{textAlign: "left"}}>

                                            <Typography align={"left"} variant={"caption"}
                                                        className={classes.textSubheading}>{new Date(post.date).toDateString()}</Typography>
                                            <div className={classes.standardBorder} style={{padding: 10}} >
                                                <Typography variant={"h5"}
                                                            style={{color: theme.palette.primary.main}}
                                                            align={"left"}
                                                            className={classes.textHeading}>{post.title}</Typography>
                                            <Typography align={"left"}
                                            >{post.description}</Typography>
                                            <Typography align={"left"} variant={"caption"}
                                                        className={classes.text}>{post.signature}</Typography>
                                            </div>
                                        </div>)
                                })}
                            </div>
                        </div>
                    </Grid>
                    <Grid item sm={4} xs={12} style={{textAlign: "left"}}>
                        <Typography variant={"h5"}>Latest Games</Typography>
                        <div style={{padding: 10}}>
                            <Divider style={{marginBottom: 10}}/>
                            <Grid container spacing={2}>
                                {games.map(game => {
                                    return (
                                        <Grid key={game.player1 + game.player2 + game.time} item xs={12}>
                                            <GameComponent game={game}/>
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


export default HomeComponent;
