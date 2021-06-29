import React from "react";
import ErrorBoundary from "../utils/ErrorBoundary";
import SignInComponent from "../components/auth/SignInComponent";
import ErrorAlert from "../utils/ErrorAlert";


const SignInScreen = props => {
    return (
        <ErrorBoundary render={(error) => <ErrorAlert error={error}/>}>
            <SignInComponent {...props}/>
        </ErrorBoundary>);
};

export default SignInScreen;

