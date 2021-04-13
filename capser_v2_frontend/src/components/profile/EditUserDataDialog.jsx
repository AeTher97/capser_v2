import React from 'react';
import useFieldValidation from "../../utils/useFieldValidation";
import {validateEmail, validateLength} from "../../utils/Validators";
import ValidatedField from "../misc/ValidatedField";
import {Button, Dialog, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import {useSelector} from "react-redux";

const EditUserDataDialog = ({open, setOpen, editData, data}) => {
    const {email} = useSelector(state => state.auth);

    const usernameField = useFieldValidation(data.username, (word) => () => validateLength(word, 3, 15))
    const emailField = useFieldValidation(email || '', validateEmail)
    const passwordField = useFieldValidation('', (word) => () => validateLength(word, 6))
    usernameField.showError = true;
    emailField.showError = true;
    passwordField.showError = true;

    const classes = mainStyles();

    const close = () => {
        usernameField.setValue(data.username);
        emailField.setValue(email);
        passwordField.setValue('');
        setOpen(false);
    }
    const saveData = (e) => {
        e.preventDefault();
        if (usernameField.validate()() || emailField.validate() || passwordField.validate()()) {
            return;
        }
        console.log("saving")
        editData({
            email: emailField.value,
            username: usernameField.value,
            password: passwordField.value
        }).then((e) => {
            setOpen(false);
            passwordField.setValue('');
        }).catch(e => {

        })
    }

    return (
        <Dialog open={open}>
            <form onSubmit={saveData}>
                <div className={classes.standardBorder} style={{margin: 0, maxWidth: 300}}>
                    <Typography variant={"h5"}>Change account data</Typography>
                    <ValidatedField field={usernameField} label={usernameField.value === "" ? "Username" : ""}/>
                    <ValidatedField field={emailField} label={emailField.value === "" ? "Email" : ""}/>
                    <ValidatedField field={passwordField} label={passwordField.value === "" ? "Password" : ""}
                                    type={"password"}/>
                    <div style={{marginTop: 10}}>
                        <Button style={{marginRight: 5}} type={"submit"}>Save</Button>
                        <Button variant={"outlined"} onClick={close}>Cancel</Button>
                    </div>
                </div>
            </form>
        </Dialog>

    );
};

export default EditUserDataDialog;
