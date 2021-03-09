import React, {useEffect, useState} from 'react';
import Drawer from "@material-ui/core/Drawer";
import {Divider, Typography} from "@material-ui/core";
import HomeOutlinedIcon from '@material-ui/icons/HomeOutlined';
import {useHistory} from "react-router-dom";
import ExitToAppOutlinedIcon from '@material-ui/icons/ExitToAppOutlined';
import {makeStyles} from "@material-ui/core/styles";
import {useDispatch, useSelector} from "react-redux";
import {logoutAction} from "../../redux/actions/authActions";
import {useHasRole} from "../../utils/SecurityUtils";
import CheckIcon from '@material-ui/icons/Check';
import {DoublesIcon, EasyIcon, SinglesIcon, UnrankedIcon} from "../../misc/icons/CapsIcons";
import BellComponent from "./BellComponent";
import AccountBoxOutlinedIcon from '@material-ui/icons/AccountBoxOutlined';
import PeopleOutlineIcon from '@material-ui/icons/PeopleOutline';
import GavelIcon from '@material-ui/icons/Gavel';
import mainStyles from "../../misc/styles/MainStyles";
import BoldTyphography from "../misc/BoldTyphography";
import {useXtraSmallSize} from "../../utils/SizeQuery";
import AccountTreeOutlinedIcon from '@material-ui/icons/AccountTreeOutlined';

const SideBar = ({open, setOpen}) => {

    const history = useHistory();
    const dispatch = useDispatch();
    const hasRole = useHasRole();
    const {email, username} = useSelector(state => state.auth)
    const small = useXtraSmallSize();
    const [state, setState] = useState({
        expanded: false,
        width: 44
    });


    useEffect(() => {
        setState({
            expanded: open && small,
            width: open && small ? 300 : 44
        });
        if (!small) {
            setOpen(false);
        }
    }, [open, small])


    const classes = useStyle();
    const mainStyles0 = mainStyles();


    const icons = [
        {
            tooltip: "Homepage",
            link: "/",
            icon: <HomeOutlinedIcon/>
        },
        {
            tooltip: username,
            link: "/secure/profile",
            icon: <AccountBoxOutlinedIcon/>,
            role: 'USER'

        },
        {
            tooltip: "Tournaments",
            link: "/tournaments",
            icon: <AccountTreeOutlinedIcon/>
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

        },

    ]

    const go = (address) => {
        history.push(address);
        setState({
            expanded: open,
            width: 44
        })
        setOpen(false);
    }

    return (

        <Drawer variant={"persistent"} open={!small || open}
                onMouseEnter={() => {
                    setState({
                        expanded: true,
                        width: 300
                    })
                }} onMouseLeave={() => {
            setState({
                expanded: open,
                width: 44
            })
        }}>
            {small && <div style={{height: 52}}/>}
            <div style={{width: state.width, overflow: "hidden"}} className={classes.expanding}>
                {!small && <div onClick={() => {
                    history.push("/")
                }}
                                style={{display: "flex", flexDirection: "row", justifyContent: "center"}}
                >
                    <img src={"/logo192.png"} style={{maxWidth: 38, padding: 3, cursor: "pointer", minHeight: 38}}/>
                </div>}
                {hasRole('USER') && <div>
                    <BellComponent expanded={state.expanded}/>
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
                             style={{paddingRight: 0}} onClick={() => go(icon.link)}>
                            {icon.icon !== 10 && <div style={{padding: 10}}> {icon.icon}</div>}
                            {icon.icon === 10 &&
                            <div style={{padding: 4, paddingLeft: 11, paddingRight: 10}}>
                                <Typography variant={"h6"} color={"inherit"}>
                                    {icon.icon}
                                </Typography>
                            </div>}
                            <div style={{ transition: "all 0,2s"}}>
                                <BoldTyphography noWrap color={"inherit"}>{icon.tooltip}</BoldTyphography>
                            </div>
                        </div>
                    )
                })}
                {hasRole('USER') || hasRole('ADMIN') ?
                    <>
                        <Divider/>
                        <div className={[classes.redHover, mainStyles0.centeredRowNoFlex].join(' ')} onClick={() => {
                            dispatch(logoutAction())
                            history.push('/')
                        }}>
                            <div style={{padding: 11}}><ExitToAppOutlinedIcon style={{transform: 'scale(-1,1)'}}/></div>
                            <div style={{ transition: "all 0,2s"}}>
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
                            <div style={{ transition: "all 0,2s"}}>
                                <BoldTyphography noWrap color={"inherit"}>Login</BoldTyphography>
                            </div>
                        </div>
                    </>
                }
            </div>
        </Drawer>
    );
};

const useStyle = makeStyles(() => ({
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
