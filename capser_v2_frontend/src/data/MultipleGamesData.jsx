import {useSelector} from "react-redux";
import axios from "axios";
import {getRequestGameTypeString} from "../utils/Utils";
import {useEffect, useState} from "react";

export const useMultipleGames = (type, pageNumber = 0, pageSize = 10) => {
    const {accessToken} = useSelector(state => state.auth);
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagesNumber, setPagesNumber] = useState(0);

    const fetchTeamName = (id) => {
        return axios.get(`/teams/name/${id}`);
    }

    useEffect(() => {
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
                setLoading(false);
            })
        })

        return () => {
            shouldUpdate = false;
        }
    }, [pageNumber, pageSize, type])


    const postGame = (gameRequest) => {
        return axios.post(`${getRequestGameTypeString(type)}`, gameRequest, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })
    };

    return {
        postGame: postGame,
        games: games,
        loading,
        pagesNumber
    }
}
