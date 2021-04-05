import React, {useState} from 'react';
import {Tooltip, Typography, useTheme} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import AddOutlinedIcon from '@material-ui/icons/AddOutlined';
import {findPlayerStats} from "../pages/singles/SinglesGamesList";
import {useHasRole} from "../../utils/SecurityUtils";
import SkipNextOutlinedIcon from '@material-ui/icons/SkipNextOutlined';
import {getRequestGameTypeString} from "../../utils/Utils";
import {useHistory} from "react-router-dom";

const BracketEntry = ({bracketEntry, showPath, isOwner, openAddGameDialog, openSkipDialog, pathElongation=0}) => {

    const entryStyle = entryStyles();
    const theme = useTheme();
    const classes = mainStyles();
    const borderColor = theme.palette.divider;
    const hasRole = useHasRole();
    const plusBaseColor = "#404040"
    const plusActiveColor = "#6b6b6b"
    const [plusColor, setPlusColor] = useState('red');
    const [skipColor, setSkipColor] = useState(plusBaseColor);
    const history = useHistory();


    const showPlus = isOwner && bracketEntry.player1 && bracketEntry.player2 && !bracketEntry.game && !bracketEntry.forfeited && hasRole('ADMIN');
    const getUsername = (player) => {
        if (!player) {
            return ""
        } else {
            return player.username;
        }
    }


    let flexType;
    if (!bracketEntry.player1 && !bracketEntry.player2) {
        flexType = 'center';
    } else if (!bracketEntry.player2) {
        flexType = 'flex-start';
    } else if (!bracketEntry.player1) {
        flexType = 'flex-end'
    }

    return (
        <div style={{display: "flex", flexDirection: "row", justifyContent: "flex-start"}}>
            <div style={{height: 71, display: "flex", flexDirection: "column", justifyContent: flexType}}>
                <div
                    className={[entryStyle.entry, !bracketEntry.bye ? classes.standardBorder : classes.disabledBorder].join(' ')}
                    style={{padding: 0, margin: 0}}>
                    <div className={[entryStyle.border, entryStyle.padding].join(' ')}
                         style={{borderBottom: '1px solid ' + borderColor}}>
                        <div style={{display: "flex", flexDirection: "row"}}>
                            <Typography style={{
                                flex: 1,
                                opacity: bracketEntry.game && bracketEntry.player1.id !== bracketEntry.game.winner || (bracketEntry.player1 && bracketEntry.forfeitedId === bracketEntry.player1.id) ? 0.5 : 1
                            }}> {getUsername(bracketEntry.player1)}</Typography>
                            {bracketEntry.player1 && bracketEntry.game && <Typography
                                style={{marginRight: 15}}>{findPlayerStats(bracketEntry.game, bracketEntry.player1.id).score}</Typography>}
                        </div>
                    </div>
                    <div className={entryStyle.padding}>
                        <div style={{display: "flex", flexDirection: "row"}}>
                            <Typography style={{
                                flex: 1,
                                opacity: (bracketEntry.game && bracketEntry.player2.id !== bracketEntry.game.winner) || (bracketEntry.player2 && bracketEntry.forfeitedId === bracketEntry.player2.id) ? 0.5 : 1
                            }}>{getUsername(bracketEntry.player2)}</Typography>
                            {bracketEntry.player2 && bracketEntry.game && <Typography
                                style={{marginRight: 15}}>{findPlayerStats(bracketEntry.game, bracketEntry.player2.id).score}</Typography>}
                        </div>
                    </div>
                </div>
                {showPlus && <div style={{
                    borderRadius: '50%',
                    backgroundColor: plusColor,
                    position: "relative",
                    top: -49, left: 137,
                    color: "white",
                    height: plusColor === "red" ? 24 : 22,
                    width: plusColor === "red" ? 24 : 22,
                    cursor: "pointer",
                    border: plusColor === "red" ? "none" : "1px solid white",
                    padding: plusColor === "red" ? 2 : 1.25,
                    zIndex: 10
                }} onMouseEnter={() => setPlusColor("#c70000")} onMouseLeave={() => setPlusColor("red")}>
                    <AddOutlinedIcon
                        onClick={() => openAddGameDialog(bracketEntry.id, bracketEntry.player1, bracketEntry.player2, null, null)}/>
                </div>}

                {showPlus && <div style={{
                    borderRadius: '50%',
                    backgroundColor: skipColor,
                    position: "relative",
                    top: plusColor === "red" ? -77 : -76,
                    left: 170,
                    color: "white",
                    height: skipColor === plusBaseColor ? 24 : 22,
                    width: skipColor === plusBaseColor ? 24 : 22,
                    cursor: "pointer",
                    border: skipColor === plusBaseColor ? "none" : "1px solid white",
                    padding: skipColor === plusBaseColor ? 2 : 1.25,
                    zIndex: 10000
                }} onMouseEnter={() => setSkipColor(plusActiveColor)} onMouseLeave={() => setSkipColor(plusBaseColor)}>
                    <SkipNextOutlinedIcon
                        onClick={() => openSkipDialog(bracketEntry.id, bracketEntry.player1, bracketEntry.player2)}/>
                </div>}

                {bracketEntry.forfeited && <div style={{
                    borderRadius: '50%',
                    position: "relative",
                    color: "grey",
                    top: -90,
                    left: 80,
                    zIndex: 10
                }} onMouseEnter={() => setSkipColor(plusActiveColor)} onMouseLeave={() => setSkipColor(plusBaseColor)}>
                    <Typography color={"inherit"}>Forfeited</Typography>
                </div>}

                {!showPlus && bracketEntry.game && <Tooltip title={"Detailed game info"}
                                                            onClick={() => history.push(`/${getRequestGameTypeString(bracketEntry.game.gameType)}/${bracketEntry.game.id}`)}>
                    <div style={{
                        backgroundColor: plusBaseColor,
                        position: "relative",
                        top: -47,
                        left: 140,
                        width: 20,
                        minHeight: 20,
                        display: 'flex',
                        fontSize: 12,
                        justifyContent: 'center',
                        alignItems: 'center',
                        color: 'white',
                        borderRadius: '50%',
                        border: '1px solid grey',
                        cursor: 'pointer',
                        zIndex: 10

                    }} onMouseEnter={() => setSkipColor(plusActiveColor)}
                         onMouseLeave={() => setSkipColor(plusBaseColor)}>
                        i
                    </div>
                </Tooltip>}

            </div>
            {showPath && <div style={{
                width: 49+pathElongation,
                height: 35,
                display: "inline-block",
                borderBottom: `1px solid ${borderColor}`
            }}/>}
        </div>
    );
};

BracketEntry.propTypes = {};

const entryStyles = makeStyles(theme => ({
    entry: {
        width: 150,
    },
    border: {
        padding: 5
    },
    padding: {
        padding: 5,
        paddingLeft: 10
    }
}))

export default BracketEntry;
