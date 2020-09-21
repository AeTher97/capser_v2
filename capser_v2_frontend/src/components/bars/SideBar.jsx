import React, {useEffect, useState} from 'react';
import Drawer from "@material-ui/core/Drawer";
import {Badge, Divider, IconButton} from "@material-ui/core";
import HomeOutlinedIcon from '@material-ui/icons/HomeOutlined';
import Tooltip from "@material-ui/core/Tooltip";
import {useHistory} from "react-router-dom";
import AccountBoxOutlinedIcon from '@material-ui/icons/AccountBoxOutlined';
import ExitToAppOutlinedIcon from '@material-ui/icons/ExitToAppOutlined';
import {makeStyles} from "@material-ui/core/styles";
import {useDispatch} from "react-redux";
import {logoutAction} from "../../redux/actions/authActions";
import {useHasRole} from "../../utils/SecurityUtils";
import NotificationsIcon from '@material-ui/icons/Notifications';
import useNotificationFetch, {useMarkNotificationAsSeen} from "../../data/NotificationData";
import Menu from "@material-ui/core/Menu";
import NotificationList from "./NotificationList";
import CheckIcon from '@material-ui/icons/Check';
import {showError} from "../../redux/actions/alertActions";
import {DoublesIcon, EasyIcon, SinglesIcon, UnrankedIcon} from "../../misc/icons/CapsIcons";

const SideBar = () => {

    const history = useHistory();
    const dispatch = useDispatch();
    const hasRole = useHasRole();

    const getNotifications = useNotificationFetch();
    const markAsSeen = useMarkNotificationAsSeen();

    const [anchorEl, setAnchorEl] = React.useState(null);
    const [notifications, setNotifications] = useState([]);
    const [showBadge, setShowBadge] = useState(false);

    const sendSeen = () => {
        return new Promise((resolve) => {
            notifications.forEach(notification => {
                if (!notification.seen) {
                    markAsSeen(notification.id).catch(() => {
                        dispatch(showError("Error occurred with notifications"));
                    })
                }
            })
            setShowBadge(false);
            resolve();
        })
    }

    const updateNotifications = () => {
        if (hasRole('USER')) {
            getNotifications().then(data => {
                    setNotifications(data.data)

                    if (data.data.map(notification => {
                        return notification.seen;
                    }).filter(boolean => !boolean).length > 0) {
                        setShowBadge(true);
                    } else {
                        setShowBadge(false);
                    }
                }
            );
        }
    }

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
        sendSeen().then(r => {})
    };

    const handleClose = () => {
        setAnchorEl(null);
        updateNotifications();
    };

    useEffect(() => {
        updateNotifications();
    }, [])

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
            icon:   <UnrankedIcon/>
        },
        {
            tooltip: "Doubles",
            link: "/doubles",
            icon:  <DoublesIcon/>
        },
        {
            tooltip: "Games Accepting",
            link: "/secure/acceptance",
            icon: <CheckIcon/>,
            role: 'USER'
        },
        {
            tooltip: "Profile",
            link: "/secure/profile",
            icon: <AccountBoxOutlinedIcon/>,
            role: 'USER'

        },

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
                {hasRole('USER') && <Tooltip title={"Notifications"} placement={"right"}>
                    <IconButton className={classes.iconButton} onClick={handleClick}>
                        {showBadge ? <Badge color={"primary"} badgeContent={5} variant={"dot"}>
                            <NotificationsIcon/>
                        </Badge> : <NotificationsIcon/>}
                    </IconButton>
                </Tooltip>}
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
                {hasRole('USER') &&
                <>
                    <Divider/>
                    <Tooltip title={"Logout"} placement={"right"}>
                        <IconButton className={classes.iconButton} onClick={() => {
                            dispatch(logoutAction())
                            history.push('/')
                        }}>
                            <ExitToAppOutlinedIcon/>
                        </IconButton>
                    </Tooltip></>
                }
            </Drawer>

            <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose}>
                <div>
                    <NotificationList notifications={notifications}/>
                </div>
            </Menu>
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
