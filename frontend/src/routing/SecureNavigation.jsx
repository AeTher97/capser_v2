import {Route, Switch} from "react-router-dom";
import React from 'react';
import SideBar from "../components/bars/SideBar";
import AddSinglesGameComponent from "../components/game/AddSinglesGameComponent";
import AcceptanceScreen from "../screens/private/AcceptanceScreen";
import TeamsScreen from "../screens/private/TeamsScreen";
import {useXtraSmallSize} from "../utils/SizeQuery";
import MobileTopBar from "../components/bars/TopBar";
import {useTheme} from "@material-ui/core";
import ProfileScreen from "../screens/profile/ProfileScreen";
import SideBarMobile from "../components/bars/SideBarMobile";
import LiveGameScreen from "../screens/games/solo/LiveGameScreen";

const SecureNavigation = ({open, setOpen}) => {

    const small = useXtraSmallSize();
    const theme = useTheme();

    return (
        <div style={{height: "100dvh", display: "flex", flexDirection: "column"}}>
            {small ? <SideBarMobile open={open} setOpen={setOpen}/>
                : <SideBar open={open} setOpen={setOpen}/>}
            {small && <MobileTopBar open={open} setOpen={setOpen}/>}
            <div style={{
                paddingLeft: small ? 0 : 44,
                backgroundColor: theme.palette.background.default,
                flex: 1,
                minHeight: 0
            }}>

                <Switch>

                    <Route exact path={"/secure/liveGame"}>
                        <div style={{height: "100%", position: "relative"}}>
                            <LiveGameScreen/>
                        </div>
                    </Route>

                    <Route exact path={"/secure/addSinglesGame"}>
                        <div>
                            <AddSinglesGameComponent/>
                        </div>
                    </Route>

                    <Route exact path={"/secure/acceptance"}>
                        <div>
                            <AcceptanceScreen/>
                        </div>
                    </Route>

                    <Route exact path={"/secure/teams"}>
                        <div>
                            <TeamsScreen/>
                        </div>
                    </Route>

                    <Route exact path={"/secure/profile"}>
                        <div>
                            <ProfileScreen/>
                        </div>
                    </Route>

                </Switch>
            </div>
        </div>
    );
};


export default SecureNavigation;
