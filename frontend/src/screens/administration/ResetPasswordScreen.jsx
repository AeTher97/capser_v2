import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";
import {Button, Typography} from "@material-ui/core";
import useFieldValidation from "../../utils/useFieldValidation";
import {validateEmail} from "../../utils/Validators";
import ValidatedField from "../../components/misc/ValidatedField";
import {useResetPassword} from "../../data/ResetPasswordData";
import {useHistory} from "react-router-dom";

const ResetPasswordScreen = () => {

    const history = useHistory();
    const emailField = useFieldValidation('', validateEmail);
    emailField.showError = true;
    const {resetPassword} = useResetPassword();

    const sendResetRequest = () => {
        if (emailField.validate()) {
            return;
        }
        resetPassword(emailField.value, history.push("/reset/go"));
    }

    const classes = mainStyles();
    return (
        <div style={{color: 'white', display: 'flex', justifyContent: 'center', height: '100%', alignItems: 'center'}}>
            <div style={{flex: 1, maxWidth: 500}}>
                <form className={classes.standardBorder} onSubmit={sendResetRequest}>
                    <Typography variant={"h5"}>Reset password</Typography>
                    <Typography>To reset password please type in the email associated with your account.</Typography>
                    <ValidatedField field={emailField} label={emailField.value === '' ? 'Email' : ''}/>
                    <Button style={{marginTop: 10}} type={"submit"}>Reset</Button>
                </form>
            </div>
        </div>
    );
};

export default ResetPasswordScreen;
