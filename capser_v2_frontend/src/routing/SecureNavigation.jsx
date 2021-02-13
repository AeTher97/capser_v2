import {Route, Switch} from "react-router-dom";
import React, {useState} from 'react';
import SideBar from "../components/bars/SideBar";
import AddSinglesGameComponent from "../components/pages/singles/AddSinglesGameComponent";
import AcceptanceComponent from "../components/pages/acceptance/AcceptanceComponent";
import TeamsComponent from "../components/pages/teams/TeamsComponent";
import {useXtraSmallSize} from "../utils/SizeQuery";
import MobileTopBar from "../components/bars/TopBar";

const SecureNavigation = () => {

    const small = useXtraSmallSize();
    const [open,setOpen] = useState();

    return (
        <div>
            <SideBar open={open} setOpen={setOpen}/>
            {small && <MobileTopBar open={open} setOpen={setOpen}/>}
            <div style={{paddingLeft: small ? 0 : 44}}>

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

            </Switch>
            </div>
        </div>
    );
};


export default SecureNavigation;
