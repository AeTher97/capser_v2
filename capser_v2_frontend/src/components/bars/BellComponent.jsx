import React from 'react';
import {useNotifications} from "../../data/NotificationData";
import {Badge, IconButton, Link, Typography} from "@material-ui/core";
import NotificationsIcon from "@material-ui/icons/Notifications";
import Menu from "@material-ui/core/Menu";
import NotificationList from "./NotificationList";
import {makeStyles} from "@material-ui/core/styles";
import NotificationsOutlinedIcon from '@material-ui/icons/NotificationsOutlined';
import mainStyles from "../../misc/styles/MainStyles";
import BoldTyphography from "../misc/BoldTyphography";
import ExitToAppOutlinedIcon from "@material-ui/icons/ExitToAppOutlined";
import {useXtraSmallSize} from "../../utils/SizeQuery";

const BellComponent = ({expanded = true}) => {

    const {notSeen, notifications, markSeen} = useNotifications();

    const [anchorEl, setAnchorEl] = React.useState(null);
    const classes = useStyle();

    const small = useXtraSmallSize();
    const mainStyles0 = mainStyles()

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        markSeen();
        setAnchorEl(null);
    };

    return (
        <div>
            <div className={[mainStyles0.centeredRowNoFlex, classes.redHover].join(' ')} onClick={handleClick}>
                {notSeen ?
                    <Badge color={"primary"} badgeContent={5} variant={"dot"}>
                        <div style={{padding: 9}}><NotificationsIcon/></div>
                    </Badge> : <div style={{padding: 11}}><NotificationsOutlinedIcon/></div>}
                <div style={{opacity: expanded ? 1 : 0, transition: "all 0,2s"}}>
                    <BoldTyphography noWrap color={"inherit"}>Notifications</BoldTyphography>
                </div>
            </div>
            <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose}
                  PaperProps={{
                      style: {
                          transform: small ?  'translateX(3%) translateY(10%)': 'translateX(6%) translateY(0)',
                      }
                  }}>
                <div className={mainStyles0.standardBorder}>
                    <NotificationList notifications={notifications}/>
                    <div style={{padding: 10}}>
                        <Link onClick={handleClose}>Hide</Link>
                    </div>
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
    },
    redHover: {
        "&:hover": {
            color: 'red'
        },
        cursor: "pointer"
    }

}))

export default BellComponent;
