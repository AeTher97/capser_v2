import React from 'react';
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {isTokenOutdated} from "../../utils/TokenUtils";
import {refreshAction} from "../../redux/actions/authActions";

const RefreshProvider = () => {

    let authTokenRequest;

    function resetAuthTokenRequest() {
        authTokenRequest = null;
    }

    const {refreshToken, expirationTime} = useSelector(state => state.auth);
    const dispatch = useDispatch();



    async function getAuthToken() {
        if (!authTokenRequest) {
            authTokenRequest = dispatch(refreshAction(refreshToken));

            authTokenRequest.then(() => {
                resetAuthTokenRequest();
            })
        }
        return authTokenRequest;
    }

    console.log("Refresh provider reload")
    if (axios.interceptors.request.handlers.length > 0) {
        axios.interceptors.request.eject(axios.interceptors.request.handlers.length - 1);
    }
    axios.interceptors.request.use(async request => {
        if (expirationTime && !authTokenRequest) {
            if (isTokenOutdated(expirationTime)) {
                console.log("Refreshing token");
                return getAuthToken()
                    .then((payload) => {
                        request.headers['Authorization'] = `Bearer ${payload.authToken}`;
                        return request;
                    }).catch(err => {
                        console.log('Error in refresh');
                        return request;
                    })
            }
        }
        return request;
    })

    axios.defaults.baseURL = process.env.REACT_APP_BACKEND_URL;


    return (<></>);


};

export default RefreshProvider;
