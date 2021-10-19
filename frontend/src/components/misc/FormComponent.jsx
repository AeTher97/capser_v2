import React from 'react';
import CenteredColumn from "./CenteredColumn";
import {Typography} from "@material-ui/core";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Button from "@material-ui/core/Button";
import PropTypes from 'prop-types';
import ValidatedField from "./ValidatedField";


const FormComponent = ({title, fields, secondaryTitle, onSubmit, buttonText, stretchButton, error, labels}) => {

    const classes = useStyles();
    return (
        <form onSubmit={onSubmit} style={{width: "100%"}}>
            <CenteredColumn>
                <div className={classes.header}>
                    <Typography variant={"h5"}
                                style={{textAlign: "center", whiteSpace: "pre-line"}}>{title}</Typography>
                </div>
                {secondaryTitle && <Typography variant={"subtitle2"}>{secondaryTitle}</Typography>}
                <div className={classes.error}>
                    <Typography style={{color: "red"}} variant={"caption"}>{error ? error : " "}</Typography>
                </div>

                {fields && fields.map(field => {
                    return <div key={field.label} className={classes.field}>
                        {labels && <Typography className={classes.entryHeading}>{field.label}</Typography>}
                        <ValidatedField
                            label={field.validation.value ? '' : field.label}
                            field={field.validation}
                            type={field.type}
                            disabled={field.disabled}
                        />
                    </div>
                })}

                {onSubmit && <Button fullWidth={stretchButton} type={"submit"}
                                     className={classes.button}>
                    {buttonText ? buttonText : 'Wyślij'}
                </Button>}
            </CenteredColumn>
        </form>
    );
};

FormComponent.propTypes = {
    title: PropTypes.string,
    fields: PropTypes.array.isRequired,
    secondaryTitle: PropTypes.string,
    onSubmit: PropTypes.func,
    buttonText: PropTypes.string,
    labels: PropTypes.bool

};

const useStyles = makeStyles(theme => ({
    card: {
        padding: theme.spacing(2),
    },
    cardTitle: {
        fontSize: 18,
        padding: 10,
    },
    header: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 0
    },
    field: {
        flex: 1,
        margin: 10,
        width: '100%',
        display: "flex",
        flexDirection: "row"
    },
    button: {
        marginTop: 30
    },
    buttonStretched: {
        marginTop: 30,
    },
    error: {
        marginTop: 5,
        minHeight: theme.typography.h5.fontSize
    }
}));

export default FormComponent;
