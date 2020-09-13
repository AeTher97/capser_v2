import {CLOSE_ALERT, SHOW_ERROR, SHOW_INFORMATION, SHOW_SUCCESS, SHOW_WARNING} from "../actions/types/alertActions";


const initialState = {
    isOpen: false,
    message: "",
    severity: ""
};


const alertReducer = (state = initialState, action) => {
    switch (action.type) {
        case SHOW_INFORMATION:
            return {
                isOpen: true,
                message: action.payload,
                severity: "info"
            }
        case SHOW_ERROR:
            return {
                isOpen: true,
                message: action.payload,
                severity: "error"
            }
        case SHOW_WARNING:
            return {
                isOpen: true,
                message: action.payload,
                severity: "warning"
            }
        case SHOW_SUCCESS:
            return {
                isOpen: true,
                message: action.payload,
                severity: "success"
            }

        case CLOSE_ALERT:
            return {
                ...state,
                isOpen: false
            }
        default:
            return state;

    }
}


export default alertReducer;