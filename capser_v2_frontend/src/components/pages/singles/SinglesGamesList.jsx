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
import GameComponent from "../../game/GameComponent";


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
                    <div>
                        {!loading ? <div >
                                {games.map(game => <GameComponent key={game.id} game={game} vertical={false}/>)}
                            </div>
                            :
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
