import React from 'react';
import PropTypes from 'prop-types';
import {Divider, Typography, useTheme} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import AddOutlinedIcon from '@material-ui/icons/AddOutlined';
import {findPlayerStats} from "../pages/singles/SinglesGamesList";
import {useHasRole} from "../../utils/SecurityUtils";

const BracketEntry = ({bracketEntry, showPath, isOwner,openAddGameDialog}) => {

    const entryStyle = entryStyles();
    const theme = useTheme();
    const classes = mainStyles();
    const borderColor = theme.palette.divider;
    const hasRole = useHasRole();

    const showPlus = isOwner && bracketEntry.player1 && bracketEntry.player2 && !bracketEntry.game && hasRole('ADMIN');
    const getUsername = (player) =>{
        if(!player){
            return ""
        } else {
            return player.username;
        }
    }

    let flexType;
    if (!bracketEntry.player1 && !bracketEntry.player2){
        flexType = 'center';
    } else if(!bracketEntry.player2){
        flexType ='flex-start';
    } else if(!bracketEntry.player1){
        flexType='flex-end'
    }

        return (
            <div style={{display: "flex", flexDirection:"row", justifyContent: "flex-start"}}>
            <div style={{height: 71, display: "flex", flexDirection: "column", justifyContent: flexType}}>
                    <div className={[entryStyle.entry, !bracketEntry.bye ?  classes.standardBorder : classes.disabledBorder].join(' ')} style={{padding: 0, margin: 0}}>
                        <div className={[entryStyle.border, entryStyle.padding].join(' ')} style={{borderBottom: '1px solid ' + borderColor}}>
                            <div style={{display: "flex", flexDirection: "row"}}>
                            <Typography style={{flex:1}}>{getUsername(bracketEntry.player1)}</Typography>
                                {bracketEntry.player1 && bracketEntry.game &&  <Typography
                                    style={{marginRight: 15}}>{findPlayerStats(bracketEntry.game, bracketEntry.player1.id).score}</Typography>}
                            </div>
                        </div>
                        <div className={entryStyle.padding}>
                            <div style={{display: "flex", flexDirection: "row"}}>
                            <Typography style={{flex: 1}}>{getUsername(bracketEntry.player2)}</Typography>
                                {bracketEntry.player2 && bracketEntry.game &&  <Typography
                                    style={{marginRight: 15}}>{findPlayerStats(bracketEntry.game, bracketEntry.player2.id).score}</Typography>}
                            </div>
                        </div>
                    </div>
                {showPlus && <div style={{borderRadius: '50%',
                    backgroundColor: 'red',
                    position: "relative",
                    top: -47, left: 140,
                    color: "black",
                    height:24,
                    width:24,
                    cursor: "pointer"
                }}>
                    <AddOutlinedIcon onClick={() => openAddGameDialog(bracketEntry.id,bracketEntry.player1,bracketEntry.player2, null, null)}/>
                </div>}
            </div>
                {showPath && <div style={{
                    width: 49,
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
