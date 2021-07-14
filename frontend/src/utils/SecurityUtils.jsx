import {useSelector} from "react-redux";

export const useHasRole = () => {

    const {roles} = useSelector(state => state.auth)

    const hasRole = (...checkedRoles) => {
        if (roles) {
            return checkedRoles.some(item => item === roles);
        } else {
            return false;
        }
    }

    return hasRole;
}
