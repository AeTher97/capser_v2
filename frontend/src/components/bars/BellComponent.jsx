import React, {useEffect} from 'react';
import {useNotifications} from "../../data/NotificationData";
import {Badge, Link, MenuItem} from "@material-ui/core";
import NotificationsIcon from "@material-ui/icons/Notifications";
import Menu from "@material-ui/core/Menu";
import NotificationList from "./NotificationList";
import {makeStyles} from "@material-ui/core/styles";
import NotificationsOutlinedIcon from '@material-ui/icons/NotificationsOutlined';
import mainStyles from "../../misc/styles/MainStyles";
import BoldTyphography from "../misc/BoldTyphography";
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

    useEffect(() => {
        if (!expanded) {
            setAnchorEl(null);
        }
    }, [expanded])

    const handleClose = () => {
        markSeen();
        setAnchorEl(null);
    };

    return (
        <div>
            <div className={[mainStyles0.centeredRowNoFlex, classes.redHover, mainStyles0.twichHighlight].join(' ')}
                 onClick={handleClick}>
                {notSeen ?
                    <div style={{padding: '1px 0px 1px 5px', marginRight: 11}}>
                        <Badge color={"primary"} badgeContent={5} variant={"dot"}>
                            <NotificationsIcon/>
                        </Badge></div> :
                    <div style={{padding: '1px 0px 1px 5px', marginRight: 11}}><NotificationsOutlinedIcon/></div>}
                <div style={{transition: "all 0,2s"}}>
                    <BoldTyphography noWrap color={"inherit"}>Notifications</BoldTyphography>
                </div>
            </div>
            <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose}
                  PaperProps={{
                      style: {
                          transform: small ? 'translateX(3%) translateY(15%)' : 'translateX(6%) translateY(0)',
                      }
                  }}>
                <div className={mainStyles0.standardBorder} style={{padding: 0, margin: 0}}>
                    <NotificationList notifications={notifications} markSeen={markSeen} handleClose={handleClose}/>
                    <MenuItem className={mainStyles0.twichHighlight} onClick={handleClose}>
                        <Link>Hide</Link>
                    </MenuItem>
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
