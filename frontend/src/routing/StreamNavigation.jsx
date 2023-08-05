import {Route, Switch} from "react-router-dom";
import React from 'react';
import {useTheme} from "@material-ui/core";
import TournamentLadder from "../components/tournament/TournamentLadder";

const StreamNavigation = ({open, setOpen}) => {

    const theme = useTheme();

    console.log("xd")

    return (
        <div style={{height: '100%'}}>
            <div style={{
                backgroundColor: theme.palette.background.default,
                height: '100%'
            }}>
                <Switch>

                    <Route exact path={"/stream/:tournamentType/tournament/:tournamentId"}>
                        <TournamentLadder/>
                    </Route>


                </Switch>
            </div>
        </div>
    );
};


export default StreamNavigation;
