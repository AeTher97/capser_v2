import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useSelector} from "react-redux";


export const useNotifications = () => {
    const {accessToken} = useSelector(state => state.auth);

    const [notifications, setNotifications] = useState([]);
    const [notSeen, setNotSeen] = useState(false);

    const intervalTime = 10000;


    const getAccessToken = () => {
        return accessToken;
    }

    const fetchNotifications = () => {
        console.log("fetch")
        axios.get(`/notifications`, {
            headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        }).then(data => {
            setNotifications(data.data);
        }).catch(e => {
            console.error(e.message);
        })
    }

    useEffect(() => {
        fetchNotifications();
        const interval = setInterval(fetchNotifications, intervalTime);
        return () => {
            console.log("clear")
            clearInterval(interval);
        };
    }, [accessToken])

    useEffect(() => {
        if (notifications.map(notification => {
            return notification.seen;
        }).filter(boolean => !boolean).length > 0) {
            setNotSeen(true);
        } else {
            if (notSeen) {
                setNotSeen(false);
            }
        }
    }, [notifications]);

    const markSeen = (notificationId) => {
        axios.put(`/notifications/seen/${notificationId}`, null, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then(result => {
            const copy = notifications.slice();
            setNotifications(copy.map(note => {
                if (note.id === result.data.id) {
                    return result.data;
                } else {
                    return note;
                }
            }));
        })
    }

    return {notifications: notifications, markSeen: markSeen, notSeen: notSeen}
}



