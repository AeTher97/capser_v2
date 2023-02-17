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
