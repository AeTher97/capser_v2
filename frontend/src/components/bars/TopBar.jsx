import React from 'react';
import {AppBar, IconButton, Toolbar} from "@material-ui/core";
import makeStyles from "@material-ui/core/styles/makeStyles";
import MenuIcon from '@material-ui/icons/Menu';

const MobileTopBar = ({open, setOpen}) => {

    const classes = useStyles();
    return (
        <AppBar position={"sticky"} className={classes.bar}>
            <Toolbar variant={"dense"}>
                <IconButton edge="start" color="inherit" aria-label="menu"
                            onClick={() => setOpen(!open)}>
                    <MenuIcon/>
                </IconButton>
                <img alt={'Gcl wordmark logo'} style={{height: 32, marginTop: 2}} src={'/gcl_logo_wordmark.svg'}/>
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
