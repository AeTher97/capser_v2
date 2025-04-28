import React from 'react';
import {Divider, Grid, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import PageHeader from "../../components/misc/PageHeader";
import {useDashboard} from "../../data/DashboardData";
import LoadingComponent from "../../utils/LoadingComponent";
import GameComponent from "../../components/game/details/GameComponent";
import {useWindowSize} from "../../utils/UseSize";
import {useXtraSmallSize} from "../../utils/SizeQuery";


const BlogPosts = ({classes, small, posts, theme}) => {
    return (
        <Grid item sm={4} xs={12}>
            <div className={classes.leftOrientedWrapperNoPadding} style={{padding: small ? 10 : 15}}>
                <Typography variant={"h5"}>News Feed</Typography>
                <div style={{padding: small ? 0 : 10}}>
                    <Divider/>
                    {posts.map(post => {
                        return (
                            <div key={post.title} style={{textAlign: "left"}}>
                                <div style={{padding: 10, margin: 0}}>
                                    <div className={{
                                        display: "flex",
                                        flexDirection: "row",
                                        flex: 1,
                                        gap: 5,
                                        alignItems: "center",
                                    }}>
                                        <Typography variant={"h5"}
                                                    style={{color: theme.palette.primary.main, flex: 1}}
                                                    align={"left"}
                                                    className={classes.textHeading}>{post.title}</Typography>
                                        <Typography align={"left"} variant={"caption"}
                                                    className={classes.textSubheading}>{new Date(post.date).toDateString()}</Typography>
                                    </div>
                                    <Typography align={"left"}
                                    >{post.description}</Typography>
                                    <Typography align={"left"} variant={"caption"}
                                                className={classes.text}>{post.signature}</Typography>
                                </div>
                                <Divider style={{marginTop: 10}}/>
                            </div>)
                    })}
                </div>
            </div>
        </Grid>
    );
};


const LatestGames = ({games, small, size}) => {
    return (
        <Grid item sm={8} xs={12} style={{
            minHeight: size.height - 120,
            textAlign: "left",
            padding: small ? 10 : 15
        }}>
            <div>
                <Typography variant={"h5"}>Latest Games</Typography>
            </div>
            <div style={{padding: small ? 0 : 10}}>
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
    );
};


const HomeScreen = () => {

    const classes = mainStyles();
    const theme = useTheme();
    const size = useWindowSize();
    const small = useXtraSmallSize();

    const {games, posts, gamesLoading, postsLoading} = useDashboard();


    return (
        <div style={{height: '100%', overflow: 'scroll'}}>
            {!small && <PageHeader title={"Global Caps League"} font={"BankGothic"} fontWeight={1000}/>}
            <div style={{display: 'flex', justifyContent: 'center'}}>
                <Grid className={classes.root} container style={{padding: 0, maxWidth: 1250}}>
                    {!gamesLoading && !postsLoading ? <Grid container>
                        {!small ? <><LatestGames games={games} small={small} size={size}/>
                                <BlogPosts classes={classes} small={small} posts={posts} theme={theme}/></>
                            : <><BlogPosts classes={classes} small={small} posts={posts} theme={theme}/>
                                <LatestGames games={games} small={small} size={size}/></>}

                    </Grid> : <LoadingComponent showText fullHeight/>}
                </Grid>
            </div>

        </div>);
}


export default HomeScreen;
