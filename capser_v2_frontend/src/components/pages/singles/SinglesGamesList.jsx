import React, {useState} from 'react';
import {useSinglesGames} from "../../../data/SoloGamesData";
import mainStyles from "../../../misc/styles/MainStyles";
import {TableBody} from "@material-ui/core";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {getGameModeString, getRequestGameTypeString} from "../../../utils/Utils";
import Pagination from '@material-ui/lab/Pagination';
import LoadingComponent from "../../../utils/LoadingComponent";
import {makeStyles} from "@material-ui/core/styles";
import {useHistory} from "react-router-dom";


const SinglesGamesList = ({type, hiddenPoints = false}) => {

    const [currentPage, setPage] = useState(1);
    const history = useHistory();

    const {games, loading, pagesNumber} = useSinglesGames(type, currentPage - 1, 10);


    const findPlayerStats = (game, id) => {
        return game.gamePlayerStats.find(o => o.playerId === id)
    }

    const handlePageChange = (e, value) => {
        setPage(value);
    }


    const classes = mainStyles();
    const styles = useStyles();
    return (
        <div>

            <div className={classes.root}>
                <div className={classes.leftOrientedWrapperNoPadding}>
                    <div className={[classes.paddedContent, classes.squareShine].join(' ')}>
                        {!loading ? <Table style={{width: '100%'}}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Players</TableCell>
                                        <TableCell>Winner</TableCell>
                                        <TableCell>Game Mode</TableCell>
                                        <TableCell>Time</TableCell>
                                        <TableCell>Score</TableCell>
                                        <TableCell>Rebuttals</TableCell>
                                        <TableCell>Sinks</TableCell>
                                        <TableCell>Beers</TableCell>
                                        {!hiddenPoints && <TableCell>Points change</TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {games.map(game => {
                                        const player1Stats = findPlayerStats(game, game.player1);
                                        const player2Stats = findPlayerStats(game, game.player2);
                                        return (
                                            <TableRow key={game.id} className={styles.row} onClick={() => {
                                                history.push(`/${getRequestGameTypeString(type)}/${game.id}`)
                                            }}>
                                                <TableCell
                                                    style={{color: 'red'}}>{game.player1Name} vs {game.player2Name}</TableCell>
                                                <TableCell>{game.winner === game.player1 ? game.player1Name : game.player2Name}</TableCell>
                                                <TableCell>{getGameModeString(game.gameMode)}</TableCell>
                                                <TableCell>{new Date(game.time).toUTCString()}</TableCell>
                                                <TableCell>{player1Stats.score} : {player2Stats.score}</TableCell>
                                                <TableCell>{player1Stats.rebuttals} : {player2Stats.rebuttals}</TableCell>
                                                <TableCell>{player1Stats.sinks} : {player2Stats.sinks}</TableCell>
                                                <TableCell>{player1Stats.beersDowned} : {player2Stats.beersDowned}</TableCell>
                                                {!hiddenPoints &&
                                                <TableCell>{player1Stats.pointsChange.toFixed(2)} : {player2Stats.pointsChange.toFixed(2)}</TableCell>}
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
        </div>
    );
};

const useStyles = makeStyles(theme => ({
    row: {
        cursor: "pointer",
        '&:hover': {
            backgroundColor: 'rgba(255,255,255,0.05)'
        }
    }
}));

export default SinglesGamesList;
