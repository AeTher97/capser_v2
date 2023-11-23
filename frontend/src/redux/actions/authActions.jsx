import axios from "axios";
import {
    LOGIN_ATTEMPT,
    LOGIN_FAILED,
    LOGIN_SUCCESS,
    LOGOUT,
    REFRESH_ATTEMPT,
    REFRESH_FAILED,
    REFRESH_SUCCESS
} from "./types/authActionTypes";
import {decodeToken} from "../../utils/TokenUtils";



const axiosInstance = axios.create(
    {baseURL: baseURL}
)


export const loginAction = ({username, password}, onSuccessCallback = () => null) => dispatch => {
    dispatch({type: LOGIN_ATTEMPT});

    let formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);


    axiosInstance.post('/login', formData)
        .then(({data}) => {
            const payload = {
                ...decodeToken(data.authToken),
                accessToken: data.authToken,
                refreshToken: data.refreshToken,
                username: username
            };

            dispatch({type: LOGIN_SUCCESS, payload: payload});
            onSuccessCallback(data.authToken, data.refreshToken, payload.email);
        })
        .catch(err => {
            console.error('Login unsuccessful');
            dispatch({type: LOGIN_FAILED, error: "Invalid username or password"});
        });
};

export const createAccount = (request) => {
    return axiosInstance.post('/users', request);
}

