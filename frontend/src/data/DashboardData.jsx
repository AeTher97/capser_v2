import axios from "axios";
import {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {showError} from "../redux/actions/alertActions";

export const useDashboard = () => {

    const dispatch = useDispatch();
    const [postsLoading, setPostsLoading] = useState(true);
    const [gamesLoading, setGamesLoading] = useState(true);
    const [games, setGames] = useState([]);
    const [posts, setPosts] = useState([]);

    const fetchTeamName = (id) => {
        return axios.get(`/teams/name/${id}`);
    }

    const fetchUsername = (id) => {
        return axios.get(`/users/${id}`)
    }


    const fetchDashboardGames = () => {
        let shouldUpdate = true;
        axios.get(`/dashboard/games`).then(response => {

                setGames(response.data)
            }
        ).finally(() => {
            if (shouldUpdate) {
                setGamesLoading(false);
            }
        }).catch(e => {
            dispatch(showError("Failed loading games"))
        })

        return () => {
            shouldUpdate = false
        };
    };

    const fetchBlogPosts = () => {
        let shouldUpdate = true;
        axios.get(`/dashboard/posts`).then(result => {
            if (shouldUpdate) {
                setPosts(result.data);
            }
        }).catch(e => {
            dispatch(showError("Failed loading blog"))
        }).finally(() => {
            if (shouldUpdate) {
                setPostsLoading(false);
            }
        })
        return () => {
            shouldUpdate = false;
        }
    };

    useEffect(() => {
        const unsubscribe1 = fetchBlogPosts();
        const unsubscribe2 = fetchDashboardGames();
        return () => {
            unsubscribe1();
            unsubscribe2();
        }
    }, [])

    return {games, posts, postsLoading, gamesLoading}
}


