import React, {useCallback, useState} from 'react';
import {useSelector} from "react-redux";
import mainStyles from "../../misc/styles/MainStyles";
import {Divider, Tab, Tabs, Typography, useTheme} from "@material-ui/core";
import TabPanel from "../../components/misc/TabPanel";
import useQuery from "../../utils/UserQuery";
import {useUserData} from "../../data/UserData";
import LoadingComponent from "../../utils/LoadingComponent";
import {getGameIcon} from "../../components/game/details/GameComponent";
import {getGameTypeString} from "../../utils/Utils";
import Button from "@material-ui/core/Button";
import EditUserDataDialog from "../../components/dialogs/EditUserDataDialog";
import {useHistory, useParams} from "react-router-dom";
import ProfilePicture from "../../components/profile/ProfilePicture";
import {useWindowSize} from "../../utils/UseSize";
import ProfilePlots from "../../components/profile/ProfilePlots";
import {useXtraSmallSize} from "../../utils/SizeQuery";
import GameHistory from "../../components/profile/GameHistory";


const displayStats = (type, stats, showPoints = true) => {
    return (<div>
        <div style={{display: "flex"}}>
            {getGameIcon(type)}
            <Typography style={{fontWeight: "bold", marginLeft: 5}}
                        color={"inherit"}> {getGameTypeString(type)}</Typography>
        </div>

        <div style={{display: "flex", flexWrap: 'wrap'}}>
            <div style={{flex: 1}}>
                <div style={{display: "flex", flexDirection: "column", marginRight: 10}}>
                    <Typography noWrap variant={"caption"}>Average
                        rebuttals: {stats.avgRebuttals.toFixed(2)}</Typography>
                    <Typography noWrap variant={"caption"}>Total rebuttals: {stats.totalRebuttals}</Typography>
                    {showPoints &&
                    <Typography noWrap variant={"caption"}>Points: {stats.points.toFixed(2)}</Typography>}
                    <Typography noWrap variant={"caption"}>Beers downed: {stats.beersDowned}</Typography>
                    <Typography noWrap variant={"caption"}>Games played: {stats.gamesPlayed}</Typography>
                    <Typography noWrap variant={"caption"}>Games won: {stats.gamesWon}</Typography>
                    <Typography noWrap variant={"caption"}>Games lost: {stats.gamesLost}</Typography>
                    <Typography noWrap variant={"caption"}>Win/loss: {stats.winLossRatio.toFixed(2)}</Typography>
                </div>
            </div>
            <div style={{flex: 1}}>
                <div style={{display: "flex", flexDirection: "column"}}>
                    <Typography noWrap variant={"caption"}>Naked laps: {stats.nakedLaps}</Typography>
                    <Typography noWrap variant={"caption"}>Points made to
                        lost: {stats.pointsMadeLostRatio.toFixed(2)}</Typography>
                    <Typography noWrap variant={"caption"}>Points made: {stats.totalPointsMade}</Typography>
                    <Typography noWrap variant={"caption"}>Points lost: {stats.totalPointsLost}</Typography>
                    <Typography noWrap variant={"caption"}>Sinks made to
                        lost: {stats.sinksMadeLostRatio.toFixed(2)}</Typography>
                    <Typography noWrap variant={"caption"}>Sinks made: {stats.totalSinksMade}</Typography>
                    <Typography noWrap variant={"caption"}>Sinks lost: {stats.totalSinksLost}</Typography>
                </div>
            </div>
        </div>
    </div>)
}

const ProfileScreen = () => {

    const {playerId} = useParams();

    const {userId} = useSelector(state => state.auth);
    const [plotWidth, setPlotWidth] = useState(0);
    const [tick, setTick] = useState(1);
    const size = useWindowSize();
    const small = useXtraSmallSize();

    const measuredRef = useCallback(node => {
        if (node) {
            setPlotWidth(node.getBoundingClientRect().width);
        }
    }, [size, tick]);


    const history = useHistory();
    const {data, loading, loaded, updateUserData} = useUserData(playerId ? playerId : userId);

    const query = useQuery();
    const tab = query.get('tab') || 'stats';

    const [dialogOpen, setDialogOpen] = useState(false);


    const classes = mainStyles();
    const theme = useTheme();

    const handleTabChange = (e, value) => {
        history.push(`?tab=${value}`);
        setTick(tick + 1)
    }

    if (data) {
        console.log(data.teams)
    }

    return (
        <div>
            <div style={{
                height: 94,
                width: '100%',
                borderBottom: '1px solid ' + theme.palette.divider,

            }}>
            </div>
            <div
                style={{
                    display: "flex",
                    color: 'white',
                    flexWrap: 'wrap',
                    maxWidth: 1200,
                    margin: 'auto',
                    zIndex: 100,
                    position: 'relative',
                    top: -60
                }}>
                <div style={{flex: 1, padding: 20, display: "flex", alignItems: 'center', flexDirection: 'column'}}>
                    {data && <ProfilePicture changePictureOverlayEnabled={(!playerId || playerId === userId)}
                                             avatarHash={data.avatarHash}/>}
                    {!loading && loaded &&
                    <Typography variant={"h6"} style={{marginTop: 10}}>{data.username}</Typography>}
                    {!loading && loaded &&
                    <div style={{display: "flex", alignItems: 'center', flexDirection: 'column', marginTop: 10}}>
                        <Typography style={{fontWeight: 'bold'}}>Last seen</Typography>
                        <Typography>{data.lastSeen ? new Date(data.lastSeen).toDateString() : 'Never seen'}</Typography>
                        <Typography style={{fontWeight: 'bold'}}>Last game</Typography>
                        <Typography>{data.lastGame ? new Date(data.lastGame).toDateString() : 'Never played'}</Typography>
                    </div>}
                    {(!playerId || playerId === userId) &&
                    <Button style={{marginTop: 20}} variant={"text"} onClick={() => setDialogOpen(true)}>Edit
                        profile</Button>}
                </div>
                <div style={{flex: 5, padding: 20}}>
                    <Tabs value={tab} onChange={handleTabChange} centered={small}>
                        <Tab label={"Overview"} value={"stats"} onChange={() => {
                        }}/>
                        <Tab label={"Charts"} value={"charts"} onChange={() => {
                        }}/>
                        <Tab label={"Game history"} value={"history"}/>
                    </Tabs>

                    {!loading && loaded && <>
                        <TabPanel value={tab} showValue={'stats'}>
                            <div className={classes.paddedContent} style={{paddingLeft: 0}}>
                                <Typography variant={"h6"}>Game stats</Typography>
                                <div style={{display: "flex", flexWrap: "wrap"}}>
                                    <div style={{display: "flex", flexDirection: "column", flex: 1}}>
                                        <div className={classes.standardBorder}
                                             style={{flex: 1}}>{displayStats("SINGLES", data.userSinglesStats)}</div>
                                        <div className={classes.standardBorder}
                                             style={{flex: 1}}>{displayStats("DOUBLES", data.userDoublesStats)}</div>
                                    </div>
                                    <div style={{display: "flex", flexDirection: "column", flex: 1}}>
                                        <div className={classes.standardBorder}
                                             style={{flex: 1}}>{displayStats("EASY_CAPS", data.userEasyStats)}</div>
                                        <div className={classes.standardBorder}
                                             style={{flex: 1}}>{displayStats("UNRANKED", data.userUnrankedStats, false)}</div>
                                    </div>
                                </div>
                            </div>
                            <Divider/>
                            <div>
                                <div className={classes.paddedContent} style={{paddingLeft: 0}}>
                                    <Typography variant={"h6"}>Teams</Typography>
                                </div>
                                {data.teams.filter(team => team.active).map(team => {
                                    return <div key={team.id} style={{color: 'red'}}>
                                        <div className={classes.paddedContent}>
                                            <Typography color={"inherit"}>{team.name}</Typography>
                                        </div>
                                        <Divider/>
                                    </div>
                                })}
                                {data.teams.length === 0 &&
                                    <div className={classes.header} style={{justifyContent: 'center'}}>
                                        <Typography variant={"h6"}>No teams</Typography>
                                </div>}
                            </div>
                        </TabPanel>
                        <TabPanel value={tab} showValue={'charts'}>
                            <div className={classes.paddedContent} style={{paddingLeft: 0}}>
                                <div ref={measuredRef}>

                                    <ProfilePlots userId={playerId ? playerId : userId} width={plotWidth}/>
                                </div>
                            </div>
                        </TabPanel>
                        <TabPanel value={tab} showValue={'history'}>
                            <div className={classes.paddedContent} style={{paddingLeft: 0}}>
                                <div ref={measuredRef}>
                                    <GameHistory userId={playerId ? playerId : userId}/>
                                </div>
                            </div>
                        </TabPanel>
                    </>}


                </div>

                {(!playerId || playerId === userId) && !loading && loaded &&
                <EditUserDataDialog open={dialogOpen} setOpen={setDialogOpen} data={data} editData={updateUserData}/>}


            </div>
            {loading && <LoadingComponent/>}
        </div>
    );
};

export default ProfileScreen;
