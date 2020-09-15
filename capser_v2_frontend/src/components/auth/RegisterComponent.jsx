import React, {useState} from 'react';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Typography} from "@material-ui/core";
import {useDispatch, useSelector} from "react-redux";
import {createAccount, loginAction} from "../../redux/actions/authActions";
import {useHistory,useLocation} from "react-router-dom";
import {saveTokenInStorage} from "../../utils/TokenUtils";
import useFieldValidation from "../../utils/useFieldValidation";
import FormComponent from "../misc/FormComponent";
import CenteredColumn from "../misc/CenteredColumn";




const RegisterComponent = props => {

    const location = useLocation();
    const classes = useStyle();

    const password = useFieldValidation("", () => {
    });
    const repeatPassword = useFieldValidation("", () => {
    });
    const username = useFieldValidation("", () => {
    });

    const [error,setError]  = useState('');

    const history = useHistory();

    const fields = [
        {
            label: 'Username',
            validation: username
        }, {
            label: 'Password',
            validation: password,
            type: 'password'
        },
        {
            label: 'Repeat Password',
            validation: repeatPassword,
            type: 'password'
        }]

    const handleClick = (e) => {
        e.preventDefault();
        createAccount({
            username: username.value,
            password: password.value,
            repeatPassword: repeatPassword.value
        }).then(() => {
            history.push("/login")
        }).catch((e) => {
            setError('Error while registering')
        })
    }

    return (
        <div className={classes.root}>
            <div className={classes.loginContainer}>
                <img style={{maxWidth: 260}} src={"/splash.png"}/>
                <CenteredColumn>
                    <FormComponent
                        title={'Create account'}
                        fields={fields}
                        onSubmit={handleClick}
                        buttonText={"Sign up"}
                        stretchButton={true}
                        error={error}
                    />
                    <div className={classes.footer}>
                        <Typography variant={"caption"}>Have an account? </Typography>
                        <Typography variant={"caption"} color={"primary"} className={classes.link} onClick={() => {history.push('/login')}}>Sign in</Typography>
                    </div>
                </CenteredColumn>
            </div>
        </div>
    );
};

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
    },
    link: {
        "&:hover": {
            textDecoration: 'underline',
            cursor: 'pointer'
        }
    }
}));

export default RegisterComponent;
