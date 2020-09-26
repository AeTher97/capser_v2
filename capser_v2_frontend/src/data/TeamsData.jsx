import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import axios from "axios";
import {showError, showSuccess} from "../redux/actions/alertActions";

export const usePlayerTeams = (userId, pageNumber = 0, pageSize = 5) => {
    const {accessToken} = useSelector(state => state.auth);

    const [teams, setTeams] = useState([]);
    const [loading, setLoading] = useState(true);
    const dispatch = useDispatch();

    const fetchTeams = () => {
        axios.get(`/teams/${userId}?pageNumber=${pageNumber}&pageSize=${pageSize}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        }).then(data => {
            setTeams(data.data.content.filter(team => team.active));
            setLoading(false);
        }).catch(e => {
            console.error(e.message);
        })
    }

    useEffect(() => {
        if (accessToken && userId) {
            fetchTeams();
        }
    }, [accessToken])


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


