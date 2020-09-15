import {Route, Switch} from "react-router-dom";
import React from 'react';
import SideBar from "../components/bars/SideBar";
import HomeScreen from "../screens/HomeScreen";
import SinglesScreen from "../screens/SinglesScreen";
import RegisterComponent from "../components/auth/RegisterComponent";

const InsecureNavigation = () => {


    return (
        <div>
            <SideBar/>
            <div style={{paddingLeft: 44}}>
            <Switch>


                <Route exact path={"/"}>
                   <HomeScreen/>
                </Route>

                <Route exact path={"/singles"}>
                    <SinglesScreen/>
                </Route>




            </Switch>
            </div>
        </div>
    );
};


export default InsecureNavigation;
