import React, {useRef, useState} from 'react';
import PageHeader from "../../components/misc/PageHeader";
import {Chip, Grid, TextField, Typography} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import {useXtraSmallSize} from "../../utils/SizeQuery";
import SearchIcon from "@material-ui/icons/Search";
import LoadingComponent from "../../utils/LoadingComponent";
import useSearch from "../../data/SearchData";
import TwichZoom from "../../components/misc/TwichZoom";
import {useHistory} from "react-router-dom";

const getColor = (searchType, requiredType) => {
    if (searchType === requiredType) {
        return 'primary';
    } else {
        return 'default'
    }
}


const SearchScreen = () => {

    const classes = mainStyles();
    const small = useXtraSmallSize();

    const [searchType, setSearchType] = useState('all');
    const [searchPhrase, setSearchPhrase] = useState("");

    const {searchResult, loading} = useSearch(searchType, searchPhrase);

    const history = useHistory();

    const ref = useRef();
    const ref2 = useRef();
    const ref3 = useRef();


    return (
        <div style={{height: '100%', overflow: 'scroll'}}>
            <PageHeader title={"Search"} icon={<SearchIcon fontSize={"large"}/>}/>
            <div className={classes.paddedContent}
                 style={{display: 'flex', justifyContent: 'center'}}>
                <div style={{width: small ? '100%' : 600}}>
                    <TextField label={searchPhrase === '' ? "Type what you want to find..." : ""}
                               style={{width: '100%'}} value={searchPhrase}
                               onChange={e => {
                                   setSearchPhrase(e.target.value)
                               }}/>
                    <Grid container className={classes.paddedContent} spacing={1}>
                        <Grid item>
                            <Chip ref={ref} label={"All"} variant={"outlined"} color={getColor(searchType, 'all')}
                                  onClick={() => {
                                      setSearchType("all");
                                      ref.current.blur();
                                  }}/>
                        </Grid>
                        <Grid item>
                            <Chip ref={ref2} label={"Player"} variant={"outlined"} color={getColor(searchType, 'player')}
                                  onClick={() => {
                                      setSearchType("player")
                                      ref2.current.blur();
                                  }}/>
                        </Grid>
                        <Grid item>
                            <Chip ref={ref3} label={"Team"} variant={"outlined"} color={getColor(searchType, 'team')}
                                  onClick={() => {
                                      setSearchType("team")
                                      ref3.current.blur();
                                  }}/>
                        </Grid>
                    </Grid>
                    <div>
                        {loading && <LoadingComponent/>}
                        {!loading && searchResult && searchResult.map(result => {
                            return <TwichZoom onClick={() => {
                                if (result.type === 'PLAYER') {
                                    history.push(`/players/${result.id}`)
                                } else {
                                    history.push(`/teams/${result.id}`)
                                }
                            }}>
                                <Typography>{result.name}</Typography>
                                <Typography
                                    variant={"caption"}>{result.type === 'PLAYER' ? "Player" : "Team"}</Typography>
                            </TwichZoom>
                        })}

                        {!loading && searchResult && searchResult.length === 0 && <>
                            <Typography>No results found!</Typography>
                        </>}
                    </div>
                </div>
            </div>

        </div>);
};

export default SearchScreen;