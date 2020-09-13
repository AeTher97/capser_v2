import React from 'react';
import {Divider, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

const NotificationList = ({notifications}) => {

    const classes = notificationListStyle();

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

    return (
        <div className={classes.container}>
            {notifications.map(notification => {
                return (
                    <div key={notification.id}>
                        <Typography color={"primary"} variant={"body2"}>{getDescription(notification.notificationType)}</Typography>
                        <Typography variant={"body2"}>{notification.text}</Typography>
                        <Divider/>
                    </div>)
            })}
        </div>
    );
};

NotificationList.propTypes = {};

const notificationListStyle = makeStyles(theme => ({
    container: {
        maxWidth: 400,
        padding: 10
    }
}))
export default NotificationList;
