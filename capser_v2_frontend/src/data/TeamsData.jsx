import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import axios from "axios";
import {showError, showSuccess} from "../redux/actions/alertActions";
import {fetchUsername} from "./UsersFetch";

export const usePlayerTeams = (userId, pageNumber = 0, pageSize = 5) => {
    const {accessToken} = useSelector(state => state.auth);

    const [teams, setTeams] = useState([]);
    const [loading, setLoading] = useState(false);
    const dispatch = useDispatch();

     useEffect(() => {
        let shouldUpdate = true;
        setLoading(true)
        if (accessToken && userId) {
            axios.get(`/teams/${userId}?pageNumber=${pageNumber}&pageSize=${pageSize}`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            }).then(data => {
                if (shouldUpdate) {
                    setTeams(data.data.content.filter(team => team.active));
                    setLoading(false);
                }
            }).catch(e => {
                console.error(e.message);
            }).finally(() => {
                if (shouldUpdate) {
                    setLoading(false);
                }
            })
        }
    }, [accessToken, pageNumber, pageSize])


    const createTeam = (request) => {
        axios.post("/teams", request, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            const copy = teams.slice();
            copy.push(response.data);
            dispatch(showSuccess("Team created"));
            setTeams(copy);
        }).catch(() => {
            dispatch(showError("Failed to create team"));
        })
    }

    const updateTeam = (id, request) => {
        axios.put(`/teams/${id}`, request, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then((response) => {
            const copy = teams.slice();

            setTeams(copy.map(team => {
                if (team.id === id) {
                    team.name = response.data.name;
                    team.playerList = response.data.playerList
                }
                return team;
            }))
            dispatch(showSuccess("Team edited"));
        }).catch(() => {
            dispatch(showError("Failed to edit team"));
        })
    }

    const deleteTeam = (id) => {
        axios.delete(`/teams/${id}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then(() => {
            const copy = teams.slice();
            setTeams(copy.filter(team => team.id !== id));
        }).catch(e => {
            dispatch(showError("Failed to delete team"));
        })
    }

    const fetchTeamName = (id) => {
        return axios.get(`/teams/name/${id}`);
    }

    return {
        teams: teams,
        createTeam: createTeam,
        deleteTeam: deleteTeam,
        updateTeam: updateTeam,
        fetchTeamName: fetchTeamName,
        loading: loading
    }
}

export const useAllTeams = (pageNumber = 0, pageSize = 5) => {

    const [teams, setTeams] = useState([]);
    const [pageCount, setPageCount] = useState(0);
    const [loading, setLoading] = useState(false);
    const dispatch = useDispatch();


    useEffect(() => {
        setLoading(true);
        let shouldUpdate = true;
        axios.get(`/teams?pageNumber=${pageNumber}&pageSize=${pageSize}`)
            .then(data => {
                const teams = data.data.content;
                Promise.all(teams.map(team => {
                    return team.playerList.map(player => fetchUsername(player));
                }).flat()).then(result => {
                    result = result.map(o => o.data);
                    if (shouldUpdate) {
                        setPageCount(data.data.totalPages);
                        setTeams(teams.map(team => {
                            team.playerList = team.playerList.map(player => {
                                const username = result.find(o => o.id === player).username;
                                return {
                                    username: username,
                                    id: player
                                }
                            })
                            return team;
                        }));
                    }
                }).finally(() => {
                    if (shouldUpdate) {
                        setLoading(false);
                    }
                })

            }).catch(e => {
            dispatch(showError("Failed to load teams"));
            if (shouldUpdate) {
                setLoading(false);
            }
            console.error(e.message);
        })
        return () => {
            shouldUpdate = false;
        }
    }, [pageSize, pageNumber])

    return {teams, loading, pageNumber: pageCount}

}

