import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";

export const useGamePost = (type, request) => {

    const {accessToken} = useSelector(state => state.auth);

    // return (username) => {
    //     return axios.get(`${url}?pageSize=${pageSize}&pageNumber=${pageNumber}&username=${username}`,{
    //         headers: {
    //             'Authorization' : `Bearer ${accessToken}`
    //         }
    //     })
    // };
};

export const useGameFetch = () => {

    const {accessToken} = useSelector(state => state.auth);

    return (gameId,gameType) => {
        return axios.get(`games/${gameId}/?gameType=${gameType}`,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };
}


