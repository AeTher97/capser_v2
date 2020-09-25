import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";
import {easing} from "@material-ui/core";
import {getRequestGameTypeString} from "../utils/Utils";


export const useSoloGamePost = (type) => {

    const {accessToken} = useSelector(state => state.auth);


    return (gameRequest) => {
        return axios.post(`${getRequestGameTypeString(type)}`,gameRequest,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };
};


export const useGameListFetch = (type, pageSize = 10) => {


    return (pageNumber) => {
        return axios.get(`/${getRequestGameTypeString(type)}?pageSize=${pageSize}&pageNumber=${pageNumber}`)
    };
}

export const useGameFetch = () => {

    return (gameId,gameType) => {
        return axios.get(`games/${gameId}/?gameType=${gameType}`)
    };
}

export const useGameAcceptance = () =>{

    const {accessToken} = useSelector(state => state.auth);

    return {acceptGame: (id, type) => {
        return axios.post(`${getRequestGameTypeString(type)}/accept/${id}`,null,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
        },
        rejectGame: (id, type) => {
            return axios.post(`${getRequestGameTypeString(type)}/reject/${id}`, null , {
                headers: {
                    'Authorization' : `Bearer ${accessToken}`
                }
            })
        },
    }
}


