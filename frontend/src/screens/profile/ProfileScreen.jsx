import React, {useCallback, useState} from 'react';
import {useSelector} from "react-redux";
import mainStyles from "../../misc/styles/MainStyles";
import {Divider, Tab, Tabs, Typography, useTheme} from "@material-ui/core";
import TabPanel from "../../components/misc/TabPanel";
import useQuery from "../../utils/UserQuery";
import {useUserData} from "../../data/UserData";
import LoadingComponent from "../../utils/LoadingComponent";
import {displayStats} from "../../utils/Utils";
import Button from "@material-ui/core/Button";
import EditUserDataDialog from "../../components/dialogs/EditUserDataDialog";
import {useHistory, useParams} from "react-router-dom";
import ProfilePicture from "../../components/profile/ProfilePicture";
import {useWindowSize} from "../../utils/UseSize";
import ProfilePlots from "../../components/profile/ProfilePlots";
import GameHistory from "../../components/profile/GameHistory";
import AchievementsTab from "../../components/profile/AchievementsTab";
import EditPasswordDialog from "../../components/dialogs/EditPasswordDialog";
import TeamTooltip from "../../components/tooltips/TeamTooltip";


const ProfileScreen = () => {

    const {playerId} = useParams();

    const {userId} = useSelector(state => state.auth);
    const [plotWidth, setPlotWidth] = useState(0);
    const [tick, setTick] = useState(1);
    const size = useWindowSize();

    const measuredRef = useCallback(node => {
        if (node) {
            setPlotWidth(node.getBoundingClientRect().width);
        }
    }, [size, tick]);


    const history = useHistory();
    const {data, loading, loaded, updateUserData, changePassword} = useUserData(playerId ? playerId : userId);


    const query = useQuery();
    const tab = query.get('tab') || 'stats';

    const [dialogOpen, setDialogOpen] = useState(false);
    const [changePasswordOpen, setChangePasswordOpen] = useState(false);


    const classes = mainStyles();
    const theme = useTheme();

    const handleTabChange = (e, value) => {
        history.push(`?tab=${value}`);
        setTick(tick + 1)
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
                    {!loading && data && loaded &&
                        <Typography variant={"h6"} style={{marginTop: 10}}>{data.username}</Typography>}
                    {!loading && data && loaded &&
                        <div style={{display: "flex", alignItems: 'center', flexDirection: 'column', marginTop: 10}}>
                            <Typography style={{fontWeight: 'bold'}}>Last seen</Typography>
                            <Typography>{data.lastSeen ? new Date(data.lastSeen).toDateString() : 'Never seen'}</Typography>
                            <Typography style={{fontWeight: 'bold'}}>Last game</Typography>
                            <Typography>{data.lastGame ? new Date(data.lastGame).toDateString() : 'Never played'}</Typography>
                        </div>}
                    {(!playerId || playerId === userId) &&
                        <><Button style={{marginTop: 20}} variant={"text"} onClick={() => setDialogOpen(true)}>Edit
                            profile</Button>
                            <Button style={{marginTop: 20}} variant={"text"} onClick={() => setChangePasswordOpen(true)}>Change password</Button>
                        </>}
                </div>
                <div style={{flex: 5, paddingTop: 20, maxWidth: '100%'}}>
                    <Tabs value={tab} onChange={handleTabChange} variant={"scrollable"}>
                        <Tab label={"Overview"} value={"stats"}/>
                        <Tab label={"Charts"} value={"charts"}/>
                        <Tab label={"Game history"} value={"history"}/>
                        <Tab label={"Achievements"} value={"achievements"}/>
                    </Tabs>

                    {!loading && loaded && <>
                        <TabPanel value={tab} showValue={'stats'}>
                            <div className={classes.paddedContent}>
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
                                <div className={classes.paddedContent}>
                                    <Typography variant={"h6"}>Teams</Typography>
                                </div>
                                {data.teams.filter(team => team.active).map(team => {
                                    return <div key={team.id} style={{color: 'red'}}>
                                        <div className={classes.paddedContent} style={{display: "flex"}}>
                                            <TeamTooltip teamId={team.id}>
                                                <Typography color={"inherit"} style={{flex: 0}} onClick={
                                                    () => history.push(`/teams/${team.id}`)}>{team.name}</Typography>
                                            </TeamTooltip>
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
                            <div className={classes.paddedContent} >
                                <div ref={measuredRef}>
                                    <ProfilePlots userId={playerId ? playerId : userId} width={plotWidth}/>
                                </div>
                            </div>
                        </TabPanel>
                        <TabPanel value={tab} showValue={'history'}>
                            <div className={classes.paddedContent} >
                                <GameHistory userId={playerId ? playerId : userId}/>
                            </div>
                        </TabPanel>
                        <TabPanel value={tab} showValue={'achievements'}>
                            <div className={classes.paddedContent}>
                                <AchievementsTab achievementsList={data.achievements}/>
                            </div>
                        </TabPanel>
                    </>}
                </div>
                {(!playerId || playerId === userId) && !loading && loaded &&
                    <EditUserDataDialog open={dialogOpen} setOpen={setDialogOpen} data={data}
                                        editData={updateUserData}/>}
                {(!playerId || playerId === userId) && !loading && loaded &&
                    <EditPasswordDialog open={changePasswordOpen} setOpen={setChangePasswordOpen} data={data}
                                        editData={changePassword}/>}


            </div>
            {loading && <LoadingComponent/>}
        </div>
    );
};

export default ProfileScreen;
