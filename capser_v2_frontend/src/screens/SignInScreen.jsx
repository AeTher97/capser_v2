import React from "react";
import ErrorBoundary from "../utils/ErrorBoundary";
import SignInComponent from "../components/auth/SignInComponent";
import ErrorAlert from "../utils/ErrorAlert";


const SignInScreen = () => {
    return (
        <ErrorBoundary render={(error) => <ErrorAlert error={error}/>}>
            <SignInComponent/>
        </ErrorBoundary>);
};

export default SignInScreen;

