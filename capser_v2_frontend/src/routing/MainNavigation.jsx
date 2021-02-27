import React from 'react';
import {Route, Switch,} from "react-router-dom";
import SecureRoute from "./SecureRoute";
import Snackbar from "@material-ui/core/Snackbar";
import Slide from "@material-ui/core/Slide";
import Alert from "@material-ui/lab/Alert";
import {useDispatch, useSelector} from "react-redux";
import {closeAlert} from "../redux/actions/alertActions";
import SecureNavigation from "./SecureNavigation";
import InsecureNavigation from "./InsecureNavigation";
import SignInScreen from "../screens/SignInScreen";
import RegisterComponent from "../components/auth/RegisterComponent";

const MainNavigation = () => {
    const {severity, message, isOpen} = useSelector(state => state.alert);
    const dispatch = useDispatch();
    const handleClose = () => {
        dispatch(closeAlert());
    }


    return (
        <Switch>
            <Route exact path='/login' render={(props) => <SignInScreen {...props}/>}/>

            <Route exact path={"/register"}>
                <RegisterComponent/>
            </Route>


            <SecureRoute path='/secure*'>
                <SecureNavigation/>
                <Snackbar open={isOpen} autoHideDuration={3000} onClose={handleClose} TransitionComponent={Slide}>
                    <Alert severity={severity}>
                        {message}
                    </Alert>
                </Snackbar>
            </SecureRoute>


            <Route path='/*'>
                <InsecureNavigation/>
                <Snackbar open={isOpen} autoHideDuration={3000} onClose={handleClose} TransitionComponent={Slide}>
                    <Alert severity={severity}>
                        {message}
                    </Alert>
                </Snackbar>
            </Route>


        </Switch>
    );
};

export default MainNavigation;
