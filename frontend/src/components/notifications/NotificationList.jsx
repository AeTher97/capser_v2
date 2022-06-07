import React from 'react';
import {Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import MenuItem from "@material-ui/core/MenuItem";
import {useHistory} from "react-router-dom";
import {useWindowSize} from "../../utils/UseSize";


const NotificationList = ({notifications, markSeen}) => {

    const mainClasses = mainStyles();
    const notificationListClasses = notificationListStyle();
    const history = useHistory();
    const size = useWindowSize();

    const getDescription = (type) => {
        switch (type) {
            case     'ACCEPT_REQUEST' :
                return 'Accept game'
            case     'GAME_ACCEPTED' :
                return 'Game accepted'
            case      'GAME_REJECTED':
                return 'Game rejected'
            case 'ACHIEVEMENT_UNLOCKED':
                return 'Achievement unlocked!'
        }
    }

    if (notifications.length > 0) {
        return notifications.map(notification => {
            return (
                <MenuItem key={notification.id}
                          className={[notification.seen ? notificationListClasses.seen : notificationListClasses.unseen, mainClasses.twichHighlight].join(' ')}
                          style={{
                              width: size.width < 400 ? size.width - 60 : 350,
                          }}
                          onClick={() => {
                              if (notification.notificationType === 'ACCEPT_REQUEST') {
                                  markSeen();
                                  history.push('/secure/acceptance')
                              }
                          }}>
                    <div style={{display: 'flex', flexDirection: 'column', flex: 1, width: '100%'}}>
                        <div className={mainClasses.header}>
                            <Typography color={"primary"} style={{flex: 1}}
                                        variant={"body2"}>{getDescription(notification.notificationType)}</Typography>
                            <Typography
                                variant={"caption"}>{new Date(notification.date).toDateString()} {new Date(notification.date).getHours()}:{new Date(notification.date).getMinutes()}</Typography>
                        </div>
                        <div style={{whiteSpace: "normal"}}>
                            <Typography variant={"body2"}>
                                {notification.text}
                            </Typography>
                        </div>
                    </div>
                </MenuItem>)
        })
    } else {
        return <MenuItem className={notificationListClasses.seen}
                         style={{width: size.width < 400 ? size.width - 30 : 350}}>
            <Typography variant={"body2"}>No notifications</Typography>
        </MenuItem>
    }


};

const notificationListStyle = makeStyles(() => ({
    seen: {
        padding: 10
    },
    unseen: {
        padding: 10,
        backgroundColor: 'rgba(255,255,255,0.2)'
    }

}))
export default NotificationList;
