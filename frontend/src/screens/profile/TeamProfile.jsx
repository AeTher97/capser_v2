import {Divider, Tab, Tabs, Typography, useTheme} from "@material-ui/core";
import {displayStats} from "../../utils/Utils";
import {useHistory, useParams} from "react-router-dom";
import React from "react";
import useQuery from "../../utils/UserQuery";
import mainStyles from "../../misc/styles/MainStyles";
import TabPanel from "../../components/misc/TabPanel";
import LoadingComponent from "../../utils/LoadingComponent";
import {useTeamData} from "../../data/TeamsData";
import TeamPicture from "../../components/profile/TeamPicture";
import PlayerTooltip from "../../components/tooltips/PlayerTooltip";


const TeamScreen = () => {

    const {teamId} = useParams();

    const history = useHistory();
    const {loading, team} = useTeamData(teamId);


    const query = useQuery();
    const tab = query.get('tab') || 'stats';

    const classes = mainStyles();
    const theme = useTheme();

    const handleTabChange = (e, value) => {
        history.push(`?tab=${value}`);
    }


    return (
        <div>
            {!loading && team && <>
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
                        <TeamPicture player1Hash={team.players[0].avatarHash} player2Hash={team.players[1].avatarHash}/>
                        <Typography variant={"h5"} style={{marginTop: 10}}>{team.teamWithStats.name}</Typography>
                        <Typography variant={"h6"} style={{marginTop: 10}}>Team</Typography>

                    </div>
                    <div style={{flex: 5, paddingTop: 20, maxWidth: '100%'}}>
                        <Tabs value={tab} onChange={handleTabChange} variant={"scrollable"}>
                            <Tab label={"Overview"} value={"stats"}/>
                        </Tabs>

                        {!loading && <>
                            <TabPanel value={tab} showValue={'stats'}>
                                <div className={classes.paddedContent}>
                                    <Typography variant={"h6"}>Game stats</Typography>
                                    <div style={{display: "flex", flexWrap: "wrap"}}>
                                        <div style={{display: "flex", flexDirection: "column", flex: 1}}>

                                            <div className={classes.standardBorder}
                                                 style={{flex: 1}}>{displayStats("DOUBLES", team.teamWithStats.doublesStats)}</div>
                                        </div>
                                        <div style={{display: "flex", flexDirection: "column", flex: 1}}>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div className={classes.paddedContent}>
                                        <Typography variant={"h6"}>Members:</Typography>
                                        <Divider/>
                                    </div>
                                    {team.players.map(player => {
                                        return <div key={player.id} style={{color: 'red', display: "flex"}}>
                                            <PlayerTooltip playerId={player.id} gameType={'DOUBLES'}>
                                                <Typography color={"inherit"}
                                                            style={{flex: 0}}>{player.username}</Typography>
                                            </PlayerTooltip>
                                        </div>
                                    })}

                                </div>

                            </TabPanel>

                        </>}
                    </div>


                </div>
            </>}
            {loading && <LoadingComponent/>}
        </div>
    );
};

export default TeamScreen;