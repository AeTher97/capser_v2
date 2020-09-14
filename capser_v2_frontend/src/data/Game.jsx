import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";

export const useGamePost = (type) => {

    const {accessToken} = useSelector(state => state.auth);

    let urlPart;
    switch (type) {
        case 'SINGLES':
            urlPart = 'singles'
    }

    return (gameRequest) => {
        return axios.post(`${urlPart}`,gameRequest,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };
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


