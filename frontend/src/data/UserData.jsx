import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {UPDATE_DATA} from "../redux/actions/types/authActionTypes";
import {showError, showSuccess} from "../redux/actions/alertActions";
import {useEffect, useState} from "react";


export const useUserData = (id) => {

    const {accessToken} = useSelector(state => state.auth);
    const dispatch = useDispatch();

    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [loaded, setLoaded] = useState(false);


    useEffect(() => {
        let shouldUpdate = true;
        if (id) {
            setLoading(true);
            axios.get(`/users/${id}/full`).then(response => {
                if (shouldUpdate) {
                    setData(response.data);
                }
            }).finally(() => {
                if (shouldUpdate) {
                    setLoading(false)
                    setLoaded(true)
                }
            })
        }
        return () => {
            shouldUpdate = false;
        }
    }, [id])

    const updateUserData = (newData) => {
        return axios.put(`/users/${id}`, newData, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }).then((response) => {
            setData(response.data)
            dispatch({
                type: UPDATE_DATA, payload: {
                    email: newData.email,
                    username: response.data.username
                }
            })
            dispatch(showSuccess("Data saved"))
        }).catch(e => {
            if (e.response.status === 403) {
                dispatch(showError("Password invalid"));
                throw  e;
            }
            if (e.response.data.error) {
                dispatch(showError(e.response.data.error))
            }

        })
    }
    return {updateUserData, data, loading, loaded}
}

export const useUserPlots = (id, gameType) => {

    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [loaded, setLoaded] = useState(false);


    useEffect(() => {
        let shouldUpdate = true;
        if (id) {
            setLoading(true);
            setLoaded(false);
            axios.get(`/users/${id}/plots?gameType=${gameType}`).then(response => {
                if (shouldUpdate) {
                    setData(response.data);
                }
            }).finally(() => {
                if (shouldUpdate) {
                    setLoading(false)
                    setLoaded(true)
                }
            })
        }
        return () => {
            shouldUpdate = false;
        }
    }, [id, gameType])

    return {data, loading, loaded}

}
