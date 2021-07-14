import React from 'react';
import {Redirect, Route, useLocation} from "react-router-dom";
import {useSelector} from "react-redux";


const SecureRoute = ({children, ...props}) => {
    const {isAuthenticated} = useSelector(state => state.auth)
    const location2 = useLocation();

    return (
        <Route
            {...props}
            render={({location}) =>
                (isAuthenticated) ? (
                    children
                ) : (
                    <Redirect
                        to={{
                            pathname: "/login",
                            state: {from: location2.pathname}
                        }}/>
                )}
        />
    );
};

export default SecureRoute;
