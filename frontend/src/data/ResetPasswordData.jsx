import axios from "axios";
import {useDispatch} from "react-redux";
import {showError, showSuccess} from "../redux/actions/alertActions";
import {useHistory} from "react-router-dom";

export const useResetPassword = () => {
    const dispatch = useDispatch();
    const history = useHistory();

    const resetPassword = (email, callback) => {
        axios.post(`/users/resetPassword?email=${email}`).then(r => {
                dispatch(showSuccess("Password reset token was send to an email associated with your account"));
                callback();
            }
        )
    }

    const updatePassword = (code, newPassword, callback = () => {
    }) => {
        axios.post(`/users/updatePassword`, {code, password: newPassword}).then(r => {
            dispatch(showSuccess("Password was updated"));
            history.push("/login");
        }).catch(e => {
            dispatch(showError(e.response.data.error))
            console.log(e.response.data.error);
            callback()
        })
    }

    return {resetPassword, updatePassword};
}
