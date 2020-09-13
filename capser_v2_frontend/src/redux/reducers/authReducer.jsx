import {
    LOGIN_ATTEMPT,
    LOGIN_FAILED,
    LOGIN_SUCCESS,
    LOGOUT,
    REFRESH_ATTEMPT,
    REFRESH_FAILED,
    REFRESH_SUCCESS
} from "../actions/types/authActionTypes";
import {decodeToken, isTokenOutdated, saveTokenInStorage} from "../../utils/TokenUtils";

const getAuthFromStorage = () => {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    const email = localStorage.getItem('email');


    if (accessToken) {
        const result = {...decodeToken(accessToken)};

        const refreshResult = {...decodeToken(refreshToken)}
        if (isTokenOutdated(refreshResult.expirationTime)) {
            return emptyState;
        }

        if (result.error || refreshResult.error) {
            return result;
        } else
            return {
                ...result,
                accessToken: accessToken,
                refreshToken: refreshToken,
                isAuthenticated: true,
                email: email,
                error: null
            }
    }

};

const clearLocalStorage = () => {
    localStorage.removeItem("email")
    localStorage.removeItem("refreshToken")
    localStorage.removeItem("accessToken")
}

const emptyState = {
    userId: null,
    roles: null,
    accessToken: null,
    refreshToken: null,
    expirationTime: null,
    isAuthenticated: false,
    error: null,
    email: null
}

const initialState = {
    ...emptyState,
    ...getAuthFromStorage()
};


const authReducer = (state = initialState, action) => {
    switch (action.type) {
        case LOGIN_SUCCESS:
        case REFRESH_SUCCESS:
            saveTokenInStorage(action.payload.accessToken, state.refreshToken, state.email)
            return {
                ...state,
                ...action.payload,
                isAuthenticated: true,
                error: null,
            }
        case LOGIN_FAILED:
            clearLocalStorage();
            return {
                isAuthenticated: false,
                error: action.error,
            }
        case LOGIN_ATTEMPT: {
            return {
                ...emptyState,
                error: state.error
            }
        }
        case REFRESH_FAILED:
            return {
                ...emptyState,
                error: action.error
            }
        case LOGOUT:
            clearLocalStorage();
            return emptyState;
        case REFRESH_ATTEMPT:
        default:
            return state;

    }
}


export default authReducer;