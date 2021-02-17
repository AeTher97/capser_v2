import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {Button, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import {makeStyles} from "@material-ui/core/styles";
import BoldTyphography from "../misc/BoldTyphography";
import PersonOutlineIcon from '@material-ui/icons/PersonOutline';
import FetchSelectField from "../misc/FetchSelectField";
import {useDispatch} from "react-redux";
import {showError} from "../../redux/actions/alertActions";
import ClearIcon from '@material-ui/icons/Clear';

const TournamentsPlayers = ({players, addPlayer, removePlayer, savePlayers, adding, max}) => {
    const classes = mainStyles();
    const theme = useTheme();
    const dispatch = useDispatch();

    const [playersIds, setPlayersIds] = useState();

    useEffect(() => {
        setPlayersIds(players.map(player => player.id));
    }, [players])


    const styles = playersStyles();

    return (
        <div>
            <div style={{padding: 10, display: "flex", borderBottom: '1px solid ' + theme.palette.divider}}>
                <Typography variant={"h5"} style={{marginRight: 10, flex: 1}}>{players.length} Players</Typography>
                {adding && <FetchSelectField label={"Select Player"} onChange={(value) => {
                    if (playersIds.length === max) {
                        dispatch(showError("Too many players"))
                        return;
                    }
                    addPlayer(value);
                }}
                                             clearOnChange
                                             searchYourself
                                             url={"/users/search"}
                                             nameParameter={"username"}/>}
            </div>
            {players.map(player => {
                return <div key={player.id} className={[styles.player, classes.header].join(' ')}>
                    <PersonOutlineIcon fontSize={"small"}/>
                    <BoldTyphography>{player.username}</BoldTyphography>
                    {adding && <div style={{flex: 1, display: "flex", justifyContent: "flex-end", color: "red"}}>
                        <div style={{cursor: "pointer"}} onClick={() => {
                            removePlayer(player.id);
                        }
                        }>
                            <ClearIcon fontSize={"small"}/>
                        </div>
                    </div>}
                </div>
            })}
            {adding && <div style={{padding: 10}}>
                <Button onClick={() => {
                    savePlayers(playersIds);
                }}>Save</Button>
            </div>}
        </div>
    );
};

TournamentsPlayers.propTypes = {};

const playersStyles = makeStyles(theme => ({
    player: {
        color: "white",
        padding: 10,
        borderBottom: '1px solid ' + theme.palette.divider
    }
}))

export default TournamentsPlayers;
