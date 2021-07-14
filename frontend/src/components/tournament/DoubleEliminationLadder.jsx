import React, {useEffect, useRef} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import {Typography} from "@material-ui/core";
import BracketEntry from "./BracketEntry";
import BracketPath from "./BracketPath";


export const getDroString = (round) => {
    switch (round) {
        case "D_RO_16":
            return "Ro 16"
        case "D_RO_12":
            return ""
        case "D_RO_8":
            return "Quarter finals"
        case "D_RO_6":
            return ""
        case "D_RO_4":
            return "Semi finals"
        case "D_RO_3":
            return ""
        case "D_RO_2":
            return "Bracket Finals"
        case "D_RO_1":
            return "Grand Finals"

    }
}

function powerOfTwo(x) {
    return (Math.log(x) / Math.log(2)) % 1 === 0;
}


const DoubleEliminationLadder = ({
                                     gameType,
                                     bracketEntries,
                                     lowestRound,
                                     isOwner,
                                     openAddGameDialog,
                                     openSkipDialog,
                                     winner,
                                     teams
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
            levels.push({
                type: entry.bracketEntryType, entries: [entry]
            })
        }
    })
    levels.sort((a, b) => {
        return parseInt(b.type.split("_")[2]) - parseInt(a.type.split("_")[2])
    })
    levels.forEach(level => {

        level.entries.sort((a, b) => {
            return a.coordinate - b.coordinate
        })
    })


    let currentHorizontalOffset = 30;
    let horizontalOffsetLevel = 250;
    let verticalOffsetLevel = 100;
    let currentVertical = 0;
    let additionalVerticalOffset = 60;
    let lastPower2;
    let firstLowerRow = true;

    return (
        <>
            <div className={[styles.container].join(' ')} ref={ref}>
                {levels.map(level => {
                    if (powerOfTwo(level.type.split("_")[2])) {
                        const value = (
                            <div key={level.type}>
                                <div style={{
                                    position: "absolute",
                                    top: -60 + verticalOffsetLevel * currentVertical + additionalVerticalOffset,
                                    left: currentHorizontalOffset
                                }}>
                                    <Typography variant={"h5"} color={"textSecondary"} noWrap>
                                        {getDroString(level.type)}
                                    </Typography>
                                </div>
                                {level.entries.map((entry) => {
                                    const value = (<div key={entry.id} className={styles.entry} style={{
                                        top: verticalOffsetLevel * currentVertical + additionalVerticalOffset,
                                        left: currentHorizontalOffset
                                    }}>
                                        <BracketEntry isOwner={isOwner} bracketEntry={entry}
                                                      showPath={level.type !== "D_RO_1"}
                                                      pathElongation={0}
                                                      openAddGameDialog={openAddGameDialog}
                                                      openSkipDialog={openSkipDialog}
                                                      gameType={gameType}
                                                      teams={teams}
                                        />
                                        {level.type !== lowestRound && <>
                                            <BracketPath height={verticalOffsetLevel / 4}
                                                         width={level.type === "D_RO_1" ? 50 : 300}
                                                         left={level.type === "D_RO_1" ? -50 : -300}
                                                         top={-verticalOffsetLevel / 4 + 35}
                                                         pathType={"top"}/>
                                            {level.type !== 'D_RO_1' && currentVertical > parseInt(level.type.split("_")[2]) / 2 - 1 &&
                                            <BracketPath height={verticalOffsetLevel / 4}
                                                         width={50} left={-50}
                                                         top={-verticalOffsetLevel / 4 + 35}
                                                         pathType={"top"}/>}
                                            {currentVertical < parseInt(level.type.split("_")[2]) / 2 &&
                                            <BracketPath height={verticalOffsetLevel / 4}
                                                         width={level.type === "D_RO_1" ? 50 : 300}
                                                         left={level.type === "D_RO_1" ? -50 : -300}
                                                         top={35}
                                                         pathType={"bottom"}
                                            />}</>}
                                    </div>)
                                    currentVertical += 1;

                                    return value;
                                })
                                }</div>)
                        currentVertical = 0;
                        lastPower2 = level.type;
                        currentHorizontalOffset += horizontalOffsetLevel;
                        additionalVerticalOffset += verticalOffsetLevel / 2;
                        verticalOffsetLevel *= 2;
                        return value;
                    } else {
                        currentVertical = parseInt(lastPower2.split("_")[2]) / 4;
                        const value = (
                            <div key={level.type}>
                                <div style={{
                                    position: "absolute",
                                    top: -60 + additionalVerticalOffset,
                                    left: currentHorizontalOffset
                                }}>
                                    <Typography variant={"h4"} color={"textSecondary"} noWrap>
                                        {getDroString(level.type)}
                                    </Typography>
                                </div>
                                {level.entries.map((entry) => {
                                    const value = (<div key={entry.id} className={styles.entry} style={{
                                        top: verticalOffsetLevel * currentVertical + additionalVerticalOffset,
                                        left: currentHorizontalOffset
                                    }}>
                                        <BracketEntry isOwner={isOwner} bracketEntry={entry} showPath={true}
                                                      pathElongation={100}
                                                      openAddGameDialog={openAddGameDialog}
                                                      openSkipDialog={openSkipDialog}
                                                      gameType={gameType}
                                                      teams={teams}
                                        />
                                        {level.type !== lowestRound && <>
                                            {!firstLowerRow && <BracketPath height={verticalOffsetLevel / 4}
                                                                            width={50} left={-50}
                                                                            top={35}
                                                                            pathType={"bottom"}
                                            />}</>}
                                    </div>)
                                    currentVertical += 1;
                                    return value;
                                })
                                }</div>)
                        currentVertical = 0;
                        if (firstLowerRow) {
                            firstLowerRow = false;
                        }

                        currentHorizontalOffset += horizontalOffsetLevel;
                        return value;
                    }

                })}
                <div className={styles.entry} style={{
                    top: verticalOffsetLevel * currentVertical + additionalVerticalOffset - verticalOffsetLevel / 2 / 2 + 20,
                    left: currentHorizontalOffset - 80
                }}>
                    <Typography variant={"h5"}>Winner</Typography>
                    <Typography
                        variant={"h5"}>{winner ? teams ? winner.team.name : winner.user.username : "TBD"}</Typography>
                </div>
            </div>
            <div
                style={{height: levels.sort((a, b) => a.entries.length - b.entries.length)[0].entries.length / 2 * verticalOffsetLevel}}/>
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

export default DoubleEliminationLadder;
