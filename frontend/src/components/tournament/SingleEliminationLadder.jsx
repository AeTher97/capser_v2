import React, {useEffect, useRef, useState} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import BracketEntry from "./BracketEntry";
import BracketPath from "./BracketPath";
import {Typography} from "@material-ui/core";

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

const SingleEliminationLadder = ({
                                     gameType,
                                     bracketEntries,
                                     lowestRound,
                                     isOwner,
                                     openAddGameDialog,
                                     openSkipDialog,
                                     winner,
                                     teams,
                                     highlighted,
                                     onHighlight,
                                     onHighlightEnd
                                 }) => {
    const styles = ladderStyles();

    const ref = useRef();

    useEffect(() => {
        if (ref.current) {
            ref.current.id = "tournamentContainer"
        }
    }, [ref])


    const levels = [];
    bracketEntries.forEach(entry => {
        const array = levels.find(obj => obj.type === entry.bracketEntryType)
        if (array) {
            array.entries.push(entry);
        } else {
            levels.push({type: entry.bracketEntryType, entries: [entry]})
        }
    })

    levels.sort((a, b) => {
        return parseInt(b.type.split("_")[1]) - parseInt(a.type.split("_")[1])
    })

    levels.forEach(level => {
        level.entries.sort((a, b) => {
            return a.coordinate - b.coordinate
        })
    })

    let currentHorizontalOffset = 30;
    let horizontalOffsetLevel = 300;
    let verticalOffsetLevel = 100;
    let currentVertical = 0;
    let additionalVerticalOffset = 60;


    console.log(onHighlight)
    return (
        <>
            <div className={[styles.container].join(' ')} ref={ref}>
                {levels.map(level => {
                    const value = (
                        <div key={level.type}>
                            <div style={{
                                position: "absolute",
                                top: -60 + additionalVerticalOffset,
                                left: currentHorizontalOffset
                            }}>
                                <Typography variant={"h5"} color={"textSecondary"} noWrap>
                                    {getRoString(level.type)}
                                </Typography>
                            </div>
                            {level.entries.map((entry) => {
                                const value = (<div key={entry.id} className={styles.entry} style={{
                                    top: verticalOffsetLevel * currentVertical + additionalVerticalOffset,
                                    left: currentHorizontalOffset
                                }}>
                                    <BracketEntry isOwner={isOwner} bracketEntry={entry} showPath={true}
                                                  openAddGameDialog={openAddGameDialog} openSkipDialog={openSkipDialog}
                                                  gameType={gameType} teams={teams} highlighted={highlighted}
                                                  onHighlight={onHighlight} onHighlightEnd={onHighlightEnd}
                                    />
                                    {level.type !== lowestRound && <>
                                        <BracketPath height={verticalOffsetLevel / 4}
                                                     width={100} left={-100}
                                                     top={-verticalOffsetLevel / 4 + 35}
                                                     pathType={"top"}/>
                                        <BracketPath height={verticalOffsetLevel / 4}
                                                     width={100} left={-100}
                                                     top={35}
                                                     pathType={"bottom"}
                                        /></>}
                                </div>)
                                currentVertical += 1;
                                return value;
                            })
                            }</div>)
                    currentVertical = 0;
                    currentHorizontalOffset += horizontalOffsetLevel;
                    additionalVerticalOffset += verticalOffsetLevel / 2;
                    verticalOffsetLevel *= 2;
                    return value;

                })}
                <div className={styles.entry} style={{
                    top: verticalOffsetLevel * currentVertical + additionalVerticalOffset - verticalOffsetLevel / 2 / 2 + 20,
                    left: currentHorizontalOffset - 90
                }}>
                    <Typography variant={"h5"}>Winner</Typography>
                    <Typography
                        variant={"h5"}>{winner ? teams ? winner.team.name : winner.user.username : "TBD"}</Typography>
                </div>
            </div>
            <div
                style={{height: levels.slice().sort((a, b) => a.entries.length - b.entries.length)[0].entries.length / 2 * verticalOffsetLevel}}/>
        </>
    );
};


const ladderStyles = makeStyles(theme => ({
    container: {
        height: '100%',
        overflow: "scroll",
        position: "relative",
        scrollbarColor: "white"

    },
    entry: {
        position: "absolute",
        display: "block",
    }
}))

export default SingleEliminationLadder;
