import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import UserFetchSelectField from "../../utils/UserFetchSelectField";
import {MenuItem, Select, Typography} from "@material-ui/core";

const GameHistoryFilters = ({setOpponent, setGameType, selectedGameType}) => {
    const classes = mainStyles();
    return (
        <div className={[classes.standardBorder, classes.header].join(' ')}
             style={{margin: 0, marginBottom: 10, justifyContent: 'space-between', flexWrap: 'wrap'}}>
            <Select value={selectedGameType} onChange={(event => setGameType(event.target.value))}>
                <MenuItem value={'ALL'}>All game types</MenuItem>
                <MenuItem value={'EASY_CAPS'}>Easy Caps</MenuItem>
                <MenuItem value={'SINGLES'}>Singles</MenuItem>
                <MenuItem value={'UNRANKED'}>Unranked</MenuItem>
            </Select>
            {selectedGameType !== 'ALL' && <UserFetchSelectField label={"Opponent"} onChange={(value) => {
                if (!value) {
                    setOpponent(null);
                    return;
                }
                setOpponent(value.id)
            }}/>}
            {selectedGameType === 'ALL' && <Typography>Select game type to choose an opponent</Typography>}
        </div>
    );
};

export default GameHistoryFilters;
