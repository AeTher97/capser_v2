import React from 'react';
import PageHeader from "../../misc/PageHeader";
import {Button, Divider, TableBody, Typography} from "@material-ui/core";
import mainStyles from "../../../misc/styles/MainStyles";
import Table from "@material-ui/core/Table";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import TableHead from "@material-ui/core/TableHead";
import {getHeaders} from "../../../utils/TokenUtils";
import {useHistory} from "react-router-dom";

const SinglesComponent = () => {

    const history = useHistory();

    const games = [
        {
            player1: 'aether',
            player2: 'bartq',
            player1Score: 11,
            player2Score: 8,
            date: new Date(),
            player1Sinks : 16,
            player2Sinks: 8,
            player1Rebuttals: 5,
            player2Rebuttals: 6,
            player1Beers : 2,
            player2Beers: 1,
            player1PointsChange: '+6',
            player2PointsChange: '-4'
        },{
            player1: 'aether',
            player2: 'bartq',
            player1Score: 11,
            player2Score: 8,
            date: new Date(),
            player1Sinks : 16,
            player2Sinks: 8,
            player1Rebuttals: 5,
            player2Rebuttals: 6,
            player1Beers : 2,
            player2Beers: 1,
            player1PointsChange: '+6',
            player2PointsChange: '-4'
        },
        {
            player1: 'aether',
            player2: 'bartq',
            player1Score: 11,
            player2Score: 8,
            date: new Date(),
            player1Sinks : 16,
            player2Sinks: 8,
            player1Rebuttals: 5,
            player2Rebuttals: 6,
            player1Beers : 2,
            player2Beers: 1,
            player1PointsChange: '+6',
            player2PointsChange: '-4'
        }

    ]

    const classes = mainStyles();
    return (
        <div>
            <PageHeader title={"Singles"}/>
            <div className={classes.root}>
                <div className={classes.leftOrientedWrapperNoPadding}>
                    <div className={classes.header}>
                    <Typography variant={"h5"} align={"left"} style={{marginRight: 30}}>Games</Typography>
                        <Button onClick={() => {history.push("/secure/addSinglesGame")}}>Add game</Button>
                    </div>
                    <div className={classes.paddedContent}>
                        <Divider/>
                        <Table style={{width: '100%'}}>
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
                                    return( <TableRow>
                                        <TableCell>{game.player1} vs {game.player2}</TableCell>
                                        <TableCell>{game.player1Score} : {game.player2Score}</TableCell>
                                        <TableCell>{game.date.toDateString()}</TableCell>
                                        <TableCell>{game.player1Rebuttals} : {game.player2Rebuttals}</TableCell>
                                        <TableCell>{game.player1Sinks} : {game.player2Sinks}</TableCell>
                                        <TableCell>{game.player1Beers} : {game.player2Beers}</TableCell>
                                        <TableCell>{game.player1PointsChange} : {game.player2PointsChange}</TableCell>
                                    </TableRow>)
                                })}
                            </TableBody>

                        </Table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SinglesComponent;
