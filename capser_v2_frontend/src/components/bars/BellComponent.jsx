import React, {useEffect} from 'react';
import {useNotifications} from "../../data/NotificationData";
import {Badge, IconButton} from "@material-ui/core";
import NotificationsIcon from "@material-ui/icons/Notifications";
import Menu from "@material-ui/core/Menu";
import NotificationList from "./NotificationList";
import {makeStyles} from "@material-ui/core/styles";

const BellComponent = () => {

    const {notSeen, notifications, markSeen} = useNotifications();

    const [anchorEl, setAnchorEl] = React.useState(null);
    const classes = useStyle();


    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        notifications.forEach(notification => {
            if (notification.seen === false) {
                markSeen(notification.id)
            }
        })
        setAnchorEl(null);
    };

    return (
        <>
            <IconButton className={classes.iconButton} onClick={handleClick}>
                {notSeen ?
                    <Badge color={"primary"} badgeContent={5} variant={"dot"}>
                        <NotificationsIcon/>
                    </Badge> : <NotificationsIcon/>}
            </IconButton>
            <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose}>
                <div>
                    <NotificationList notifications={notifications}/>
                </div>
            </Menu>
        </>
    );
};

const useStyle = makeStyles(theme => ({
    iconButton: {
        '&:hover': {
            color: 'red'
        }
    }
}))

export default BellComponent;