import React, {useState} from 'react';
import PropTypes from 'prop-types';
import {useMultipleGames} from "../../../data/MultipleGamesData";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {TableBody} from "@material-ui/core";
import {getGameModeString} from "../../../utils/Utils";
import LoadingComponent from "../../../utils/LoadingComponent";
import Pagination from "@material-ui/lab/Pagination";
import mainStyles from "../../../misc/styles/MainStyles";

const MultipleGameList = ({hiddenPoints, type}) => {

    const classes = mainStyles();

    const [currentPage, setPage] = useState(1);

    const {games, loading, pagesNumber} = useMultipleGames(type, currentPage - 1, 10);

    const handlePageChange = (e, value) => {
        setPage(value);
    }


    return (
            <div className={classes.root}>
                <div className={classes.leftOrientedWrapperNoPadding}>
                    <div className={[classes.paddedContent, classes.squareShine].join(' ')}>
                        {!loading ? <Table style={{width: '100%'}}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Teams</TableCell>
                                        <TableCell>Winner</TableCell>
                                        <TableCell>Game Mode</TableCell>
                                        <TableCell>Time</TableCell>
                                        <TableCell>Score</TableCell>
                                        {!hiddenPoints && <TableCell>Points change</TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {games.map(game => {
                                        const winner = game.winnerTeamId === game.team1DatabaseId ? game.team1Name.name : game.team2Name.name;
                                        const team1PointsChange = game.gamePlayerStats.find(obj => obj.playerId === game.team1.playerList[0]).pointsChange;
                                        const team2PointsChange = game.gamePlayerStats.find(obj => obj.playerId === game.team2.playerList[0]).pointsChange;
                                        return (
                                            <TableRow key={game.id}>
                                                <TableCell
                                                    style={{color: 'red'}}>{game.team1Name.name} vs {game.team2Name.name}</TableCell>
                                                <TableCell>{winner}</TableCell>
                                                <TableCell>{getGameModeString(game.gameMode)}</TableCell>
                                                <TableCell>{new Date(game.time).toUTCString()}</TableCell>
                                                <TableCell>{game.team1Score} : {game.team2Score}</TableCell>
                                                {!hiddenPoints &&
                                                <TableCell>{team1PointsChange.toFixed(2)} : {team2PointsChange.toFixed(2)}</TableCell>}
                                            </TableRow>)
                                    })
                                    }
                                </TableBody>
                            </Table> :
                            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                                <LoadingComponent/>
                            </div>}
                        {!loading && pagesNumber > 1 &&
                        <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                            <Pagination count={pagesNumber} onChange={handlePageChange} page={currentPage}/>
                        </div>}
                    </div>
                </div>
            </div>
    );
};

MultipleGameList.propTypes = {};

export default MultipleGameList;
