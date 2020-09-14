import React, {useEffect, useState} from 'react';
import TextField from "@material-ui/core/TextField";
import useFieldSearch from "../../data/UsersFetch";
import {Divider, Typography, useTheme} from "@material-ui/core";
import CircularProgress from "@material-ui/core/CircularProgress";
import {makeStyles} from "@material-ui/core/styles";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

const FetchSelectField = (props) => {


    const {onChange, label, url, resultSize, nameParameter, className} = props;

    const searchPhrase = useFieldSearch(url, resultSize || 5);
    const classes = fetchSelectFieldStyles();

    const [phrase, setPhrase] = useState('')
    const [searchResult, setSearchResult] = useState([])
    const [searching, setSearching] = useState(false);
    const [focused, setFocused] = useState(false);

    useEffect(() => {
        if (phrase !== '') {
            setSearching(true)
            searchPhrase(phrase).then((response => {
                setSearchResult(response.data.content)
                setSearching(false);
            }))
        } else {
            setSearching(false);
            setSearchResult([]);
        }
    }, [phrase])

    const getNoResults = () => {
        if (phrase.length > 0 && searchResult.length === 0) {
            return <Typography style={{padding: 5}}>No results</Typography>
        } else {
            return <Typography style={{padding: 5}}>Type in a name</Typography>;
        }
    }

    const getChoiceList = () => {
        return (!searching ?
            <div className={classes.recordContainer}>
                {searchResult.length > 0 ? searchResult.map(user => {
                    return (
                        <div className={classes.record} key={user[nameParameter]} onMouseDown={() => {
                            setPhrase(user[nameParameter])
                            onChange(user)
                        }}>
                            <Typography className={classes.record} style={{padding: 5}}>
                                {user[nameParameter]}
                            </Typography>
                            <Divider/>
                        </div>
                    )
                }) : getNoResults()}
            </div> : <div className={classes.loadingContainer}>
                <CircularProgress/>
            </div>)
    }

    return (
        <div style={{maxWidth: 200}}>
            <TextField label={phrase === '' ? label : ''}
                       onChange={(e) => setPhrase(e.target.value)} onFocus={() => {
                setFocused(true)
            }}
                       value={phrase}
                       onBlur={() => {
                           setFocused(false)
                       }}
                       InputProps={{endAdornment: <ExpandMoreIcon/>}}
                       className={[classes.input, className].join(' ')}
            />
            {focused && getChoiceList()}
        </div>
    );
};

const fetchSelectFieldStyles = makeStyles(theme => ({
    recordContainer: {
        backgroundColor: theme.palette.background.paper,
        textAlign: "left",
        position: "absolute",
        zIndex: 1000,
        minWidth: 200
    },
    loadingContainer: {
        backgroundColor: theme.palette.background.paper,
        textAlign: "center",
        position: "absolute",
        zIndex: 1000,
        minWidth: 200
    },
    record: {
        '&:hover': {
            cursor: "pointer",
            color: 'red'
        }
    },
    input: {
        cursor: 'pointer'
    }
}))

FetchSelectField.propTypes = {};

export default FetchSelectField;
