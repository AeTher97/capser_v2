import axios from "axios";

export const useDashboardGamesFetch = () => {

    return () => {
        return axios.get(`/dashboard/games`)
    };
}

export const useBlogPostsFetch = () => {

    return () => {
        return axios.get(`/dashboard/posts`)
    };
}

