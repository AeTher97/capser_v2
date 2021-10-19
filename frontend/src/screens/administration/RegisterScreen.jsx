import React, {useCallback, useState} from 'react';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Typography} from "@material-ui/core";
import {createAccount} from "../../redux/actions/authActions";
import {useHistory} from "react-router-dom";
import useFieldValidation from "../../utils/useFieldValidation";
import FormComponent from "../../components/misc/FormComponent";
import CenteredColumn from "../../components/misc/CenteredColumn";
import {validateEmail, validateLength, validatePassword, validateRepeatedPassword} from "../../utils/Validators";


const RegisterScreen = () => {

    const classes = useStyle();

    const password = useFieldValidation("", validatePassword);

    const validatePasswordConfirmation = useCallback(validateRepeatedPassword(password.value), [password.value]);
    const repeatPassword = useFieldValidation("", validatePasswordConfirmation);
    const username = useFieldValidation("", (word) => () => validateLength(word, 3, 15))
    const email = useFieldValidation("", validateEmail)

    password.showError = true;
    repeatPassword.showError = true;
    username.showError = true;
    email.showError = true;

    const [error, setError] = useState('');

    const history = useHistory();

    const fields = [
        {
            label: 'Username',
            validation: username
        },
        {
            label: 'Email',
            validation: email
        }, {
            label: 'Password',
            validation: password,
            type: 'password'
        },
        {
            label: 'Repeat Password',
            validation: repeatPassword,
            type: 'password'
        }
    ]

    const handleClick = (e) => {
        e.preventDefault();

        if (password.validate() || repeatPassword.validate() || username.validate()()) {
            console.log()
            return;
        }
        createAccount({
            username: username.value,
            password: password.value,
            repeatPassword: repeatPassword.value,
            email: email.value
        }).then(() => {
            history.push("/login")
        }).catch((e) => {
            if (e.response) {
                if (e.response.data.error) {
                    setError(e.response.data.error);
                }
            } else {
                setError('Error while registering')
            }
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
                        <Typography variant={"caption"} color={"primary"} className={classes.link} onClick={() => {
                            history.push('/login')
                        }}>Sign in</Typography>
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

export default RegisterScreen;
