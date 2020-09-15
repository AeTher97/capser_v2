import React from 'react';
import {Divider, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import MenuItem from "@material-ui/core/MenuItem";

const NotificationList = ({notifications}) => {

    const mainClasses = mainStyles();

    const getDescription = (type) => {
        switch (type) {
            case     'ACCEPT_REQUEST' :
                return 'Accept game'
            case     'GAME_ACCEPTED' :
                return 'Game accepted'
            case      'GAME_REJECTED':
                return 'Game rejected'
        }
    }

    return notifications.map(notification => {
        return (
            <MenuItem key={notification.id}>
                <div style={{display: 'flex', flexDirection: 'column'}}>
                <div className={mainClasses.header}>
                    <Typography color={"primary"} style={{flex: 1}}
                                variant={"body2"}>{getDescription(notification.notificationType)}</Typography>
                    <Typography variant={"caption"}>{new Date(notification.date).toUTCString()}</Typography>
                </div>
                <div className={mainClasses.header}>
                    <Typography variant={"body2"}>{notification.text}</Typography>
                    <Divider/>
                </div>
                </div>
            </MenuItem>)
    })


};

const notificationListStyle = makeStyles(theme => ({
    container: {
        maxWidth: 400,
        padding: 10
    }
}))
export default NotificationList;
