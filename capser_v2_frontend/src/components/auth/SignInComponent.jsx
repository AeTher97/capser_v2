import React from 'react';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Typography} from "@material-ui/core";
import {useDispatch, useSelector} from "react-redux";
import {loginAction} from "../../redux/actions/authActions";
import {useHistory,useLocation} from "react-router-dom";
import {saveTokenInStorage} from "../../utils/TokenUtils";
import useFieldValidation from "../../utils/useFieldValidation";
import FormComponent from "../misc/FormComponent";
import CenteredColumn from "../misc/CenteredColumn";




const SignInComponent = props => {

    const location = useLocation();
    const {from} = location.state || {from: {pathname: '/'}};
    const classes = useStyle();

    const password = useFieldValidation("", () => {
    });
    const email = useFieldValidation("", () => {
    });

    const {error} = useSelector(state => state.auth);

    const history = useHistory();
    const dispatch = useDispatch();

    const fields = [
        {
            label: 'Username',
            validation: email
        }, {
            label: 'Password',
            validation: password,
            type: 'password'
        }]

    const handleLogin = (e) => {
        e.preventDefault();
        dispatch(loginAction({
            email: email.value,
            password: password.value
        }, (authToken,refreshToken) => {
            history.push(from);
            saveTokenInStorage(authToken,refreshToken, email.value)
        }))
    }

    return (
        <div className={classes.root}>
            <div className={classes.loginContainer}>
                <CenteredColumn>
                    <img src={"/logo192.png"} />
                    <FormComponent
                        title={'Capser Log In'}
                        fields={fields}
                        onSubmit={handleLogin}
                        buttonText={"Log In"}
                        stretchButton={true}
                        error={error}
                    />
                    <div className={classes.footer}>
                        <Typography variant={"caption"}>Made with ❤ by Mike 2020</Typography>
                    </div>
                </CenteredColumn>
            </div>
        </div>
    );
};

SignInComponent.propTypes = {};

const useStyle = makeStyles(theme => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        position: 'absolute',
        width: '100%',
        height: '100%'
    },
    loginContainer: {
        padding: 10,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        position: 'absolute',
        [theme.breakpoints.down('xs')]: {
            width: '90%'
        },
        [theme.breakpoints.up('xs')]: {
            maxWidth: 400,
            minWidth: 250
        }
    },
    footer: {
        marginTop: 5,
        minHeight: theme.typography.h5.fontSize
    }
}));

export default SignInComponent;
