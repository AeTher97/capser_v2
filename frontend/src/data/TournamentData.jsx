import {useEffect, useState} from "react";
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {showError} from "../redux/actions/alertActions";

export const useTournamentsList = (type, pageNumber = 0, pageSize = 10) => {
    const {accessToken} = useSelector(state => state.auth);
    const [tournaments, setTournaments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagesNumber, setPagesNumber] = useState(0);


    const fetchTournaments = () => {
        return axios.get(`/tournaments?pageSize=${pageSize}&pageNumber=${pageNumber}`)
    };

    const createNew = (creationRequest, type) => {
        setLoading(true);
        let shouldUpdate = true;
        axios.post(`/${type}/tournaments`, creationRequest, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }).then((response) => {
            const newContent = [response.data];
            newContent.push(...tournaments);
            setTournaments(newContent);
        }).finally(() => {
            if (shouldUpdate) {
                setLoading(false);
            }
        })

        return () => {
            shouldUpdate = false;
        }
    }

    useEffect(() => {
        setLoading(true);
        let shouldUpdate = true;
        fetchTournaments().then((response) => {
            setTournaments(response.data);
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
        tournaments,
        loading,
        pagesNumber,
        createNew
    }
}


export const useTournamentData = (type, tournamentId, refresh) => {

    const [tournament, setTournament] = useState(null);
    const {accessToken} = useSelector(state => state.auth);
    const [loading, setLoading] = useState(false);
    const dispatch = useDispatch();

    useEffect(() => {
        let shouldUpdate = true;
        setLoading(true);
        axios.get(`/${type}/tournaments/${tournamentId}`).then(result => {
            if (shouldUpdate) {
                setTournament(result.data);
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
    }, [tournamentId])

    useEffect(() => {

        if (refresh) {
            const interval = setInterval(() => {
                console.log("Refreshing tournament data")
                axios.get(`/${type}/tournaments/${tournamentId}`).then(result => {
                    setTournament(result.data);

                }).catch(e => {
                    console.log(e.message)
                }).finally(() => {
                    setLoading(false);
                })

            }, 10000)

            return () => {
                clearInterval(interval);
            }
        }
    }, [refresh]);


    const postTournamentGame = (entryId, gameRequest) => {
        let shouldUpdate = true;
        setLoading(true);
        axios.post(`${type}/tournaments/${tournamentId}/entry/${entryId}`, gameRequest, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            setTournament(response.data);
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
    }

    const skipTournamentGame = (entryId, playerToSkip) => {
        let shouldUpdate = true;
        setLoading(true);
        axios.post(`${type}/tournaments/${tournamentId}/entry/${entryId}/skip`, {forfeitedId: playerToSkip}, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            setTournament(response.data);
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
    }

    const savePlayers = (playersList) => {
        setLoading(true);
        return axios.post(`${type}/tournaments/${tournamentId}/players`, playersList, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            setTournament(response.data);
        }).catch(e => {
            console.log(e.message)
            dispatch(showError(e.response.data.error))
        })

    }

    const seedTournament = () => {
        setLoading(true);
        axios.post(`${type}/tournaments/${tournamentId}/seed`, null, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            setTournament(response.data);
        }).catch(e => {
            console.log(e.message)
        }).finally(() => {

        })

    }

    const deleteTournament = (callback) => {
        let shouldUpdate = true;
        setLoading(true);
        axios.delete(`${type}/tournaments/${tournamentId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            callback();
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
    }

    const addParticipantToEntryTemp = (entry, bottom, playerId, playerName) => {
        setTournament(current => {
            const i = current.bracketEntries.findIndex(currentEntry => currentEntry.id === entry);

            const teams = tournament && tournament.gameType === 'DOUBLES';

            if (teams) {
                const team = {
                    id: playerId,
                    name: playerName,
                }
                if (bottom) {
                    current.bracketEntries[i].team2 = team;
                } else {
                    current.bracketEntries[i].team1 = team;
                }
            } else {
                const player = {
                    id: playerId,
                    username: playerName,
                    avatarHash: null,
                    achievements: []
                }
                if (bottom) {
                    current.bracketEntries[i].player2 = player;
                } else {
                    current.bracketEntries[i].player1 = player;
                }

            }


            return current;

        })
    }

    const addParticipantsToEntries = () => {
        setLoading(true);
        return axios.post(`${type}/tournaments/${tournamentId}/setSeed`, tournament.bracketEntries, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            setTournament(response.data);
        }).catch(e => {
            console.log(e.message)
            dispatch(showError(e.response.data.error))
        })
    }

    return {
        tournament,
        loading,
        postTournamentGame,
        savePlayers,
        seedTournament,
        deleteTournament,
        skipTournamentGame,
        addParticipantsToEntries,
        addParticipantToEntryTemp
    }
}

