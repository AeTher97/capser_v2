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
import SignInScreen from "../screens/administration/SignInScreen";
import RegisterScreen from "../screens/administration/RegisterScreen";
import ResetPasswordScreen from "../screens/administration/ResetPasswordScreen";
import FinishResettingPasswordScreen from "../screens/administration/FinishResettingPasswordScreen";
import ScrollToTop from "../utils/ScrollToTop";
import ResetFinalizeScreen from "../screens/administration/ResetFinalizeScreen";

const MainNavigation = () => {
    const {severity, message, isOpen} = useSelector(state => state.alert);
    const dispatch = useDispatch();
    const handleClose = () => {
        dispatch(closeAlert());
    }


    return (
        <ScrollToTop>
            <Switch>
                <Route exact path='/login' render={(props) => <><SignInScreen {...props}/>
                    <Snackbar open={isOpen} autoHideDuration={3000} onClose={handleClose} TransitionComponent={Slide}>
                        <Alert severity={severity}>
                            {message}
                        </Alert>
                    </Snackbar>
                </>}/>

                <Route exact path={"/register"}>
                    <RegisterScreen/>
                </Route>

                <Route exact path={"/reset"}>
                    <ResetPasswordScreen/>
                    <Snackbar open={isOpen} autoHideDuration={3000} onClose={handleClose} TransitionComponent={Slide}>
                        <Alert severity={severity}>
                            {message}
                        </Alert>
                    </Snackbar>
                </Route>

                <Route exact path={"/reset/go"}>
                    <ResetFinalizeScreen/>
                </Route>

                <Route exact path={"/resetUpdate"}>
                    <FinishResettingPasswordScreen/>
                    <Snackbar open={isOpen} autoHideDuration={3000} onClose={handleClose} TransitionComponent={Slide}>
                        <Alert severity={severity}>
                            {message}
                        </Alert>
                    </Snackbar>
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
        </ScrollToTop>
    );
};

export default MainNavigation;
