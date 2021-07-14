import React, {useEffect, useRef, useState} from 'react';
import TextField from "@material-ui/core/TextField";
import useFieldSearch from "../../data/UsersFetch";
import {Divider, Typography, useTheme} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import LoadingComponent from "../../utils/LoadingComponent";
import PropTypes from 'prop-types';
import {useKeyPress} from "../../utils/UseKeyPress";
import mainStyles from "../../misc/styles/MainStyles";
import {useSelector} from "react-redux";


const FetchSelectField = ({
                              onChange,
                              label,
                              url,
                              resultSize = 5,
                              nameParameter,
                              className,
                              clearOnChange = false,
                              disabled,
                              searchYourself = false
                          }) => {


    const searchPhrase = useFieldSearch(url, resultSize || 5);
    const theme = useTheme();
    const classes = fetchSelectFieldStyles();
    const styles = mainStyles();
    const {userId} = useSelector(state => state.auth)

    const [phrase, setPhrase] = useState('')
    const [searchResult, setSearchResult] = useState([])
    const [searching, setSearching] = useState(false);
    const [focused, setFocused] = useState(false);

    const [hovered, setHovered] = useState(undefined);

    const enterPress = useKeyPress("Enter");
    const upPress = useKeyPress("ArrowUp");
    const downPress = useKeyPress("ArrowDown");

    const ref = useRef(null);

    const [cursorPosition, setCursorPosition] = useState(0);


    useEffect(() => {
        if (searchResult.length && enterPress) {
            if (clearOnChange) {
                setPhrase('');
            } else {
                setPhrase(searchResult[cursorPosition][nameParameter]);
            }
            ref.current.blur();
            onChange(searchResult[cursorPosition]);
        }
    }, [cursorPosition, enterPress])

    useEffect(() => {
        if (searchResult.length && upPress) {
            setCursorPosition(prevState => (prevState > 0 ? prevState - 1 : prevState));
        }
    }, [upPress])

    useEffect(() => {
        if (searchResult.length && downPress) {
            setCursorPosition(prevState => prevState < searchResult.length - 1 ? prevState + 1 : prevState);
        }
    }, [downPress]);

    useEffect(() => {
        if (searchResult.length && hovered) {
            setCursorPosition(searchResult.indexOf(hovered));
        }
    }, [hovered]);

    useEffect(() => {
        if (searchResult.length) {
            setHovered(searchResult[cursorPosition])
        }
    }, [cursorPosition])

    let timeout;

    useEffect(() => {
        if (phrase !== '') {
            clearTimeout(timeout);
            setSearching(true)
            timeout = setTimeout(() => {
                searchPhrase(phrase).then((response => {
                    if (!searchYourself) {
                        setSearchResult(response.data.content.filter(obj => obj.id !== userId))
                    } else {
                        setSearchResult(response.data.content)
                    }
                    setSearching(false);
                    if (response.data.content.length > 0) {
                        setHovered(response.data.content[cursorPosition]);
                    }
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
            return <div style={{borderBottom: '1px solid ' + theme.palette.divider}}><Typography style={{padding: 5}}>No
                results</Typography></div>
        } else {
            return <div style={{borderBottom: '1px solid ' + theme.palette.divider}}><Typography style={{padding: 5}}>Type
                in a name</Typography></div>;
        }
    }

    const getChoiceList = () => {
        if (disabled) {
            return null;
        }
        return (!searching ?
            <div style={{position: 'relative'}}>
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
                            }}
                                 onMouseEnter={() => {
                                     setHovered(user);
                                 }}
                            >
                                <Typography className={classes.record} style={{padding: 5}}
                                            color={hovered === user ? "primary" : "textPrimary"}>
                                    {user[nameParameter]}
                                </Typography>
                                <Divider/>
                            </div>
                        )
                    }) : getNoResults()}
                </div>
            </div> : <div className={classes.loadingContainer}>

                <LoadingComponent size={"small"} wrapper={false} noPadding showText={false}/>
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
                       InputProps={{
                           endAdornment: focused ?
                               <ExpandMoreIcon style={{cursor: "pointer", transform: "scale(-1,-1)"}}
                                               onMouseDown={() => {
                                                   console.log("bluer")
                                                   ref.current.blur()
                                               }}/> : <ExpandMoreIcon style={{cursor: "pointer"}} onMouseDown={() => {
                                   console.log("focus")
                                   setTimeout(() => ref.current.focus(), 30)
                               }}/>
                       }}
                       className={[classes.input, className].join(' ')}
                       disabled={disabled}
                       inputRef={ref}
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
        minWidth: 200,
        border: "1px solid " + theme.palette.divider,
        borderBottom: 'none',
        left: -1

    },
    loadingContainer: {
        backgroundColor: theme.palette.background.paper,
        alignItems: 'center',
        position: "absolute",
        zIndex: 1000,
        minWidth: 200,
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'center',
        border: "1px solid " + theme.palette.divider
    },
    record: {
        '&:hover': {
            cursor: "pointer"
        },
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
