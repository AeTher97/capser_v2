import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useSelector} from "react-redux";
import {getRequestGameTypeString} from "../utils/Utils";
import {fetchUsername} from "./FieldSearchData";


export const useSoloGamePost = (type) => {

    const {accessToken} = useSelector(state => state.auth);

    const postGame = (gameRequest) => {
        return axios.post(`${getRequestGameTypeString(type)}`, gameRequest, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })
    };

    return {postGame}

};


export const useSinglesGames = (type, pageNumber = 0, pageSize = 10) => {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagesNumber, setPagesNumber] = useState(0);


    const fetchGames = () => {
        return axios.get(`/${getRequestGameTypeString(type)}?pageSize=${pageSize}&pageNumber=${pageNumber}`)
    };

    useEffect(() => {
        setLoading(true);
        let shouldUpdate = true;
        fetchGames().then((response) => {

            if (shouldUpdate) {
                setPagesNumber(response.data.totalPages);
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


export const useSoloGame = (type, gameId) => {

    const [game, setGame] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        let shouldUpdate = true;
        setLoading(true);
        axios.get(`/${type}/${gameId}`).then(result => {
            if (shouldUpdate) {
                const game = result.data;

                Promise.all([fetchUsername(game.player1), fetchUsername(game.player2)])
                    .then((value) => {
                        if (shouldUpdate) {

                            const usernames = value.map(obj => {
                                return {username: obj.data.username, id: obj.data.id, avatarHash: obj.data.avatarHash}
                            });
                            game.player1Name = usernames.find(obj => obj.id === game.player1).username;
                            game.player2Name = usernames.find(obj => obj.id === game.player2).username;
                            game.player1Data = usernames.find(obj => obj.id === game.player1);
                            game.player2Data = usernames.find(obj => obj.id === game.player2);
                            setGame(game);

                        }
                    }).finally(() => {
                    if (shouldUpdate) {
                        setLoading(false);
                    }
                })

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

export const useGameAcceptance = () => {

    const {accessToken} = useSelector(state => state.auth);

    return {
        acceptGame: (id, type) => {
            return axios.post(`${getRequestGameTypeString(type)}/accept/${id}`, null, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            })
        },
        rejectGame: (id, type) => {
            return axios.post(`${getRequestGameTypeString(type)}/reject/${id}`, null, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            })
        },
    }
}

export const useLatestPlayerGames = (playerId, page = 1) => {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pages, setPages] = useState(0);


    useEffect(() => {
        setLoading(true);
        let shouldUpdate = true;
        axios.get(`games/user/${playerId}?pageNumber=${page - 1}`).then((response) => {

            if (shouldUpdate) {
                setGames(response.data.content);
                setPages(response.data.totalPages)
            }
        }).finally(() => {
            if (shouldUpdate) {
                setLoading(false);
            }
        })
        return () => {
            shouldUpdate = false;
        }
    }, [playerId, page])

    return {games, loading, pages}

}


export const usePlayerGamesWithOpponent = (playerId, opponentId, gameType, page = 1) => {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pages, setPages] = useState(false);


    useEffect(() => {
        if (!gameType || gameType === 'ALL') {
            return;
        }

        setLoading(true);
        let shouldUpdate = true;
        axios.get(`games/user/${playerId}/${gameType}${opponentId ? `?opponentId=${opponentId}` : ''}${opponentId ? "&" : "?"}pageNumber=${page - 1}`).then((response) => {

            if (shouldUpdate) {
                setGames(response.data.content);
                setPages(response.data.totalPages)
            }
        }).finally(() => {
            if (shouldUpdate) {
                setLoading(false);
            }
        })
        return () => {
            shouldUpdate = false;
        }
    }, [playerId, opponentId, gameType, page])

    return {games, loading, pages}

}


