import React from 'react';
import {Divider, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import MenuItem from "@material-ui/core/MenuItem";
import {useHistory} from "react-router-dom";
import {useNotifications} from "../../data/NotificationData";


const NotificationList = ({notifications}) => {

    const mainClasses = mainStyles();
    const notificationListClasses = notificationListStyle();
    const history = useHistory();

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

    if (notifications.length > 0) {
        return notifications.map(notification => {
            return (
                <MenuItem key={notification.id}
                          className={notification.seen ? notificationListClasses.seen : notificationListClasses.unseen}
                          onClick={() => {
                              if (notification.notificationType === 'ACCEPT_REQUEST') {
                                  history.push('/secure/acceptance')
                              }
                          }}>
                    <div style={{display: 'flex', flexDirection: 'column', flex: 1}}>
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
    } else {
        return <MenuItem className={notificationListClasses.seen}>
            <Typography variant={"body2"}>No notifications</Typography>
        </MenuItem>
    }


};

const notificationListStyle = makeStyles(theme => ({
    seen: {
        minWidth: 400,
        padding: 10
    },
    unseen: {
        minWidth: 400,
        padding: 10,
        backgroundColor: 'rgba(255,255,255,0.2)'
    }

}))
export default NotificationList;
