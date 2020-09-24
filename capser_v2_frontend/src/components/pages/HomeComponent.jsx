import React, {useEffect, useState} from 'react';
import {Divider, Grid, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import Card from "@material-ui/core/Card";
import PageHeader from "../misc/PageHeader";
import {useBlogPostsFetch, useDashboardGamesFetch} from "../../data/Dashboard";
import {useUsernameFetch} from "../../data/UsersFetch";
import LoadingComponent from "../../utils/LoadingComponent";
import {getGameTypeString} from "../../utils/Utils";

const HomeComponent = () => {

        const [usernames, setUsernames] = useState([])
        const [posts, setPosts] = useState([]);
        const [games, setGames] = useState([])
        const [loadingPosts, setLoadingPosts] = useState(true);
        const [loadingGames, setLoadingGames] = useState(true);
        const classes = mainStyles();
        const theme = useTheme();

        const gamesFetch = useDashboardGamesFetch();
        const fetchUsername = useUsernameFetch();
        const fetchBlog = useBlogPostsFetch();

        const findPlayerStats = (game, id) => {
            return game.gamePlayerStats.find(o => o.playerId === id)
        }

        useEffect(() => {
            gamesFetch().then((response) => {

                console.log(response.data)
                setGames(response.data);

                Promise.all(response.data.map(game => {
                    console.log(game.player1)
                    return [fetchUsername(game.player1), fetchUsername(game.player2)]
                }).flat()).then((value) => {
                    setUsernames(value.map(user => {
                        return {id: user.data.id, username: user.data.username}
                    }));
                    setLoadingGames(false);
                })
            })
        }, [])

        useEffect(() => {
            fetchBlog().then(response => {
                setLoadingPosts(false)
                console.log(response.data)
                setPosts(response.data);
            })
        }, [])


        const findUsername = (id) => {
            const obj = usernames.find(o => o.id === id);
            if (obj) {
                return obj.username;
            } else {
                return ''
            }
        }


        return (
            <>
                <PageHeader title={"Global Caps League"} showLogo/>
                {!loadingGames && !loadingPosts ? <Grid className={classes.root}>
                    <Grid container>
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
                                        const player1Stats = findPlayerStats(game, game.player1)
                                        const player2Stats = findPlayerStats(game, game.player2)
                                        return (
                                            <Grid key={game.player1 + game.player2 + game.time} item xs={12}>
                                                <div className={classes.neon} style={{padding: 10}}>
                                                    <Typography
                                                        color={"primary"}>{getGameTypeString(game.gameType)}</Typography>
                                                    <div className={classes.header}>
                                                        <Typography>{player1Stats.score} : {player2Stats.score} {findUsername(game.player1)} vs {findUsername(game.player2)}</Typography>
                                                    </div>
                                                    <Typography>{new Date(game.time).toDateString()}</Typography>
                                                </div>
                                            </Grid>
                                        )
                                    })}
                                </Grid>
                            </div>

                        </Grid>
                    </Grid>
                </Grid> : <LoadingComponent/>}
            </>);
    }
;

export default HomeComponent;
