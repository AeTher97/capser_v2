import React, {useState} from 'react';
import PageHeader from "../../components/misc/PageHeader";
import {Chip, Grid, TextField} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";
import {useXtraSmallSize} from "../../utils/SizeQuery";
import SearchIcon from "@material-ui/icons/Search";
import LoadingComponent from "../../utils/LoadingComponent";

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


    return (
        <div style={{height: '100%', overflow: 'scroll'}}>
            <PageHeader title={"Search"} icon={<SearchIcon fontSize={"large"}/>}/>
            <div className={classes.paddedContent}
                 style={{display: 'flex', justifyContent: 'center'}}>
                <div style={{width: small ? '100%' : 600}}>
                    <TextField label={"Type what you want to find..."} style={{width: '100%'}}/>
                    <Grid container className={classes.paddedContent} spacing={1}>
                        <Grid item>
                            <Chip label={"All"} variant={"outlined"} color={getColor(searchType, 'all')}
                                  onClick={() => {
                                      setSearchType("all")
                                  }}/>
                        </Grid>
                        <Grid item>
                            <Chip label={"Player"} variant={"outlined"} color={getColor(searchType, 'player')}
                                  onClick={() => {
                                      setSearchType("player")
                                  }}/>
                        </Grid>
                        <Grid item>
                            <Chip label={"Team"} variant={"outlined"} color={getColor(searchType, 'team')}
                                  onClick={() => {
                                      setSearchType("team")
                                  }}/>
                        </Grid>
                    </Grid>
                    <div>
                        <LoadingComponent/>
                    </div>
                </div>
            </div>

        </div>);
};

export default SearchScreen;