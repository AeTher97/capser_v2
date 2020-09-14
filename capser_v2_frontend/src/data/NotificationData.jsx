import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";

const useNotificationFetch = () => {

    const {accessToken} = useSelector(state => state.auth);

    return () => {
        return axios.get(`/notifications`,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };
};

export const useMarkNotificationAsSeen = () => {
    const {accessToken} = useSelector(state => state.auth);

    return (notificationId) => {
        return axios.put(`/notifications/seen/${notificationId}`,null,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };
}


export default useNotificationFetch;
