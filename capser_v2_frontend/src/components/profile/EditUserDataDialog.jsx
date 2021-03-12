import React from 'react';
import useFieldValidation from "../../utils/useFieldValidation";
import {validateEmail, validateLength} from "../../utils/Validators";
import ValidatedField from "../misc/ValidatedField";
import {Button, Dialog, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import {useSelector} from "react-redux";

const EditUserDataDialog = ({open, setOpen, editData, data}) => {
    const {email} = useSelector(state => state.auth);

    const usernameField = useFieldValidation(data.username, (word) => () => validateLength(word, 3))
    const emailField = useFieldValidation(email || '', validateEmail)
    usernameField.showError = true;
    emailField.showError = true;

    const classes = mainStyles();
    const saveData = () => {

        if (usernameField.validate()() || emailField.validate()) {
            return;
        }
        console.log("saving")
        editData({
            email: emailField.value,
            username: usernameField.value
        })
        setOpen(false);
    }

    return (
        <Dialog open={open}>
            <div className={classes.standardBorder} style={{margin: 0}}>
                <Typography variant={"h5"}>Change account data</Typography>
                <ValidatedField field={usernameField} label={usernameField.value === "" ? "Username" : ""}/>
                <ValidatedField field={emailField} label={emailField.value === "" ? "Email" : ""}/>
                <div style={{marginTop: 10}}>
                    <Button style={{marginRight: 5}} onClick={saveData}>Save</Button>
                    <Button variant={"outlined"} onClick={() => setOpen(false)}>Cancel</Button>
                </div>
            </div>
        </Dialog>
    );
};

export default EditUserDataDialog;
