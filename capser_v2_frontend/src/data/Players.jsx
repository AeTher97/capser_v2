import axios from "axios";

export const usePlayersListFetch = (type, pageSize = 10) => {


    return (pageNumber) => {
        return axios.get(`/users?pageSize=${pageSize}&pageNumber=${pageNumber}&gameType=${type}`)
    };
}
