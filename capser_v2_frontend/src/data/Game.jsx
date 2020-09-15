import React from 'react';
import axios from "axios";
import {useSelector} from "react-redux";
import {easing} from "@material-ui/core";


const getTypeString = (type) => {
    switch (type) {
        case 'SINGLES' :
            return 'singles'
        case 'EASY' :
            return 'easy'
        case 'DOUBLES' :
            return 'doubles'
        case 'UNRANKED' :
            return 'unranked'
    }
}


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


export const useGameListFetch = (type, pageSize = 10, pageNumber = 0) => {


    return () => {
        return axios.get(`/${getTypeString(type)}?pageSize=${pageSize}&pageNumber=${pageNumber}`)
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
        return axios.post(`${getTypeString(type)}/accept/${id}`,null,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
        },
        rejectGame: (id, type) => {
            return axios.post(`${getTypeString(type)}/reject/${id}`, null , {
                headers: {
                    'Authorization' : `Bearer ${accessToken}`
                }
            })
        },
    }
}


