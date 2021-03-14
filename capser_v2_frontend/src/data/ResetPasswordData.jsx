import axios from "axios";
import {useDispatch} from "react-redux";
import {showSuccess} from "../redux/actions/alertActions";
import {useHistory} from "react-router-dom";

export const useResetPassword = () => {
    const dispatch = useDispatch();
    const history = useHistory();

    const resetPassword = (email) => {
        axios.post(`/users/resetPassword?email=${email}`).then(r => {
                dispatch(showSuccess("Password reset token was send to an email associated with your account"));
                history.push("/");
            }
        )
    }

    const updatePassword = (code, newPassword) => {
        axios.post(`/users/updatePassword`, {code, password: newPassword}).then(r => {
            dispatch(showSuccess("Password was updated"));
            history.push("/login");
        }).catch(e => {
            console.log(e);
        })
    }

    return {resetPassword, updatePassword};
}
