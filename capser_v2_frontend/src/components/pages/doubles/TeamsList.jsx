import React, {useState} from 'react';
import {useAllTeams} from "../../../data/TeamsData";
import LoadingComponent from "../../../utils/LoadingComponent";
import mainStyles from "../../../misc/styles/MainStyles";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {TableBody, Typography} from "@material-ui/core";
import {getGameModeString, getStatsString} from "../../../utils/Utils";
import Table from "@material-ui/core/Table";
import Pagination from "@material-ui/lab/Pagination";

const TeamsList = props => {

    const [currentPage, setPage] = useState(1);
    const {teams, loading, pageNumber} = useAllTeams(currentPage -1);
    const handlePageChange = (e, value) => {
        setPage(value);
    }
    let index = 0;


    const classes = mainStyles();

    return (
        <div className={classes.root}>
            <div className={classes.leftOrientedWrapperNoPadding}>
                <div className={[classes.paddedContent, classes.squareShine].join(' ')}>
                    {!loading ? <Table style={{width: '100%'}}>
                        <TableHead>
                            <TableRow>
                                <TableCell>Placement</TableCell>
                                <TableCell>Team Name</TableCell>
                                <TableCell>Team Points</TableCell>
                                <TableCell>Players</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {teams.map(team => {
                                index++;
                                const stats = team.doublesStats;
                                return (
                                    <TableRow key={team.id}>
                                        <TableCell>{(currentPage - 1) * 10 + index}</TableCell>
                                        <TableCell>
                                            <Typography color={"primary"} className={classes.link}>
                                                {team.name}
                                            </Typography>
                                        </TableCell>
                                        <TableCell>{stats.points.toFixed(2)}</TableCell>
                                        <TableCell>
                                            {team.playerList.map(player => {
                                                return <Typography key={player.id} color={"primary"}
                                                                     className={classes.link}>
                                                    {player.username}
                                                </Typography>
                                            })}
                                        </TableCell>

                                    </TableRow>)
                            })
                            }
                        </TableBody>
                    </Table> : <LoadingComponent/>}
                    {!loading && pageNumber > 1 &&
                    <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                        <Pagination count={pageNumber} onChange={handlePageChange} page={currentPage}/>
                    </div>}
                </div>
            </div>
        </div>
    );
};

TeamsList.propTypes = {};

export default TeamsList;
