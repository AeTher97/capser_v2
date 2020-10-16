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
            Promise.all(response.data.map(game => {
                if (game.gameType === 'DOUBLES') {
                    return [fetchTeamName(game.team1DatabaseId), fetchTeamName(game.team2DatabaseId)];
                }
                return [fetchUsername(game.player1), fetchUsername(game.player2)]
            }).flat()).then((value) => {
                if(shouldUpdate) {
                    value = value.map(o => o.data);
                    setGames(response.data.map(game => {
                        if (game.gameType === 'DOUBLES') {
                            game.team1Name = value.find(o => o.id === game.team1DatabaseId);
                            game.team2Name = value.find(o => o.id === game.team2DatabaseId);
                            return game;
                        } else {
                            game.player1Name = value.find(o => o.id === game.player1);
                            game.player2Name = value.find(o => o.id === game.player2);
                            return game;
                        }
                    }))
                }
            }).finally(() => {
                if(shouldUpdate){
                    setGamesLoading(false);
                }
            })
        }).catch(e => {
            dispatch(showError("Failed loading games"))
        })

        return () => {shouldUpdate =false};
    };

    const fetchBlogPosts = () => {
        let shouldUpdate = true;
        axios.get(`/dashboard/posts`).then(result => {
            if(shouldUpdate) {
                setPosts(result.data);
            }
        }).catch(e => {
            dispatch(showError("Failed loading blog"))
        }).finally(() => {
            if(shouldUpdate){
                setPostsLoading(false);
            }
        })
        return () => {
            shouldUpdate=false;
        }
    };

    useEffect(() => {
        const unsubscribe1 = fetchBlogPosts();
        const unsubscribe2=fetchDashboardGames();
        return () =>{
            unsubscribe1();
            unsubscribe2();
        }
    }, [])

    return {games, posts, postsLoading, gamesLoading}
}


