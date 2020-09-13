import React from "react";
import ErrorBoundary from "../utils/ErrorBoundary";
import ErrorAlert from "../utils/ErrorAlert";
import HomeComponent from "../components/pages/HomeComponent";


const HomeScreen = () => {
    return (
        <ErrorBoundary render={(error) => <ErrorAlert error={error}/>}>
            <HomeComponent/>
        </ErrorBoundary>);
};

export default HomeScreen;

