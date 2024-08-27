import axios from "axios";
import {useEffect, useState} from "react";

export const usePlayersList = (type, pageNumber, pageSize = 10) => {

    const [players, setPlayers] = useState([])
    const [loading, setLoading] = useState(false);
    const [pageCount, setPageCount] = useState(0)

    const fetchPlayers = () => {
        return axios.get(`/users?pageSize=${pageSize}&pageNumber=${pageNumber}&gameType=${type}`)
    };


    useEffect(() => {
        let shouldUpdate = true;
        setLoading(true)
        fetchPlayers().then((response) => {
            if (shouldUpdate) {
                setLoading(false)
                setPageCount(response.data.totalPages)
                setPlayers(response.data.content)
            }
        })

        return () => {
            shouldUpdate = false
        }
    }, [pageNumber, pageSize])


    return {loading, players, pageCount}

}

export const usePlayerComparison = (player1, player2, gameType) => {
    const [comparison, setComparison] = useState(null)
    const [loading, setLoading] = useState(false);

    const fetchComparison = () => {
        return axios.get(`/games/user/${player1}/${gameType}/comparison/${player2}`)
    };


    useEffect(() => {
        if (!player1 || !player2 || !gameType) {
            return
        }

        let shouldUpdate = true;
        setLoading(true)
        fetchComparison().then((response) => {
            if (shouldUpdate) {
                setLoading(false)
                setComparison(response.data)
            }
        })

        return () => {
            shouldUpdate = false
        }
    }, [player1, player2, gameType])


    return {loading, comparison}
}