import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";

const useNotificationFetch = () => {

    const {accessToken} = useSelector(state => state.auth);
    const pageNumber = 0;

    return () => {
        return axios.get(`/notifications`,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };
};


export default useNotificationFetch;
