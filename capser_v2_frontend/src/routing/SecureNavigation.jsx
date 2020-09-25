import {Route, Switch} from "react-router-dom";
import React from 'react';
import SideBar from "../components/bars/SideBar";
import AddSinglesGameComponent from "../components/pages/singles/AddSinglesGameComponent";
import AcceptanceComponent from "../components/pages/acceptance/AcceptanceComponent";
import TeamsComponent from "../components/pages/teams/TeamsComponent";

const SecureNavigation = () => {


    return (
        <div>
            <SideBar/>
            <div style={{paddingLeft: 44}}>

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
