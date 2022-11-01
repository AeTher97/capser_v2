import axios from "axios";
import {useEffect, useState} from "react";

const useSearch = (type, phrase) => {

    const [loading, setLoading] = useState(false);
    const [searchResult, setSearchResult] = useState();


    useEffect(() => {
        if (phrase === '') {
            setSearchResult([]);
            return;
        }
        setLoading(true);
        let update = true;
        axios.get(`/search?searchType=${type}&searchString=${phrase}`).then(response => {
            setSearchResult(response.data);
        }).finally(() => {
            if (update) {
                setLoading(false);
            }
        })
        return () => {
            update = false
        }
    }, [type, phrase])

    return {loading, searchResult}
};


export default useSearch;