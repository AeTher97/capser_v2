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
import CapserPagination from "../../misc/CapserPagination";
import {listStyles} from "../singles/SinglesPlayersList";
import {useXtraSmallSize} from "../../../utils/SizeQuery";

const TeamsList = props => {

    const [currentPage, setPage] = useState(1);
    const {teams, loading, pageNumber} = useAllTeams(currentPage - 1);
    const handlePageChange = (e, value) => {
        setPage(value);
    }
    let index = 0;
    const small = useXtraSmallSize();
    const styles = listStyles({small})();


    const classes = mainStyles();

    return (
        <div style={{display: "flex", justifyContent: 'center'}}>
            <div style={{maxWidth: 800, flex: 1}}>
                {!loading && <div className={classes.standardBorder} style={{padding: 0}}>
                    <div className={styles.row}>
                        <Typography style={{flex: 0.2}}>Team</Typography>
                        <Typography style={{flex: 0.5}}>Team Points</Typography>
                        <Typography style={{flex: 0.3}}>Players</Typography>
                    </div>
                    {teams.map(team => {
                        index++;
                        const stats = team.doublesStats;
                        return <div key={team.id} className={styles.row}>
                            <Typography color={"primary"} className={classes.link} style={{flex: 0.2}}>
                                {(currentPage - 1) * 10 + index}. {team.name}
                            </Typography>
                            <Typography style={{flex: 0.5}}>{stats.points.toFixed(2)}</Typography>
                            <div style={{flex: 0.3}}>
                                <div className={classes.header}>
                                    {team.playerList.map(player => {
                                        return <Typography key={player.id} color={"primary"}
                                                           className={classes.link} style={{marginRight: 10, marginLeft: small ? 10 : 0}}>
                                            {player.username}
                                        </Typography>
                                    })}
                                </div>
                            </div>
                        </div>
                    })}
                </div>}
                <div className={[classes.paddedContent].join(' ')}>
                    {!loading ? <Table style={{width: '100%'}}>

                        <TableBody>
                            {teams.map(team => {

                                return (
                                    <TableRow key={team.id}>


                                    </TableRow>)
                            })
                            }
                        </TableBody>
                    </Table> : <LoadingComponent/>}
                    {!loading && pageNumber > 1 &&
                    <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                        <CapserPagination onNext={() => setPage(currentPage + 1)}
                                          onPrevious={() => setPage(currentPage + -1)}
                                          currentPage={currentPage}
                                          pageCount={pageNumber}/>
                    </div>}
                </div>
            </div>
        </div>
    );
};

TeamsList.propTypes = {};

export default TeamsList;
