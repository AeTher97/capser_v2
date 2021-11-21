import {useSelector} from "react-redux";
import axios from "axios";
import {getRequestGameTypeString} from "../utils/Utils";
import {useEffect, useState} from "react";


export const useMultipleGamePost = (type) => {
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

                if (shouldUpdate) {
                    setGames(response.data.content);
                }
            }).finally(() => {
                if (shouldUpdate) {
                    setLoading(false);
                }
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


export const useTeamGame = (gameId) => {

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

