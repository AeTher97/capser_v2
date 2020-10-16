import React from 'react';
import PageHeader from "../../misc/PageHeader";
import mainStyles from "../../../misc/styles/MainStyles";
import {Typography} from "@material-ui/core";
import PropTypes from 'prop-types';
import {makeStyles} from "@material-ui/core/styles";
import {useXtraSmallSize} from "../../../utils/SizeQuery";


const TenCommandments = () => {

    const classes = mainStyles();
    const styles = useStyles();

    const xSmall = useXtraSmallSize();

    const commandments = [
        {text: "A good caps player can hit any target."},
        {text: "A good caps player can play on any side."},
        {text: "A good caps player can play in any venue."},
        {text: "A good caps player plays better when drunk."},
        {text: "A good caps player rebuttles."},
        {text: "A good caps player is not afraid of drinking beer."},
        {text: "A good caps player celebrates after scoring."},
        {text: "A good caps player is a gentleman."},
        {text: "A good caps player loves the game of caps."},
        {text: "This is a game of caps we play with 3 beers."}
    ]

    let index = 0;

    return (
        <div>
            <PageHeader title={"10 Commandments"} noSpace/>
            <div className={classes.squareShine}>

                <div className={!xSmall ? classes.root : null}>
                    <div className={classes.leftOrientedWrapperNoPadding}>
                        {commandments.map(commandment => {
                            index++;
                            return (
                                <div className={classes.header} key={commandment.text}>
                                    <OutlinedNumber value={index}/>
                                    <Typography
                                        variant={xSmall ? null : "h5"}>{commandment.text}</Typography>
                                </div>)
                        })}
                    </div>
                </div>
            </div>

        </div>
    );
};


const OutlinedNumber = ({value}) => {
    const styles = useStyles();


    return (
        <div className={styles.circle}>
            <Typography color={"primary"} variant={"h5"}>{value}</Typography>
        </div>
    );
};

OutlinedNumber.propTypes = {
    value: PropTypes.number.isRequired
};

const useStyles = makeStyles(theme => ({
    circle: {
        borderRadius: 100,
        minWidth: 40,
        minHeight: 40,
        borderColor: theme.palette.primary.main,
        borderWidth: 2,
        borderStyle: "solid",
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        margin: 15
    }
}))

export default TenCommandments;
