import React, {useEffect, useState} from 'react';
import {useGameListFetch} from "../../../data/Game";
import {useUsernameFetch} from "../../../data/UsersFetch";
import mainStyles from "../../../misc/styles/MainStyles";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {TableBody} from "@material-ui/core";
import {getGameModeString} from "../../../utils/Utils";
import CircularProgress from "@material-ui/core/CircularProgress";
import Pagination from "@material-ui/lab/Pagination";
import {usePlayersListFetch} from "../../../data/Players";

const SinglesPlayersList = ({type}) => {

    const [players, setPlayers] = useState([])
    const [loading, setLoading] = useState(true);
    const [count, setCount] = useState(0)
    const [currentPage, setPage] = useState(1);

    const statsString = () => {
        switch (type) {
            case 'SINGLES' :
                return 'userSinglesStats'
            case  'EASY_CAPS' :
                return 'userEasyStats'
            case 'UNRANKED' :
                return 'userUnrankedStats'
            case 'DOUBLES' :
                return 'userDoublesStats'
        }

    }
    const fetchPlayers = usePlayersListFetch(type);


    useEffect(() => {
        setLoading(true)
        fetchPlayers(currentPage - 1).then((response) => {
            setLoading(false)
            setCount(response.data.totalPages)
            setPlayers(response.data.content)
        })
    }, [currentPage])


    const handlePageChange = (e, value) => {
        setPage(value);
    }


    let index = 0;

    const classes = mainStyles();
    return (
        <div>

            <div className={classes.root}>
                <div className={classes.leftOrientedWrapperNoPadding}>
                    <div className={[classes.paddedContent, classes.squareShine].join(' ')}>
                        {!loading ? <Table style={{width: '100%'}}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Placement</TableCell>
                                        <TableCell>Name</TableCell>
                                        <TableCell>Points</TableCell>
                                        <TableCell>Last Game</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {players.map(player => {
                                        index++;
                                        const stats = player[statsString()];
                                        return (
                                            <TableRow key={player.id}>
                                                <TableCell>{(currentPage - 1) * 10 + index}</TableCell>
                                                <TableCell>{player.username}</TableCell>
                                                <TableCell>{stats.points.toFixed(2)}</TableCell>
                                            </TableRow>)
                                    })
                                    }
                                </TableBody>
                            </Table> :
                            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                                <CircularProgress/>
                            </div>}
                        {!loading && count > 1 &&
                        <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                            <Pagination count={count} onChange={handlePageChange} page={currentPage}/>
                        </div>}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SinglesPlayersList;