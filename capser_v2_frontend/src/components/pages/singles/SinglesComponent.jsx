import React, {useEffect, useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import {Button, Divider, TableBody, Typography} from "@material-ui/core";
import mainStyles from "../../../misc/styles/MainStyles";
import Table from "@material-ui/core/Table";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import TableHead from "@material-ui/core/TableHead";
import {useHistory} from "react-router-dom";
import {useGameListFetch} from "../../../data/Game";
import {useUsernameFetch} from "../../../data/UsersFetch";
import CircularProgress from "@material-ui/core/CircularProgress";

const SinglesComponent = () => {

    const history = useHistory();
    const [games, setGames] = useState([])
    const [loading, setLoading] = useState(true);
    const [usernames, setUsernames] = useState([])
    const fetchGames = useGameListFetch('SINGLES');
    const fetchUsername = useUsernameFetch();

    useEffect(() => {
        fetchGames().then((response) => {
            console.log(response.data.content)
            setGames(response.data.content)
            Promise.all(response.data.content.map(game => {
                console.log(game.player1)
                return [fetchUsername(game.player1), fetchUsername(game.player2)]
            }).flat()).then((value) => {
                setUsernames(value.map(user => {
                    return {id: user.data.id, username: user.data.username}
                }));
                setLoading(false);
            })
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

    const findPlayerStats = (game, id) => {
        return game.gamePlayerStats.find(o => o.playerId === id)
    }


    // const games = [
    //     {
    //         player1: 'aether',
    //         player2: 'bartq',
    //         player1Score: 11,
    //         player2Score: 8,
    //         date: new Date(),
    //         player1Sinks : 16,
    //         player2Sinks: 8,
    //         player1Rebuttals: 5,
    //         player2Rebuttals: 6,
    //         player1Beers : 2,
    //         player2Beers: 1,
    //         player1PointsChange: '+6',
    //         player2PointsChange: '-4'
    //     },{
    //         player1: 'aether',
    //         player2: 'bartq',
    //         player1Score: 11,
    //         player2Score: 8,
    //         date: new Date(),
    //         player1Sinks : 16,
    //         player2Sinks: 8,
    //         player1Rebuttals: 5,
    //         player2Rebuttals: 6,
    //         player1Beers : 2,
    //         player2Beers: 1,
    //         player1PointsChange: '+6',
    //         player2PointsChange: '-4'
    //     },
    //     {
    //         player1: 'aether',
    //         player2: 'bartq',
    //         player1Score: 11,
    //         player2Score: 8,
    //         date: new Date(),
    //         player1Sinks : 16,
    //         player2Sinks: 8,
    //         player1Rebuttals: 5,
    //         player2Rebuttals: 6,
    //         player1Beers : 2,
    //         player2Beers: 1,
    //         player1PointsChange: '+6',
    //         player2PointsChange: '-4'
    //     }
    //
    // ]

    const classes = mainStyles();
    return (
        <div>
            <PageHeader title={"Singles"}/>
            <div className={classes.root}>
                <div className={classes.leftOrientedWrapperNoPadding}>
                    <div className={classes.header}>
                        <Typography variant={"h5"} align={"left"} style={{marginRight: 30}}>Games</Typography>
                        <Button onClick={() => {
                            history.push("/secure/addSinglesGame")
                        }}>Add game</Button>
                    </div>
                    <div className={[classes.paddedContent, classes.squareShine].join(' ')}>
                        <Divider/>
                        {!loading ? <Table style={{width: '100%'}}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Players</TableCell>
                                        <TableCell>Score</TableCell>
                                        <TableCell>Time</TableCell>
                                        <TableCell>Rebuttals</TableCell>
                                        <TableCell>Sinks</TableCell>
                                        <TableCell>Beers</TableCell>
                                        <TableCell>Points change</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {games.map(game => {
                                        const player1Stats = findPlayerStats(game, game.player1);
                                        const player2Stats = findPlayerStats(game, game.player2);
                                        return (<TableRow key={game.id}>
                                            <TableCell>{findUsername(game.player1)} vs {findUsername(game.player2)}</TableCell>
                                            <TableCell>{player1Stats.score} : {player2Stats.score}</TableCell>
                                            <TableCell>{new Date(game.time).toDateString()}</TableCell>
                                            <TableCell>{player1Stats.rebuttals} : {player2Stats.rebuttals}</TableCell>
                                            <TableCell>{player1Stats.sinks} : {player2Stats.sinks}</TableCell>
                                            <TableCell>{player1Stats.beersDowned} : {player2Stats.beersDowned}</TableCell>
                                            <TableCell>{player1Stats.pointsChange} : {player2Stats.pointsChange}</TableCell>
                                        </TableRow>)
                                    })
                                    }
                                </TableBody>
                            </Table> :
                            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                                <CircularProgress/>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SinglesComponent;
