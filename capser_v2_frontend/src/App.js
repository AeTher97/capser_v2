import React from 'react';
import './App.css';
import RefreshProvider from "./components/misc/RefreshProvider";
import MainNavigation from "./routing/MainNavigation";
import {BrowserRouter as Router} from "react-router-dom";
import {darkTheme} from "./misc/Theme";
import {ThemeProvider} from "@material-ui/styles";


function App() {

    const theme =  darkTheme;

    const bodyElt = document.querySelector("body");
    bodyElt.style.backgroundColor = theme.palette.background.default;

    return (
        <div className="App">

            <Router basename={process.env.PUBLIC_URL}>
                <RefreshProvider/>
                <ThemeProvider theme={theme}>
                <MainNavigation/>
                </ThemeProvider>
            </Router>
        </div>
    );
}


export default App;
