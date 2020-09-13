import React from 'react';
import {Redirect, Route} from "react-router-dom";
import {useSelector} from "react-redux";


const SecureRoute = ({children, ...props}) => {
    const {isAuthenticated} = useSelector(state => state.auth)

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
                        state: {from: location}
                    }}/>
            )}
        />
    );
};

export default SecureRoute;
