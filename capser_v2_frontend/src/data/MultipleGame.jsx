import {useSelector} from "react-redux";
import axios from "axios";
import {getRequestGameTypeString} from "../utils/Utils";

export const useMultipleGame = (type) => {
    const {accessToken} = useSelector(state => state.auth);


    const postGame = (gameRequest) => {
        return axios.post(`${getRequestGameTypeString(type)}`,gameRequest,{
            headers: {
                'Authorization' : `Bearer ${accessToken}`
            }
        })
    };

    return {postGame: postGame}
}
