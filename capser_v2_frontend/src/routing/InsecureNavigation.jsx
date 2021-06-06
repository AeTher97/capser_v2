import {Route, Switch} from "react-router-dom";
import React, {useState} from 'react';
import SideBar from "../components/bars/SideBar";
import HomeScreen from "../screens/HomeScreen";
import SinglesScreen from "../screens/SinglesScreen";
import EasyComponent from "../components/pages/easy/EasyComponent";
import UnrankedComponent from "../components/pages/unranked/UnrankedComponent";
import DoublesComponent from "../components/pages/doubles/DoublesComponent";
import TenCommandments from "../components/pages/10Commandments/TenCommandments";
import Rules from "../components/pages/rules/Rules";
import SinglesGame from "../components/pages/singles/SinglesGame";
import {useXtraSmallSize} from "../utils/SizeQuery";
import MobileTopBar from "../components/bars/TopBar";
import {useTheme} from "@material-ui/core";
import TournamentComponent from "../components/tournament/TournamentComponent";
import TournamentsComponent from "../components/tournament/TournamentsComponent";
import PlayerComponent from "../components/profile/PlayerComponent";
import DoublesGame from "../components/pages/doubles/DoublesGame";

const InsecureNavigation = () => {

    const small = useXtraSmallSize();
    const [open,setOpen] = useState();
    const theme = useTheme();

    return (
        <div style={{height:'100%'}}>
            <SideBar open={open} setOpen={setOpen}/>
            {small && <MobileTopBar open={open} setOpen={setOpen}/>}
            <div style={{paddingLeft: small ? 0 : 44, backgroundColor: theme.palette.background.default, height:'100%'}}>
                <Switch>


                    <Route exact path={"/"}>
                        <HomeScreen/>
                    </Route>

                    <Route exact path={"/singles"}>
                        <SinglesScreen/>
                    </Route>

                    <Route exact path={"/singles/:gameId"}>
                        <SinglesGame/>
                    </Route>

                    <Route exact path={"/players/:playerId"}>
                        <PlayerComponent/>
                    </Route>


                    <Route exact path={"/easy"}>
                        <EasyComponent/>
                    </Route>

                    <Route exact path={"/easy/:gameId"}>
                        <SinglesGame/>
                    </Route>


                    <Route exact path={"/unranked"}>
                        <UnrankedComponent/>
                    </Route>

                    <Route exact path={"/unranked/:gameId"}>
                        <SinglesGame/>
                    </Route>


                    <Route exact path={"/doubles"}>
                        <DoublesComponent/>
                    </Route>

                    <Route exact path={"/doubles/:gameId"}>
                        <DoublesGame/>
                    </Route>


                    <Route exact path={"/10commandments"}>
                        <TenCommandments/>
                    </Route>


                    <Route exact path={"/rules"}>
                        <Rules/>
                    </Route>

                    <Route exact path={"/tournaments"}>
                        <TournamentsComponent/>
                    </Route>

                    <Route exact path={"/:tournamentType/tournament/:tournamentId"}>
                        <TournamentComponent/>
                    </Route>


                </Switch>
            </div>
        </div>
    );
};


export default InsecureNavigation;
