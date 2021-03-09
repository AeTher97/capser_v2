import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {UPDATE_DATA} from "../redux/actions/types/authActionTypes";
import {showError, showSuccess} from "../redux/actions/alertActions";


export const useUserData = (id) => {

    const {accessToken} = useSelector(state => state.auth);
    const dispatch = useDispatch();

    const updateUserData = (newData) => {
        axios.put(`/users/${id}`, newData, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }).then((response) => {
            dispatch({
                type: UPDATE_DATA, payload: {
                    email: response.data.email,
                    username: response.data.username
                }
            })
            dispatch(showSuccess("Email address was saved"))
        }).catch(e => {
            if (e.response.data.error) {
                dispatch(showError(e.response.data.error))
            }
        })
    }
    return {updateUserData}
}
