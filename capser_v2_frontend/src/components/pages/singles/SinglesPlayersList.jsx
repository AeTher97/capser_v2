import React, {useState} from 'react';
import mainStyles from "../../../misc/styles/MainStyles";
import {Typography} from "@material-ui/core";
import {usePlayersListFetch} from "../../../data/Players";
import LoadingComponent from "../../../utils/LoadingComponent";
import Tooltip from "@material-ui/core/Tooltip";
import PlayerCard from "../../misc/PlayerCard";
import {getStatsString} from "../../../utils/Utils";
import Grow from '@material-ui/core/Grow';
import BoldTyphography from "../../misc/BoldTyphography";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import CapserPagination from "../../misc/CapserPagination";
import {makeStyles} from "@material-ui/core/styles";
import {useHistory} from "react-router-dom";


const SinglesPlayersList = ({type, pointsHidden = false}) => {

    const [currentPage, setPage] = useState(1);

    const small = useXtraSmallSize();
    const {loading, players, pageCount} = usePlayersListFetch(type, currentPage - 1);
    const history = useHistory();



    const styles = listStyles({small})();


    let index = 0;

    const classes = mainStyles();
    return (
        <div style={{display: "flex", justifyContent: "center"}}>
            <div style={{maxWidth: 800, flex: 1}}>
                {!loading ? <div className={classes.standardBorder} style={{padding: 0}}>
                        <div className={styles.row}>
                            <div className={classes.header} style={{flex: 0.4}}>
                                <Typography>Player</Typography>
                            </div>
                            <div style={{flex: 0.23}}>
                                {!pointsHidden && <Typography>Points</Typography>}
                            </div>
                            <div className={classes.header} style={{flex: 0.4}}>
                                <Typography style={{minWidth: 75, flex: 0.55}}>Last Seen</Typography>
                                <Typography style={{minWidth: 80, flex: 0.3}}>Last Game</Typography>
                            </div>
                        </div>
                        {players.map(player => {
                            index++;
                            const stats = player[getStatsString(type)];
                            return (
                                <div className={styles.row} key={player.id}>
                                    <div className={classes.header} style={{flex: 0.4}}>
                                        <Typography className={classes.link} onClick={() => {
                                            history.push(`/players/${player.id}`)
                                        }}>
                                            <Tooltip title={<PlayerCard player={player} type={type}/>}
                                                     TransitionComponent={Grow}
                                                     style={{padding: 0, margin: 0}}>
                                                <div>
                                                    <BoldTyphography
                                                        color={"primary"}>{(currentPage - 1) * 10 + index}. {player.username}</BoldTyphography>
                                                </div>
                                            </Tooltip>
                                        </Typography>
                                    </div>

                                    <Typography style={{
                                        marginRight: small ? 0 : 20,
                                        flex: 0.2
                                    }}>{!pointsHidden ? stats.points.toFixed(2) : ''}</Typography>
                                    <div className={classes.header} style={{flex: 0.4}}>
                                        {player.lastSeen ?
                                            <Typography style={{
                                                marginRight: 10,
                                                flex: 1
                                            }}>{new Date(player.lastSeen).toDateString()}</Typography> :
                                            <Typography style={{marginRight: 10, flex: 1}}>Never seen</Typography>}
                                        {player.lastGame ?
                                            <Typography>{new Date(player.lastGame).toDateString()}</Typography> :
                                            <Typography>No games played</Typography>}
                                    </div>
                                </div>)
                        })
                        }
                    </div> :
                    <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                        <LoadingComponent/></div>}
                {!loading && pageCount > 1 &&
                <CapserPagination onNext={() => setPage(currentPage + 1)}
                                  onPrevious={() => setPage(currentPage + -1)}
                                  currentPage={currentPage}
                                  pageCount={pageCount}/>
                }
            </div>
        </div>
    );
};

export const listStyles = props => makeStyles(theme => ({
    row: {
        borderBottom: '1px solid ' + theme.palette.divider,
        padding: 15,
        paddingLeft: 15,
        display: "flex",
        alignItems: props.small ? 'center' : 'flex-start',
        justifyContent: props.small ? 'center' : 'space-around',
        flexDirection: props.small ? "column" : "row",
        flexWrap: 'wrap'
    }
}))

export default SinglesPlayersList;
