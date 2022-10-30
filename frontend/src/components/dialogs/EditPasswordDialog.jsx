import React from 'react';
import {Button, Dialog, Typography} from "@material-ui/core";
import ValidatedField from "../misc/ValidatedField";
import useFieldValidation from "../../utils/useFieldValidation";
import {validateLength} from "../../utils/Validators";
import mainStyles from "../../misc/styles/MainStyles";

const EditPasswordDialog = ({open, setOpen, editData}) => {

    const classes = mainStyles();

    const passwordField = useFieldValidation('', (word) => () => validateLength(word, 6))
    const newPasswordField = useFieldValidation('', (word) => () => validateLength(word, 6))

    const close = () => {
        passwordField.setValue('');
        newPasswordField.setValue('');
        setOpen(false);
    }

    const saveData = (e) => {
        e.preventDefault();
        if (passwordField.validate()() || newPasswordField.validate()()) {
            return;
        }
        editData({
            oldPassword: passwordField.value,
            newPassword: newPasswordField.value
        }).then(() => {
            setOpen(false);
            passwordField.setValue('');
        }).catch(() => {

        })
    }


    return (
        <Dialog open={open}>
            <form onSubmit={saveData}>
                <div className={classes.standardBorder} style={{margin: 0, maxWidth: 300}}>
                    <Typography variant={"h5"}>Change password</Typography>
                    <ValidatedField field={passwordField} label={passwordField.value === "" ? "Old Password" : ""}
                                    type={"password"}/>
                    <ValidatedField field={newPasswordField} label={newPasswordField.value === "" ? "New Password" : ""}
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

export default EditPasswordDialog;
