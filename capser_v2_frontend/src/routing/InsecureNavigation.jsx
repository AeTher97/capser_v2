import {Route, Switch} from "react-router-dom";
import React from 'react';
import SideBar from "../components/bars/SideBar";
import HomeScreen from "../screens/HomeScreen";
import SinglesScreen from "../screens/SinglesScreen";
import EasyComponent from "../components/pages/easy/EasyComponent";
import UnrankedComponent from "../components/pages/unranked/UnrankedComponent";
import DoublesComponent from "../components/pages/doubles/DoublesComponent";
import TenCommandments from "../components/pages/10Commandments/TenCommandments";
import Rules from "../components/pages/rules/Rules";

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


                <Route exact path={"/easy"}>
                    <EasyComponent/>
                </Route>

                <Route exact path={"/unranked"}>
                    <UnrankedComponent/>
                </Route>

                <Route exact path={"/doubles"}>
                    <DoublesComponent/>
                </Route>

                <Route exact path={"/10commandments"}>
                    <TenCommandments/>
                </Route>


                <Route exact path={"/rules"}>
                    <Rules/>
                </Route>



            </Switch>
            </div>
        </div>
    );
};


export default InsecureNavigation;
