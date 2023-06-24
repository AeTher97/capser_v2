import React, {useEffect, useRef, useState} from 'react';
import {Button, Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import BoldTyphography from "../misc/BoldTyphography";
import PersonOutlineIcon from '@material-ui/icons/PersonOutline';
import PeopleIcon from '@material-ui/icons/People';

import FetchSelectField from "../../utils/FetchSelectField";
import {useDispatch} from "react-redux";
import {showError} from "../../redux/actions/alertActions";
import ClearIcon from '@material-ui/icons/Clear';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {useDrag} from "react-dnd";
import {useHasRole} from "../../utils/SecurityUtils";

const type = "Player";

const Competitor = ({player, removePlayer, onHighlight, onHighlightEnd, highlighted, teams, adding}) => {

    const classes = mainStyles();
    const styles = playersStyles();
    const ref = useRef(null);


    const [{isDragging}, drag] = useDrag(() => ({
        type: type,
        item: {id: player.id, name: teams ? player.name : player.username},
        collect: (monitor) => {
            return {
                isDragging: monitor.isDragging(),
            };
        },
    }));
    const hasRole = useHasRole();

    if (hasRole('ADMIN') && adding) {
        drag(ref);
    }

    return <div key={player.id}
                ref={ref}
                className={[styles.player, classes.header, highlighted && player.id === highlighted ? classes.twichHighlighted : ''].join(' ')}
                onMouseEnter={() => onHighlight(player.id)} onMouseLeave={onHighlightEnd}>
        {teams ? <PeopleIcon fontSize={"small"}/> : <PersonOutlineIcon fontSize={"small"}/>}
        <BoldTyphography>{teams ? player.name : player.username}</BoldTyphography>
        {adding && <div style={{flex: 1, display: "flex", justifyContent: "flex-end", color: "red"}}>
            <div style={{cursor: "pointer"}} onClick={() => {
                removePlayer(player.id);
            }
            }>
                <ClearIcon fontSize={"small"}/>
            </div>
        </div>}
    </div>
};


const TournamentsCompetitors = ({
                                    players, addPlayer, removePlayer, savePlayers, adding, max, teams,
                                    onHighlight,
                                    onHighlightEnd,
                                    highlighted
                                }) => {
    const theme = useTheme();
    const dispatch = useDispatch();

    const [playersIds, setPlayersIds] = useState();


    useEffect(() => {
        setPlayersIds(players.map(player => player.id));
    }, [players])


    return (
        <div>
            <div style={{padding: 10, display: "flex", borderBottom: '1px solid ' + theme.palette.divider}}>
                <Typography variant={"h5"} style={{
                    marginRight: 10,
                    flex: 1
                }}>{players.length} {teams ? 'Teams' : 'Players'} </Typography>
                {adding && <FetchSelectField label={teams ? "Select Team" : "Select Player"} onChange={(value) => {
                    if (playersIds.length === max) {
                        dispatch(showError("Too many players"))
                        return;
                    }
                    addPlayer(value);
                }}
                                             clearOnChange
                                             searchYourself
                                             url={teams ? "/teams/search" : "/users/search"}
                                             nameParameter={teams ? "name" : "username"}/>}
            </div>
            {players.map(player => <Competitor key={player.id}
                                               removePlayer={removePlayer}
                                               player={player}
                                               onHighlight={onHighlight}
                                               onHighlightEnd={onHighlightEnd}
                                               highlighted={highlighted}
                                               teams={teams}
                                               adding={adding}/>)}
            {adding && <div style={{padding: 10}}>
                <Button onClick={() => {
                    savePlayers(playersIds);
                }}>Save</Button>
            </div>}
        </div>);
};

TournamentsCompetitors.propTypes = {};

const playersStyles = makeStyles(theme => ({
    player: {
        color: "white",
        padding: 10,
        cursor: 'default',
        borderBottom: '1px solid ' + theme.palette.divider
    }
}))

export default TournamentsCompetitors;
