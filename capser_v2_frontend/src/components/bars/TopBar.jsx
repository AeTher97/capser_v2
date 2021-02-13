import React from 'react';
import {AppBar, IconButton, Toolbar, Typography} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import makeStyles from "@material-ui/core/styles/makeStyles";
import MenuIcon from '@material-ui/icons/Menu';

const MobileTopBar = ({open, setOpen}) => {

    const classes = useStyles();
    return (
        <AppBar position={"sticky"} className={classes.bar}>
            <Toolbar variant={"dense"}>
                <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu" onClick={() => setOpen(!open)}>
                    <MenuIcon/>
                </IconButton>

            </Toolbar>
        </AppBar>
    );
};

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    bar: {
        backgroundColor: theme.palette.background.paper,
        color: "white",
        zIndex: 9999
    }
}));

export default MobileTopBar;
