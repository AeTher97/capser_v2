import React from 'react';
import PropTypes from 'prop-types';
import Tooltip from "@material-ui/core/Tooltip";
import PlayerCard from "./PlayerCard";
import Grow from "@material-ui/core/Grow";
import {Typography} from "@material-ui/core";
import TableCell from "@material-ui/core/TableCell";
import mainStyles from "../../misc/styles/MainStyles";

const PlayerTooltip = ({playerId}) => {

    const classes = mainStyles();
    return (
        <Tooltip title={<PlayerCard player={player} type={type}/>}
                 classes={{tooltip: classes.neonTooltip}}
                 TransitionComponent={Grow}>
            <div>
                <Typography color={"primary"}>{player.username}</Typography>
            </div>
        </Tooltip>
    );
};

PlayerTooltip.propTypes = {

};

export default PlayerTooltip;
