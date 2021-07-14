import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";

const useAcceptanceFetch = () => {

    const {accessToken} = useSelector(state => state.auth);

    return () => {
        return axios.get(`/acceptance`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })
    };
};


export default useAcceptanceFetch;
