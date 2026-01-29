import {Route, Switch} from "react-router-dom";
import React from 'react';
import SideBar from "../components/bars/SideBar";
import SinglesScreen from "../screens/games/solo/SinglesScreen";
import EasyCapsGamesScreen from "../screens/games/solo/EasyCapsGamesScreen";
import UnrankedScreen from "../screens/games/solo/UnrankedScreen";
import DoublesScreen from "../screens/games/team/DoublesScreen";
import TenCommandmentsScreen from "../screens/public/TenCommandmentsScreen";
import RulesScreen from "../screens/public/RulesScreen";
import SoloGame from "../components/game/details/SoloGame";
import {useXtraSmallSize} from "../utils/SizeQuery";
import MobileTopBar from "../components/bars/TopBar";
import {useTheme} from "@material-ui/core";
import TournamentComponent from "../components/tournament/TournamentComponent";
import TournamentsComponent from "../screens/tournament/TournamentsComponent";
import DoublesGame from "../components/game/details/DoublesGame";
import ProfileScreen from "../screens/profile/ProfileScreen";
import HomeScreen from "../screens/public/HomeScreen";
import SideBarMobile from "../components/bars/SideBarMobile";
import TeamScreen from "../screens/profile/TeamProfile";
import SearchScreen from "../screens/public/SearchScreen";
import Comparison from "../components/teams/Comparison";
import LiveGameScreen from "../screens/games/solo/LiveGameScreen";
import TournamentTester from "../components/tournament/TournamentTester";

const InsecureNavigation = ({open,setOpen}) => {

    const small = useXtraSmallSize();
    const theme = useTheme();

    return (
        <div style={{height: '100%'}}>
            {small ? <SideBarMobile open={open} setOpen={setOpen}/>
                : <SideBar open={open} setOpen={setOpen}/>}
            {small && <MobileTopBar open={open} setOpen={setOpen}/>}
            <div style={{
                paddingLeft: small ? 0 : 50,
                backgroundColor: theme.palette.background.default,
                height: '100%'
            }}>
                <Switch>


                    <Route exact path={"/"}>
                        <HomeScreen/>
                    </Route>

                    <Route exact path={"/search"}>
                        <SearchScreen/>
                    </Route>

                    <Route exact path={"/singles"}>
                        <SinglesScreen/>
                    </Route>

                    <Route exact path={"/singles/:gameId"}>
                        <SoloGame/>
                    </Route>

                    <Route exact path={"/players/:playerId"}>
                        <ProfileScreen/>
                    </Route>

                    <Route exact path={"/teams/:teamId"}>
                        <TeamScreen/>
                    </Route>

                    <Route exact path={"/easy"}>
                        <EasyCapsGamesScreen/>
                    </Route>

                    <Route exact path={"/easy/:gameId"}>
                        <SoloGame/>
                    </Route>


                    <Route exact path={"/unranked"}>
                        <UnrankedScreen/>
                    </Route>

                    <Route exact path={"/unranked/:gameId"}>
                        <SoloGame/>
                    </Route>


                    <Route exact path={"/doubles"}>
                        <DoublesScreen/>
                    </Route>

                    <Route exact path={"/doubles/:gameId"}>
                        <DoublesGame/>
                    </Route>

                    <Route exact path={"/10commandments"}>
                        <TenCommandmentsScreen/>
                    </Route>


                    <Route exact path={"/rules"}>
                        <RulesScreen/>
                    </Route>

                    <Route exact path={"/tournaments"}>
                        <TournamentsComponent/>
                    </Route>

                    <Route exact path={"/:tournamentType/tournament/:tournamentId"}>
                        <TournamentComponent/>
                    </Route>

                    <Route exact path={"/tournamentTester"}>
                        <TournamentTester/>
                    </Route>


                    <Route exact path={"/comparison"}>
                        <Comparison/>
                    </Route>


                </Switch>
            </div>
        </div>
    );
};


export default InsecureNavigation;
