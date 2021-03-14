import React, {useEffect, useState} from 'react';
import {Button, Dialog, Typography} from "@material-ui/core";
import {useSelector} from "react-redux";
import mainStyles from "../misc/styles/MainStyles";
import useFieldValidation from "./useFieldValidation";
import {validateEmail, validateLength} from "./Validators";
import ValidatedField from "../components/misc/ValidatedField";
import {useUserData} from "../data/UserData";

const UpdateEmailDialog = () => {

    const {email, username, userId} = useSelector(state => state.auth);
    const [open, setOpen] = useState(false);
    const emailField = useFieldValidation("", validateEmail);
    const passwordField = useFieldValidation('', (word) => () => validateLength(word, 6))
    const {updateUserData} = useUserData(userId);
    emailField.showError = true;
    passwordField.showError = true;

    const classes = mainStyles();

    useEffect(() => {
        setOpen(!!(username && (email === undefined || email === "null" || email === "undefined")));
    }, [username, email])

    const updateEmail = (e) => {
        e.preventDefault();
        if (emailField.validate() || passwordField.validate()()) {
            return;
        }

        updateUserData({password: passwordField.value, email: emailField.value}).catch(e => {

        });

    }

    return (
        <Dialog open={open}>
            <form onSubmit={updateEmail}>
                <div className={classes.standardBorder} style={{margin: 0}}>
                    <Typography variant={"h5"}>Emails are being added to GCL</Typography>
                    <Typography>Please type in your email</Typography>
                    <ValidatedField field={emailField} label={emailField.value === '' ? "Email" : ""}/>
                    <ValidatedField field={passwordField} label={passwordField.value === '' ? "Password" : ""}
                                    type={"password"}/>
                    <Button type={"submit"} style={{marginTop: 10}}>Save</Button>
                </div>
            </form>
        </Dialog>
    );
};

export default UpdateEmailDialog;
