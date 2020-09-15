import React from 'react';
import {Divider, Grid, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import Card from "@material-ui/core/Card";
import PageHeader from "../misc/PageHeader";

const HomeComponent = () => {

    const classes = mainStyles();
    const theme = useTheme();

    const posts = [{
        header: "New Season",
        date: new Date(),
        author: "Mike",
        text: "Capser version 2 was released October 22 2020. It includes many changes to game modes, page looks statistics and more " +
            "from now on players can enjoy not only Singles games but also, easy caps requiring less beer, unranked games without points," +
            "and doubles featuring teams of two. Enjoy the new version of Global Caps League website adn enjoy Caps."
    },
        {
            header: "Capser version 2.0",
            date: new Date(),
            author: "Mike",
            text: "Capser version 2 was released October 22 2020. It includes many changes to game modes, page looks statistics and more " +
                "from now on players can enjoy not only Singles games but also, easy caps requiring less beer, unranked games without points," +
                "and doubles featuring teams of two. Enjoy the new version of Global Caps League website adn enjoy Caps."
        }]

    const games = [{
        player1: "Aether",
        player2: "Bartq",
        player1Score: 11,
        player2Score: 8,
        date: new Date()
    }, {
        player1: "Woj",
        player2: "Bartq",
        player1Score: 0,
        player2Score: 15,
        date: new Date()
    }, {
        player1: "Aether",
        player2: "Woj",
        player1Score: 11,
        player2Score: 5,
        date: new Date()
    }]
    return (
        <>
           <PageHeader title={"Global Caps League"} showLogo/>
            <Grid className={classes.root}>
                <Grid container>
                    <Grid item sm={8} className={classes.squareShine}>
                        <div className={classes.leftOrientedWrapperNoPadding}>
                            <Typography variant={"h5"}>News Feed</Typography>
                            <div style={{padding: 10}}>
                                <Divider/>
                                {posts.map(post => {
                                    return (
                                        <div key={post.header} style={{textAlign: "left"}}>
                                            <Typography variant={"h5"}
                                                        style={{color: theme.palette.primary.main}}
                                                        align={"left"}
                                                        className={classes.textHeading}>{post.header}</Typography>
                                            <Typography align={"left"} variant={"caption"}
                                                        className={classes.textSubheading}>{post.date.toDateString()}</Typography>
                                            <Typography align={"left"} className={classes.text}>{post.text}</Typography>
                                            <Typography align={"left"} variant={"caption"}
                                                        className={classes.text}>{post.author}</Typography>
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
                            <Grid container spacing={2} >
                            {games.map(game => {
                                return (
                                    <Grid key={game.date.toDateString() + game.player1 + game.player2} item xs={12}>
                                        <Card style={{textAlign: "left"}}>
                                            <Typography>{game.player1} vs {game.player2}</Typography>
                                            <Typography>{game.player1Score} : {game.player2Score}</Typography>
                                            <Typography>{game.date.toDateString()}</Typography>
                                        </Card>
                                    </Grid>
                                )
                            })}
                        </Grid>
                        </div>

                    </Grid>
                </Grid>

            </Grid>
        </>);
};

export default HomeComponent;
