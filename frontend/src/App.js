import React from 'react';
import './App.css';
import RefreshProvider from "./components/misc/RefreshProvider";
import MainNavigation from "./routing/MainNavigation";
import {BrowserRouter as Router, useHistory} from "react-router-dom";
import {darkTheme} from "./misc/Theme";
import {ThemeProvider} from "@material-ui/styles";
import UpdateEmailDialog from "./utils/UpdateEmailDialog";


function App() {

    const theme = darkTheme;

    const bodyElt = document.querySelector("body");
    bodyElt.style.backgroundColor = theme.palette.background.default;
    const history = useHistory();

    return (
        <div className="App">
            <RefreshProvider/>

            <Router basename={process.env.PUBLIC_URL} history={history}>
                <ThemeProvider theme={theme}>
                    <UpdateEmailDialog/>
                    <MainNavigation/>
                </ThemeProvider>
            </Router>
        </div>
    );
}


export default App;
