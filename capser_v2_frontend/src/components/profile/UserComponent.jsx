import React, {useState} from 'react';
import {useSelector} from "react-redux";
import mainStyles from "../../misc/styles/MainStyles";
import {Dialog, Divider, Link, Tab, Tabs, Typography, useTheme} from "@material-ui/core";
import TabPanel from "../misc/TabPanel";
import useQuery from "../../utils/UserQuery";
import {useUserData} from "../../data/UserData";
import LoadingComponent from "../../utils/LoadingComponent";
import {getGameIcon} from "../game/GameComponent";
import {getGameTypeString} from "../../utils/Utils";
import Button from "@material-ui/core/Button";
import EditUserDataDialog from "./EditUserDataDialog";
import EditIcon from '@material-ui/icons/Edit';
import {useHistory} from "react-router-dom";
import {useXtraSmallSize} from "../../utils/SizeQuery";

const getClipPath = () => {
    return (<svg width="0" height="0">
            <defs>
                <clipPath id="clip" transform="translate(-20,-20) scale(1.05,1.05)">
                    <path
                        d="M42.52,44.08c-3.23-.33-4.08.63-4.05,3.82a7,7,0,0,1-.78,3.81c-2.77,4.1-4.5,9.18-9.9,10.95-1.33.44-1.68,1.47-1.24,2.92s1.07,3.15.74,4.53C26.12,75,25.46,80.26,21.12,83.8c-1.23,1-1.19,2.19-.29,3.51.7,1,1.68,2.21,1.67,3.32-.06,5.19,1.56,10.59-1.58,15.49-1,1.5-.62,2.84.88,3.89a8.71,8.71,0,0,1,3.41,5.44,27.08,27.08,0,0,0,1.31,4,12.48,12.48,0,0,1,.69,8.12c-.59,2.16-.05,2.81,2,3.69a8,8,0,0,1,3.32,2c3.23,4.22,7,8.21,7.77,13.81a2.42,2.42,0,0,0,2.2,2c1.29.29,2.78.32,3.8,1,4.42,3.06,9.35,5.66,11.41,11.15.51,1.37,1.57,1.59,2.92,1.48,1.55-.13,3.28-.5,4.64,0,4.91,1.8,10.33,2.66,14,7a2.32,2.32,0,0,0,2.16.56c3.14-2,6.59-1.64,10-1.55s6.73.17,9.67,2.28c1.92,1.38,2.64,1.29,4.11-.52a7.87,7.87,0,0,1,4.85-2.81,30.85,30.85,0,0,0,3.67-.94c3.23-1,6.41-1.83,9.71-.59a2.18,2.18,0,0,0,2.94-1.09c1.64-3.78,5.13-5.43,8.27-7.5,2.32-1.53,4.65-2.92,7.63-2.84s3.21-.41,3.66-3.41a11.34,11.34,0,0,1,1.62-4,37.45,37.45,0,0,1,2.85-3.49,15.34,15.34,0,0,1,7.07-5.37c2.61-.89,2.66-1.35,2.41-4.21a12,12,0,0,1,.34-4.4c.55-1.89,1.57-3.64,2.31-5.48a12.76,12.76,0,0,1,5-6.23c1.94-1.25,2.06-2.19,1-4.26-.5-1-1.27-2.11-1.2-3.13.33-5.1,0-10.38,3.58-14.75,1.55-1.88,1.37-2.58-.56-4.24a8.22,8.22,0,0,1-2.68-4.55c-.95-4.53-2.19-9-.11-13.6.74-1.64,0-2.67-1.53-3.48-1.3-.69-2.91-1.44-3.56-2.61-2.46-4.47-5.8-8.71-4.84-14.36.24-1.46-.55-2.17-2-2.49-1.57-.36-3.54-.44-4.58-1.44-3.53-3.33-7.75-6.22-8.67-11.59-.41-2.42-.83-2.57-3.31-2.59-1.5,0-3.23.42-4.45-.18-4.23-2.09-9.05-3.42-11.42-8.25-1.2-2.44-1.61-2.28-4.37-1.49a17.44,17.44,0,0,1-5.27.69,43.76,43.76,0,0,1-5.86-1,9.6,9.6,0,0,1-6.18-3.71c-1.41-1.81-2.22-1.73-4-.24-1.15.94-2.36,2.13-3.72,2.41-5,1.06-10,2.24-14.84-.88-1.42-.92-2.48-.31-3.47,1.06a12.32,12.32,0,0,1-3.71,3.82c-4.1,2.21-8.29,4.52-13.3,3.21-1.75-.46-2.59.37-3.21,2a13.82,13.82,0,0,1-2.62,4.84,56.84,56.84,0,0,1-7.18,5.72A10.65,10.65,0,0,1,42.52,44.08Z"/>

                </clipPath>
                <clipPath id="clip2" transform="translate(-15,-20) scale(1.1,1.1)">
                    <path
                        d="M42.52,44.08c-3.23-.33-4.08.63-4.05,3.82a7,7,0,0,1-.78,3.81c-2.77,4.1-4.5,9.18-9.9,10.95-1.33.44-1.68,1.47-1.24,2.92s1.07,3.15.74,4.53C26.12,75,25.46,80.26,21.12,83.8c-1.23,1-1.19,2.19-.29,3.51.7,1,1.68,2.21,1.67,3.32-.06,5.19,1.56,10.59-1.58,15.49-1,1.5-.62,2.84.88,3.89a8.71,8.71,0,0,1,3.41,5.44,27.08,27.08,0,0,0,1.31,4,12.48,12.48,0,0,1,.69,8.12c-.59,2.16-.05,2.81,2,3.69a8,8,0,0,1,3.32,2c3.23,4.22,7,8.21,7.77,13.81a2.42,2.42,0,0,0,2.2,2c1.29.29,2.78.32,3.8,1,4.42,3.06,9.35,5.66,11.41,11.15.51,1.37,1.57,1.59,2.92,1.48,1.55-.13,3.28-.5,4.64,0,4.91,1.8,10.33,2.66,14,7a2.32,2.32,0,0,0,2.16.56c3.14-2,6.59-1.64,10-1.55s6.73.17,9.67,2.28c1.92,1.38,2.64,1.29,4.11-.52a7.87,7.87,0,0,1,4.85-2.81,30.85,30.85,0,0,0,3.67-.94c3.23-1,6.41-1.83,9.71-.59a2.18,2.18,0,0,0,2.94-1.09c1.64-3.78,5.13-5.43,8.27-7.5,2.32-1.53,4.65-2.92,7.63-2.84s3.21-.41,3.66-3.41a11.34,11.34,0,0,1,1.62-4,37.45,37.45,0,0,1,2.85-3.49,15.34,15.34,0,0,1,7.07-5.37c2.61-.89,2.66-1.35,2.41-4.21a12,12,0,0,1,.34-4.4c.55-1.89,1.57-3.64,2.31-5.48a12.76,12.76,0,0,1,5-6.23c1.94-1.25,2.06-2.19,1-4.26-.5-1-1.27-2.11-1.2-3.13.33-5.1,0-10.38,3.58-14.75,1.55-1.88,1.37-2.58-.56-4.24a8.22,8.22,0,0,1-2.68-4.55c-.95-4.53-2.19-9-.11-13.6.74-1.64,0-2.67-1.53-3.48-1.3-.69-2.91-1.44-3.56-2.61-2.46-4.47-5.8-8.71-4.84-14.36.24-1.46-.55-2.17-2-2.49-1.57-.36-3.54-.44-4.58-1.44-3.53-3.33-7.75-6.22-8.67-11.59-.41-2.42-.83-2.57-3.31-2.59-1.5,0-3.23.42-4.45-.18-4.23-2.09-9.05-3.42-11.42-8.25-1.2-2.44-1.61-2.28-4.37-1.49a17.44,17.44,0,0,1-5.27.69,43.76,43.76,0,0,1-5.86-1,9.6,9.6,0,0,1-6.18-3.71c-1.41-1.81-2.22-1.73-4-.24-1.15.94-2.36,2.13-3.72,2.41-5,1.06-10,2.24-14.84-.88-1.42-.92-2.48-.31-3.47,1.06a12.32,12.32,0,0,1-3.71,3.82c-4.1,2.21-8.29,4.52-13.3,3.21-1.75-.46-2.59.37-3.21,2a13.82,13.82,0,0,1-2.62,4.84,56.84,56.84,0,0,1-7.18,5.72A10.65,10.65,0,0,1,42.52,44.08Z"/>

                </clipPath>
            </defs>

        </svg>
    )
}

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
                    <Typography noWrap variant={"caption"}>Win/loss: {stats.winLossRatio}</Typography>
                </div>
            </div>
            <div style={{flex: 1}}>
                <div style={{display: "flex", flexDirection: "column"}}>
                    <Typography noWrap variant={"caption"}>Naked laps: {stats.nakedLaps}</Typography>
                    <Typography noWrap variant={"caption"}>Points made to
                        lost: {stats.pointsMadeLostRatio.toFixed(2)}</Typography>
                    <Typography noWrap variant={"caption"}>Points made: {stats.totalPointsMade}</Typography>
                    <Typography noWrap variant={"caption"}>Points lost: {stats.totalPointsLost}</Typography>
                    <Typography noWrap variant={"caption"}>Sinks made to lost: {stats.sinksMadeLostRatio}</Typography>
                    <Typography noWrap variant={"caption"}>Sinks made: {stats.totalSinksMade}</Typography>
                    <Typography noWrap variant={"caption"}>Sinks lost: {stats.totalSinksLost}</Typography>
                </div>
            </div>
        </div>
    </div>)
}

const UserComponent = ({id}) => {

    const {userId} = useSelector(state => state.auth);
    const history = useHistory();
    const {data, loading, loaded, updateUserData} = useUserData(id ? id : userId);

    const query = useQuery();
    const tab = query.get('tab') || 'stats';

    const [dialogOpen, setDialogOpen] = useState(false);
    const [profilePictureInfoOpen, setProfilePictureInfoOpen] = useState(false);
    const [pictureOverlay, setPictureOverlay] = useState(false);
    const small = useXtraSmallSize();

    const classes = mainStyles();
    const theme = useTheme();

    const handleTabChange = (e, value) => {
        history.push(`?tab=${value}`);
    }

    return (
        <div>
            {getClipPath()}
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
                    <div style={{
                        backgroundColor: 'white',
                        clipPath: 'url(#clip2)',
                        padding: 5,
                        height: 160,
                        width: 170,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}>
                        {data && <img style={{clipPath: 'url(#clip)'}}
                                      src={`https://www.gravatar.com/avatar/${data.avatarHash}?s=160&d=${encodeURIComponent('https://globalcapsleague.com/defaultProfile.png')}`}/>}
                        {(!id || id === userId) &&
                        <EditIcon fontSize={"large"} style={{
                            position: 'relative', top: -95, left: 0, display: pictureOverlay || small ? 'block' : 'none'
                        }}
                        />}
                        {(!id || id === userId) && <div onMouseEnter={() => {
                            setPictureOverlay(true)
                        }} onMouseLeave={() => {
                            setPictureOverlay(false)
                        }} onClick={() => setProfilePictureInfoOpen(true)}
                                                        style={{
                                                            minHeight: 180,
                                                            width: 170,
                                                            position: 'relative',
                                                            top: -200,
                                                            left: 1,
                                                            backgroundColor: pictureOverlay ? "rgba(0,0,0,0.3)" : 'transparent',
                                                            cursor: 'pointer'
                                                        }}/>}
                    </div>
                    {!loading && loaded &&
                    <Typography variant={"h6"} style={{marginTop: 10}}>{data.username}</Typography>}
                    {!loading && loaded &&
                    <div style={{display: "flex", alignItems: 'center', flexDirection: 'column', marginTop: 10}}>
                        <Typography style={{fontWeight: 'bold'}}>Last seen</Typography>
                        <Typography>{data.lastSeen ? new Date(data.lastSeen).toDateString() : 'Never seen'}</Typography>
                        <Typography style={{fontWeight: 'bold'}}>Last game</Typography>
                        <Typography>{data.lastGame ? new Date(data.lastGame).toDateString() : 'Never played'}</Typography>
                    </div>}
                    {(!id || id === userId) &&
                    <Button style={{marginTop: 20}} variant={"text"} onClick={() => setDialogOpen(true)}>Edit
                        profile</Button>}
                </div>
                <div style={{flex: 5, padding: 20}}>
                    <Tabs value={tab} onChange={handleTabChange}>
                        <Tab label={"Overview"} value={"stats"} onChange={() => {
                        }}/>
                        {/*<Tab label={"Game history"} value={"history"}/>*/}
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
                                {data.teams.map(team => {
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
                    </>}

                    {loading && <LoadingComponent/>}
                </div>

                {(!id || id === userId) && !loading && loaded &&
                <EditUserDataDialog open={dialogOpen} setOpen={setDialogOpen} data={data} editData={updateUserData}/>}

                <Dialog open={profilePictureInfoOpen}>
                    <div className={classes.standardBorder} style={{margin: 0}}>
                        <Typography variant={"h5"}>How to change profile picture?</Typography>
                        <Typography>Profile pictures used by Global Caps League are global avatars attached to email
                            addresses. To change profile picture visit <Link
                                onClick={() => window.open('https://en.gravatar.com/')}>Gravatar</Link> and create an
                            avatar attached to your email.</Typography>
                        <Button onClick={() => setProfilePictureInfoOpen(false)}>Close</Button>
                    </div>
                </Dialog>
            </div>
        </div>
    );
};

export default UserComponent;
