import {useSelector} from "react-redux";
import axios from "axios";
import {getRequestGameTypeString} from "../utils/Utils";
import {useEffect, useState} from "react";
import {fetchUsername} from "./UsersFetch";


export const useMultipleGamePost = (type) =>{
    const {accessToken} = useSelector(state => state.auth);

    const postGame = (gameRequest) => {
        return axios.post(`${getRequestGameTypeString(type)}`, gameRequest, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })
    };

    return {
        postGame
    }
}

export const useMultipleGames = (type, pageNumber = 0, pageSize = 10) => {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagesNumber, setPagesNumber] = useState(0);

    const fetchTeamName = (id) => {
        return axios.get(`/teams/name/${id}`);
    }

    useEffect(() => {
        setLoading(true);
        let shouldUpdate = true;
        axios.get(`/${getRequestGameTypeString(type)}?pageNumber=${pageNumber}&pageSize=${pageSize}`).then(response => {
            Promise.all(response.data.content.map(game => {
                if (shouldUpdate) {
                    setPagesNumber(response.data.totalPages)
                }
                return [fetchTeamName(game.team1DatabaseId), fetchTeamName(game.team2DatabaseId)]
            }).flat()).then((value) => {
                value = value.map(o => o.data);
                if (shouldUpdate) {
                    setGames(response.data.content.map(game => {
                        game.team1Name = value.find(o => o.id === game.team1DatabaseId);
                        game.team2Name = value.find(o => o.id === game.team2DatabaseId);
                        return game;
                    }))
                }
            }).finally(() => {
                if (shouldUpdate) {
                    setLoading(false);
                }
            })
        })

        return () => {
            shouldUpdate = false;
        }
    }, [pageNumber, pageSize, type])




    return {
        games: games,
        loading,
        pagesNumber
    }
}



export const useTeamGame = ( gameId) => {

    const [game, setGame] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        let shouldUpdate = true;
        setLoading(true);
        axios.get(`/doubles/${gameId}`).then(result => {
            if (shouldUpdate) {
                const game = result.data;


                        if (shouldUpdate) {

                            setGame(game);

                        }

            }
        }).catch(e => {
            console.log(e.message)
        }).finally(() => {
            if (shouldUpdate) {
                setLoading(false);
            }
        })

        return () => {
            shouldUpdate = false;
        }


    }, [gameId])

    return {game, loading}
}

