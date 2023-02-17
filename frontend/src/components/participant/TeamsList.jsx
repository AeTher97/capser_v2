import React, {useState} from 'react';
import {useAllTeams} from "../../data/TeamsData";
import mainStyles from "../../misc/styles/MainStyles";
import {Typography} from "@material-ui/core";
import CapserPagination from "../game/list/CapserPagination";
import {listStyles} from "./SoloPlayersList";
import {useXtraSmallSize} from "../../utils/SizeQuery";
import TeamTooltip from "../tooltips/TeamTooltip";
import {useHistory} from "react-router-dom";
import BoldTyphography from "../misc/BoldTyphography";
import {Skeleton} from "@material-ui/lab";

const TeamsList = () => {

    const [currentPage, setPage] = useState(1);
    const {teams, loading, pageNumber} = useAllTeams(currentPage - 1);

    let index = 0;
    const small = useXtraSmallSize();
    const styles = listStyles({small})();
    const history = useHistory();


    const classes = mainStyles();

    return (
        <div style={{display: "flex", justifyContent: 'center'}}>
            <div style={{maxWidth: 800, flex: 1}}>
                {!loading && <div className={classes.standardBorder} style={{padding: 0}}>
                    <div className={styles.row}>
                        <Typography style={{flex: 0.4}}>Team</Typography>
                        <Typography style={{flex: 0.3}}>Team Points</Typography>
                        <Typography style={{flex: 0.3}}>Players</Typography>
                    </div>
                    {teams.map(team => {
                        index++;
                        const stats = team.doublesStats;
                        return <div key={team.id} className={styles.row}
                                    style={teams.length === 1 ? {borderWidth: 0} : {}}>
                            <div style={{flex: 0.4}} className={classes.header}>
                                <TeamTooltip teamId={team.id}>
                                    <BoldTyphography color={"primary"} className={classes.twichHighlightPadding}
                                                     style={{flex: 0}} onClick={() => {
                                        history.push(`/teams/${team.id}`)
                                    }}>
                                        {(currentPage - 1) * 10 + index}. {team.name}
                                    </BoldTyphography>
                                </TeamTooltip>
                            </div>
                            <Typography style={{flex: 0.3}}>{stats.points.toFixed(2)}</Typography>
                            <div style={{flex: 0.3}}>
                                <div className={classes.header}>
                                    {team.playerList.map(player => {
                                        return <Typography key={player.id} color={"primary"}
                                                           onClick={() => history.push(`/players/${player.id}`)}
                                                           className={classes.twichHighlightPadding}
                                                           style={{
                                                               marginRight: 10,
                                                               marginLeft: small ? 10 : 0,
                                                               marginTop: 0,
                                                               marginBottom: 0,
                                                               cursor: 'pointer'
                                                           }}>
                                            {player.username}
                                        </Typography>
                                    })}
                                </div>
                            </div>
                        </div>
                    })}
                </div>}
                {loading && Array.from(Array(10)).map((value) => <div key={value}
                    style={{display: 'flex', alignItems: 'center', margin: '5px 0 5px 0'}}>
                    <Skeleton variant={"circle"} style={{width: 60, height: 60}}/><Skeleton variant={"rect"}
                                                                                            style={{
                                                                                                margin: '5px 0 5px 10px',
                                                                                                height: 35,
                                                                                                flex: 1,
                                                                                                borderRadius: 7
                                                                                            }}/></div>)}
                <div className={[classes.paddedContent].join(' ')}>

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
