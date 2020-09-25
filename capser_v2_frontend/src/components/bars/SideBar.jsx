import React from 'react';
import Drawer from "@material-ui/core/Drawer";
import {Divider, IconButton} from "@material-ui/core";
import HomeOutlinedIcon from '@material-ui/icons/HomeOutlined';
import Tooltip from "@material-ui/core/Tooltip";
import {useHistory} from "react-router-dom";
import AccountBoxOutlinedIcon from '@material-ui/icons/AccountBoxOutlined';
import ExitToAppOutlinedIcon from '@material-ui/icons/ExitToAppOutlined';
import {makeStyles} from "@material-ui/core/styles";
import {useDispatch, useSelector} from "react-redux";
import {logoutAction} from "../../redux/actions/authActions";
import {useHasRole} from "../../utils/SecurityUtils";
import CheckIcon from '@material-ui/icons/Check';
import {DoublesIcon, EasyIcon, SinglesIcon, UnrankedIcon} from "../../misc/icons/CapsIcons";
import BellComponent from "./BellComponent";
import PeopleOutlineIcon from '@material-ui/icons/PeopleOutline';

const SideBar = () => {

    const history = useHistory();
    const dispatch = useDispatch();
    const hasRole = useHasRole();
    const {email} = useSelector(state => state.auth)


    const icons = [
        {
            tooltip: "Homepage",
            link: "/",
            icon: <HomeOutlinedIcon/>
        },
        {
            tooltip: "Singles",
            link: "/singles",
            icon: <SinglesIcon/>

        },
        {
            tooltip: "Easy Caps",
            link: "/easy",
            icon: <EasyIcon/>

        },
        {
            tooltip: "Unranked",
            link: "/unranked",
            icon: <UnrankedIcon/>
        },
        {
            tooltip: "Doubles",
            link: "/doubles",
            icon: <DoublesIcon/>
        },
        {
            tooltip: "Games Accepting",
            link: "/secure/acceptance",
            icon: <CheckIcon/>,
            role: 'USER'
        },
        {
            tooltip: "Teams",
            link: "/secure/teams",
            icon: <PeopleOutlineIcon/>,
            role: 'USER'
        },
        // {
        //     tooltip: email,
        //     link: "/secure/profile",
        //     icon: <AccountBoxOutlinedIcon/>,
        //     role: 'USER'
        //
        // },

    ]

    const classes = useStyle();

    return (
        <div>
            <Drawer variant={"permanent"}>
                <Tooltip title={"Global Caps League"} placement={"right"} onClick={() => {
                    history.push("/")
                }}>
                    <img src={"/logo192.png"} style={{maxWidth: 38, padding: 3, cursor: "pointer"}}/>
                </Tooltip>
                {hasRole('USER') && <div>
                    <BellComponent/>
                </div>}
                {icons.filter(icon => {
                    if (icon.role) {
                        return hasRole(icon.role)
                    } else {
                        return true
                    }
                }).map(icon => {
                    return (
                        <Tooltip key={icon.tooltip} title={icon.tooltip} placement={"right"}>
                            <IconButton className={classes.iconButton} onClick={() => {
                                history.push(icon.link)
                            }}>
                                {icon.icon}
                            </IconButton>
                        </Tooltip>
                    )
                })}
                {hasRole('USER') ?
                    <>
                        <Divider/>
                        <Tooltip title={"Logout"} placement={"right"}>
                            <IconButton className={classes.iconButton} onClick={() => {
                                dispatch(logoutAction())
                                history.push('/')
                            }}>
                                <ExitToAppOutlinedIcon style={{transform: 'scale(-1,1)'}}/>
                            </IconButton>
                        </Tooltip></> :
                    <>
                        <Divider/>
                        <Tooltip title={"Login"} placement={"right"}>
                            <IconButton className={classes.iconButton} onClick={() => {
                                history.push('/login')
                            }}>
                                <ExitToAppOutlinedIcon/>
                            </IconButton>
                        </Tooltip></>
                }
            </Drawer>


        </div>
    );
};

const useStyle = makeStyles(theme => ({
    iconButton: {
        '&:hover': {
            color: 'red'
        }
    }
}))


export default SideBar;
