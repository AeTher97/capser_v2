import React, {useCallback} from 'react';
import useFieldValidation from "../../utils/useFieldValidation";
import {validatePassword, validateRepeatedPassword} from "../../utils/Validators";
import {useResetPassword} from "../../data/ResetPasswordData";
import mainStyles from "../../misc/styles/MainStyles";
import {Button, Typography} from "@material-ui/core";
import ValidatedField from "../misc/ValidatedField";
import useQuery from "../../utils/UserQuery";

const UpdatePasswordComponent = () => {
    const password = useFieldValidation("", validatePassword);
    const query = useQuery();

    const code = query.get('code');
    const validatePasswordConfirmation = useCallback(validateRepeatedPassword(password.value), [password.value]);
    const repeatPassword = useFieldValidation("", validatePasswordConfirmation);

    password.showError = true;
    repeatPassword.showError = true;

    const {updatePassword} = useResetPassword();

    const onUpdatePassword = () => {
        if (password.validate() || repeatPassword.validate()) {
            return;
        }
        updatePassword(code, password.value);
    }


    const classes = mainStyles();
    return (
        <div style={{color: 'white', display: 'flex', justifyContent: 'center', height: '100%', alignItems: 'center'}}>
            <div style={{flex: 1, maxWidth: 500}}>
                <div className={classes.standardBorder}>
                    <Typography variant={"h5"}>Change password</Typography>
                    <Typography>Type in your new password.</Typography>
                    <ValidatedField field={password} label={password.value === '' ? 'Password' : ''} type={"password"}/>
                    <ValidatedField field={repeatPassword} label={repeatPassword.value === '' ? 'RepeatPassword' : ''}
                                    type={'password'}/>
                    <Button style={{marginTop: 10}} onClick={onUpdatePassword}>Update</Button>
                </div>
            </div>
        </div>
    );
};

export default UpdatePasswordComponent;
