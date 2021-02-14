import React from 'react';
import PropTypes from 'prop-types';
import Dialog from "@material-ui/core/Dialog";
import {Button, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";

const YesNoDialog = ({onYes, onNo, question, open, setOpen}) => {

    const classes = mainStyles();

    const handleYes = () => {
        onYes();
        setOpen(false);
    }

    const handleNo = () => {
        onNo();
        setOpen(false);
    }

    return (
        <Dialog open={open}>
            <div className={[classes.paddedContent, classes.standardBorder].join(' ')}  style={{backgroundColor: 'black'}}>
            <Typography variant={"h5"}>{question}</Typography>
            <div className={classes.header} style={{marginTop: 20}}>
                <Button onClick={handleYes} style={{marginRight: 10}}>
                    Yes
                </Button>
                <Button onClick={handleNo}>
                    No
                </Button>
            </div>
            </div>
        </Dialog>
    );
};

YesNoDialog.propTypes = {
    onYes: PropTypes.func.isRequired,
    onNo: PropTypes.func.isRequired,
    question: PropTypes.string.isRequired,
    open: PropTypes.bool.isRequired,
    setOpen: PropTypes.func.isRequired
};

export default YesNoDialog;
