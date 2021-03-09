import React, {useEffect, useState} from 'react';
import {Button, Dialog, Typography} from "@material-ui/core";
import {useSelector} from "react-redux";
import mainStyles from "../misc/styles/MainStyles";
import useFieldValidation from "./useFieldValidation";
import {validateEmail} from "./Validators";
import ValidatedField from "../components/misc/ValidatedField";
import {useUserData} from "../data/UserData";

const UpdateEmailDialog = () => {

    const {email, username, userId} = useSelector(state => state.auth);
    const [open, setOpen] = useState(false);
    const emailField = useFieldValidation("", validateEmail);
    const {updateUserData} = useUserData(userId);
    emailField.showError = true;
    const classes = mainStyles();

    useEffect(() => {
        setOpen(!!(username && (email === undefined || email === "null" || email === "undefined")));
    }, [username, email])

    const updateEmail = () => {
        if (emailField.validate()) {
            return;
        }

        updateUserData({email: emailField.value});


    }

    return (
        <Dialog open={open}>
            <div className={classes.standardBorder} style={{margin: 0}}>
                <Typography variant={"h5"}>Emails are being added to GCL</Typography>
                <Typography>Please type in your email</Typography>
                <ValidatedField field={emailField} label={""}/>
                <Button onClick={updateEmail} style={{marginTop: 10}}>Save</Button>
            </div>
        </Dialog>
    );
};

export default UpdateEmailDialog;
