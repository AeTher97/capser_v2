import React, {useEffect, useRef, useState} from 'react';
import Drawer from "@material-ui/core/Drawer";
import {Divider, IconButton, Typography} from "@material-ui/core";
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
import GavelIcon from '@material-ui/icons/Gavel';
import mainStyles from "../../misc/styles/MainStyles";
import BoldTyphography from "../misc/BoldTyphography";
import {useXtraSmallSize} from "../../utils/SizeQuery";

const SideBar = ({open, setOpen}) => {

    const history = useHistory();
    const dispatch = useDispatch();
    const hasRole = useHasRole();
    const {email} = useSelector(state => state.auth)
    const [expanded, setExpanded] = useState(false);
    const [width, setWidth] = useState(44);
    const small = useXtraSmallSize();


    useEffect(() => {
        setExpanded(open && small);
        if (!small) {
            setOpen(false);
        }
    }, [open, small])


    const classes = useStyle();
    const mainStyles0 = mainStyles();

    useEffect(() => {
        if (expanded) {
            setWidth(300);
        } else {
            setWidth(44);
        }
    }, [expanded])

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
        {
            tooltip: 'Commandments',
            link: '/10commandments',
            icon: 10

        },
        {
            tooltip: 'Rules',
            link: '/rules',
            icon: <GavelIcon/>

        }
        // {
        //     tooltip: email,
        //     link: "/secure/profile",
        //     icon: <AccountBoxOutlinedIcon/>,
        //     role: 'USER'
        //
        // },
    ]

    const go = (address) => {
        history.push(address);
        setExpanded(open);
        setOpen(false);
    }


    return (

        <Drawer variant={"persistent"} style={{position: "absolute"}} open={!small || open}
                onMouseEnter={() => setExpanded(true)} onMouseLeave={() => setExpanded(open)}>
            {small && <div style={{height: 10}}/>}
            <div style={{maxWidth: width, overflow: "hidden"}} className={classes.expanding}>
                <div onClick={() => {
                    history.push("/")
                }}>
                    <img src={"/logo192.png"} style={{maxWidth: 38, padding: 3, cursor: "pointer"}}/>
                </div>
                {hasRole('USER') && <div>
                    <BellComponent expanded={expanded}/>
                </div>}
                <Divider/>
                {icons.filter(icon => {
                    if (icon.role) {
                        return hasRole(icon.role)
                    } else {
                        return true
                    }
                }).map(icon => {
                    return (
                        <div key={icon.link} className={[mainStyles0.centeredRowNoFlex, classes.redHover].join(' ')}
                             style={{paddingRight: expanded ? 100 : 0}} onClick={() => go(icon.link)}>
                            {icon.icon !== 10 && <div style={{padding: 10}}> {icon.icon}</div>}
                            {icon.icon === 10 &&
                            <div style={{padding: 4, paddingLeft: 11, paddingRight: 9}}><Typography
                                variant={"h6"}> {icon.icon}</Typography></div>}
                            <div style={{opacity: expanded ? 1 : 0, transition: "all 0,2s"}}>
                                <BoldTyphography noWrap color={"inherit"}>{icon.tooltip}</BoldTyphography>
                            </div>
                        </div>
                    )
                })}
                {hasRole('USER') ?
                    <>
                        <Divider/>
                        <div className={[classes.redHover, mainStyles0.centeredRowNoFlex].join(' ')} onClick={() => {
                            dispatch(logoutAction())
                            history.push('/')
                        }}>
                            <div style={{padding: 9}}><ExitToAppOutlinedIcon style={{transform: 'scale(-1,1)'}}/></div>
                            <div style={{opacity: expanded ? 1 : 0, transition: "all 0,2s"}}>
                                <BoldTyphography noWrap color={"inherit"}>Logout</BoldTyphography>
                            </div>
                        </div>
                    </> :
                    <>
                        <Divider/>
                        <div className={[classes.redHover, mainStyles0.centeredRowNoFlex].join(' ')} onClick={() => {
                            history.push('/login')
                        }}>
                            <div style={{padding: 11}}><ExitToAppOutlinedIcon/></div>
                            <div style={{opacity: expanded ? 1 : 0, transition: "all 0,2s"}} s>
                                <BoldTyphography noWrap color={"inherit"}>Login</BoldTyphography>
                            </div>
                        </div>
                    </>
                }
            </div>
        </Drawer>
    );
};

const useStyle = makeStyles(theme => ({
    iconButton: {
        '&:hover': {
            color: 'red'
        },
        fontSize: 20
    },
    expanding: {
        transition: "all 0.2s",
        overflow: "hidden"
    },
    redHover: {
        "&:hover": {
            color: 'red'
        },
        cursor: "pointer"
    }
}))


export default SideBar;
