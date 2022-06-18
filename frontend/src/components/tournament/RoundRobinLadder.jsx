import React, {useEffect, useRef} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import BracketEntry from "./BracketEntry";
import {Table, TableBody, TableCell, TableHead, TableRow, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";

export const getRoString = (round) => {
    switch (round) {
        case "RO_64":
            return "Ro 64"
        case "RO_32":
            return "Ro 32"
        case "RO_16":
            return "Ro 16"
        case "RO_8":
            return "Quarter finals"
        case "RO_4":
            return "Semi finals"
        case "RO_2":
            return "Final"

    }
}

const RoundRobinLadder = ({
                              playerCount,
                              gameType,
                              bracketEntries,
                              isOwner,
                              openAddGameDialog,
                              openSkipDialog,
                              teams,
                              highlighted,
                              onHighlight,
                              onHighlightEnd,
                              competitorTournamentStats,
                              competitors
                          }) => {
    const styles = ladderStyles();
    const classes = mainStyles();

    const ref = useRef();


    let evenPlayerCount = playerCount % 2 === 0;
    let numberOfRounds = evenPlayerCount ? playerCount - 1 : playerCount;

    useEffect(() => {
        if (ref.current) {
            ref.current.id = "tournamentContainer"
        }
    }, [ref])


    const levels = [];
    for (let i = 0; i < numberOfRounds; i++) {
        const level = {round: i};
        level.entries = [];
        for (const element of bracketEntries) {
            if (Math.floor(element.coordinate / 1000.0) === i) {
                level.entries.push(element);
            }
        }
        levels.push(level);
    }
    let index = 0;


    return (
        <>
            {competitors && <div style={{marginLeft: 20, color: 'white'}}>
                <Typography variant={"h5"} color={"textSecondary"} noWrap>
                    Results
                </Typography>
                <div style={{display: 'flex', position: "relative", left: -10}}>
                    <div className={classes.standardBorder}
                         style={{display: 'flex', flexDirection: 'column', padding: 0}}>
                        <Table size={"small"} padding={"none"}>
                            <TableHead>
                                <TableRow style={{borderBottom: '1px solid gray'}}>
                                    <TableCell style={{padding: 5}}>Player</TableCell>
                                    <TableCell style={{padding: 5}}> Games</TableCell>
                                    <TableCell style={{padding: 5}}>Points</TableCell>
                                    <TableCell style={{padding: 5}}>Difference</TableCell>
                                    <TableCell style={{padding: 5}}>Sinks</TableCell>
                                    <TableCell style={{padding: 5}}>Difference</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {competitorTournamentStats.map(stats => {
                                    index++;
                                    return <TableRow style={{borderBottom: '1px solid rgba(255,255,255,0.2)'}}>
                                        <TableCell style={{padding: 5, paddingRight: 15}}>
                                            {index}. {
                                            teams ? competitors.find(competitor => competitor.id === stats.competitorId).name : competitors.find(competitor => competitor.id === stats.competitorId).username}
                                        </TableCell>
                                        <TableCell>{stats.wins}-{stats.losses}</TableCell>
                                        <TableCell>{stats.points}-{stats.pointsLost}</TableCell>
                                        <TableCell>{stats.points - stats.pointsLost}</TableCell>
                                        <TableCell>{stats.sinks}-{stats.sinksLost}</TableCell>
                                        <TableCell>{stats.sinks - stats.sinksLost}</TableCell>
                                    </TableRow>
                                })

                                }
                            </TableBody>
                        </Table>

                    </div>
                </div>
            </div>}
            <div className={[styles.container].join(' ')} ref={ref}>
                {levels.map(level => {
                    return <div key={level.round} style={{padding: 20}}>
                        <Typography variant={"h5"} color={"textSecondary"} noWrap>
                            Round {level.round + 1}
                        </Typography>
                        {level.entries.map((entry) =>
                            <div key={entry.id} className={styles.entry}>
                                <BracketEntry isOwner={isOwner} bracketEntry={entry}
                                              openAddGameDialog={openAddGameDialog}
                                              openSkipDialog={openSkipDialog}
                                              gameType={gameType} teams={teams}
                                              highlighted={highlighted}
                                              onHighlight={onHighlight} onHighlightEnd={onHighlightEnd}
                                />

                            </div>
                        )}
                    </div>;

                })}
            </div>
        </>

    )
        ;
};


const ladderStyles = makeStyles(theme => ({
    container: {
        scrollbarColor: "white",
        display: 'flex',
        flexWrap: 'wrap'
    },
    entry: {
        marginBottom: 20,
        display: "block",
    }
}))

export default RoundRobinLadder;
