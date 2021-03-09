import {Route, Switch} from "react-router-dom";
import React, {useState} from 'react';
import SideBar from "../components/bars/SideBar";
import AddSinglesGameComponent from "../components/pages/singles/AddSinglesGameComponent";
import AcceptanceComponent from "../components/pages/acceptance/AcceptanceComponent";
import TeamsComponent from "../components/pages/teams/TeamsComponent";
import {useXtraSmallSize} from "../utils/SizeQuery";
import MobileTopBar from "../components/bars/TopBar";
import {useTheme} from "@material-ui/core";
import UserComponent from "../components/profile/UserComponent";

const SecureNavigation = () => {

    const small = useXtraSmallSize();
    const [open,setOpen] = useState();
    const theme = useTheme();

    return (
        <div>
            <SideBar open={open} setOpen={setOpen}/>
            {small && <MobileTopBar open={open} setOpen={setOpen}/>}
            <div style={{paddingLeft: small ? 0 : 44, backgroundColor: theme.palette.background.default}}>

            <Switch>
                <Route exact path={"/secure/addSinglesGame"}>
                    <div>
                        <AddSinglesGameComponent/>
                    </div>

                </Route>

                <Route exact path={"/secure/acceptance"}>
                    <div>
                        <AcceptanceComponent/>
                    </div>

                </Route>

                <Route exact path={"/secure/teams"}>
                    <div>
                        <TeamsComponent/>
                    </div>
                </Route>

                <Route exact path={"/secure/profile"}>
                    <div>
                        <UserComponent/>
                    </div>
                </Route>

            </Switch>
            </div>
        </div>
    );
};


export default SecureNavigation;
