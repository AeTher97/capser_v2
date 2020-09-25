import React, {useEffect, useState} from 'react';
import TextField from "@material-ui/core/TextField";
import useFieldSearch from "../../data/UsersFetch";
import {Divider, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import LoadingComponent from "../../utils/LoadingComponent";
import PropTypes from 'prop-types';


const FetchSelectField = ({onChange, label, url, resultSize = 5, nameParameter, className, clearOnChange = false, disabled}) => {


    const searchPhrase = useFieldSearch(url, resultSize || 5);
    const classes = fetchSelectFieldStyles();

    const [phrase, setPhrase] = useState('')
    const [searchResult, setSearchResult] = useState([])
    const [searching, setSearching] = useState(false);
    const [focused, setFocused] = useState(false);

    let timeout;

    useEffect(() => {
        if (phrase !== '') {
            clearTimeout(timeout);
            setSearching(true)
            timeout = setTimeout(() => {
                searchPhrase(phrase).then((response => {
                    setSearchResult(response.data.content)
                    setSearching(false);
                }))
            }, 300)
        } else {
            setSearching(false);
            setSearchResult([]);
        }

        return () => {
            clearTimeout(timeout)
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
        if (disabled) {
            return null;
        }
        return (!searching ?
            <div className={classes.recordContainer}>
                {searchResult.length > 0 ? searchResult.map(user => {
                    return (
                        <div className={classes.record} key={user[nameParameter]} onMouseDown={() => {
                            if (!clearOnChange) {
                                setPhrase(user[nameParameter])
                            } else {
                                setPhrase('');
                            }
                            setFocused(false);
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
                <LoadingComponent size={"small"} wrapper={false}/>
                <Typography>Searching...</Typography>
            </div>)
    }

    return (
        <div style={{maxWidth: 200}}>
            <TextField label={phrase === '' ? label : ''}
                       onChange={(e) => setPhrase(e.target.value)} onFocus={(e) => {
                setFocused(true)
            }}
                       value={phrase}
                       onBlur={() => {
                           setFocused(false)
                       }}
                       InputProps={{endAdornment: <ExpandMoreIcon/>}}
                       className={[classes.input, className].join(' ')}
                       disabled={disabled}
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
        alignItems: 'center',
        position: "absolute",
        zIndex: 1000,
        minWidth: 200,
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'center'
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

FetchSelectField.propTypes = {
    onChange: PropTypes.func.isRequired,
    label: PropTypes.string.isRequired,
    url: PropTypes.string.isRequired,
    resultSize: PropTypes.number,
    nameParameter: PropTypes.string.isRequired,
    className: PropTypes.object,
    clearOnChange: PropTypes.bool,
    disabled: PropTypes.bool
};

export default FetchSelectField;
