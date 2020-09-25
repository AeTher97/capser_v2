import React, {useEffect, useState} from 'react';
import mainStyles from "../../../misc/styles/MainStyles";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {TableBody} from "@material-ui/core";
import Pagination from "@material-ui/lab/Pagination";
import {usePlayersListFetch} from "../../../data/Players";
import LoadingComponent from "../../../utils/LoadingComponent";
import Tooltip from "@material-ui/core/Tooltip";
import PlayerCard from "../../misc/PlayerCard";
import {getStatsString} from "../../../utils/Utils";

const SinglesPlayersList = ({type}) => {

    const [players, setPlayers] = useState([])
    const [loading, setLoading] = useState(true);
    const [count, setCount] = useState(0)
    const [currentPage, setPage] = useState(1);


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
                                        <TableCell>Last Seen</TableCell>
                                        <TableCell>Last Game</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {players.map(player => {
                                        index++;
                                        const stats = player[getStatsString(type)];
                                        return (
                                            <TableRow key={player.id}>
                                                <TableCell>{(currentPage - 1) * 10 + index}</TableCell>
                                                <TableCell className={classes.link}>
                                                    <Tooltip title={<PlayerCard player={player} type={type}/>} classes={{tooltip: classes.neonTooltip}}>
                                                        <div>
                                                            {player.username}
                                                        </div>
                                                    </Tooltip>
                                                </TableCell>
                                                <TableCell>{stats.points.toFixed(2)}</TableCell>
                                                {player.lastSeen &&
                                                <TableCell>{new Date(player.lastSeen).toDateString()}</TableCell>}
                                                {player.lastGame ?
                                                <TableCell>{new Date(player.lastGame).toDateString()}</TableCell> :
                                                <TableCell>No games played</TableCell>}
                                            </TableRow>)
                                    })
                                    }
                                </TableBody>
                            </Table> :
                            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                                <LoadingComponent/></div>}
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
