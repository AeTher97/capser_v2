import React, {useEffect, useState} from 'react';
import useAcceptanceFetch from "../../../data/Acceptance";
import PageHeader from "../../misc/PageHeader";
import {Divider, TableBody, Typography} from "@material-ui/core";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import mainStyles from "../../../misc/styles/MainStyles";
import {getGameModeString, getGameTypeString} from "../../../utils/Utils";
import {useDispatch, useSelector} from "react-redux";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import YesNoDialog from "../../misc/YesNoDialog";
import {useGameAcceptance} from "../../../data/SoloGamesData";
import {showSuccess} from "../../../redux/actions/alertActions";
import LoadingComponent from "../../../utils/LoadingComponent";
import CheckIcon from '@material-ui/icons/Check';
import {usePlayerTeams} from "../../../data/TeamsData";
import {fetchUsername} from "../../../data/UsersFetch";
import {listStyles} from "../singles/SinglesPlayersList";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import BoldTyphography from "../../misc/BoldTyphography";

const AcceptanceComponent = props => {

    const fetchAcceptance = useAcceptanceFetch();
    const [acceptanceRequests, setAcceptanceRequests] = useState([]);
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [usernames, setUsernames] = useState([])
    const [teamNames, setTeamNames] = useState([])
    const {userId} = useSelector(state => state.auth)
    const [question, setQuestion] = useState('');
    const [open, setOpen] = useState(false);
    const [onYes, setOnYes] = useState(() => () => {
    })
    const [onNo, setOnNo] = useState(() => () => {
    })

    const small = useXtraSmallSize();
    const styles = listStyles({small})();

    const dispatch = useDispatch();
    const {acceptGame, rejectGame} = useGameAcceptance();

    const {fetchTeamName} = usePlayerTeams()

    const classes = mainStyles();

    const fetch = () => {
        fetchAcceptance().then(data => {
            setAcceptanceRequests(data.data.map(request => request.acceptanceRequest))
            setGames(data.data.map(request => request.abstractGame))
            setLoading(false);

            Promise.all(data.data.map(request => request.abstractGame).map(game => {
                if (game.gameType === 'DOUBLES') {
                    return [fetchTeamName(game.team1DatabaseId), fetchTeamName(game.team2DatabaseId)]
                } else {
                    return [];
                }
            }).flat()).then((value) => {
                setTeamNames(value.map(o => {
                    return {
                        name: o.data.name,
                        id: o.data.id
                    }
                }))

            })

            Promise.all(data.data.map(request => request.abstractGame).map(game => {
                if (game.gameType === 'DOUBLES') {
                    return [];
                }
                return [fetchUsername(game.player1), fetchUsername(game.player2)]
            }).flat()).then((value) => {
                setUsernames(value.map(user => {
                    return {id: user.data.id, username: user.data.username}
                }));
                setLoading(false);
            })
        })
    }

    useEffect(() => {
        fetch();
    }, [])

    const findUsername = (id) => {
        const obj = usernames.find(o => o.id === id);
        if (obj) {
            return obj.username;
        } else {
            return ''
        }
    }

    const findTeamName = (id) => {
        const obj = teamNames.find(o => o.id === id);
        if (obj) {
            return obj.name;
        } else {
            return ''
        }
    }


    const findPlayerStats = (game, id) => {
        return game.gamePlayerStats.find(o => o.playerId === id)
    }

    const getGame = (id) => {
        return games.find(game => game.id === id);
    }

    const handleAccept = (game, opponent) => {
        setQuestion(`Are you sure you want to accept a game against ${opponent}?`)
        setOnYes(() => () => {
            acceptGame(game.id, game.gameType).then(() => {
                dispatch(showSuccess("Game accepted"));
                fetch();
            })
        });
        setOnNo(() => () => {
        })
        setOpen(true);
    }

    const findTeamId = (game) => {
        if (game.team1.playerList.includes(userId)) {
            return game.team2DatabaseId;
        } else {
            return game.team1DatabaseId;
        }
    }

    const getWinnerString = (game) => {
        const team = game.team1.playerList.includes(userId);
        let winner = false;
        if (game.team1DatabaseId === game.winnerId) {
            if (team) {
                winner = true;
            }
        } else {
            if (!team) {
                winner = true;
            }
        }
        if (winner) {
            return 'Victory'
        } else {
            return 'Loss'
        }
    }

    const handleReject = (game, opponent) => {
        setQuestion(`Are you sure you want to reject a game against ${opponent}?`)
        setOnYes(() => () => {
            rejectGame(game.id, game.gameType).then(() => {
                dispatch(showSuccess("Game rejected"));
                fetch();
            })
        });
        setOnNo(() => () => {
        })
        setOpen(true);
    }


    const getSinglesRow = (game, request) => {
        const opponent = findUsername(game.player1 === userId ? game.player2 : game.player1);
        const playerStats = findPlayerStats(game, userId);
        const opponentStats = findPlayerStats(game, game.player1 === userId ? game.player2 : game.player1);
        return (<div key={request.id} className={styles.row} style={{flexDirection: "column"}}>
            <div className={classes.header} style={{flexDirection: small ? "column" : "row"}}>
                <BoldTyphography>{getGameTypeString(game.gameType)}</BoldTyphography>
                <Typography style={{marginLeft: 10, marginRight: 10}}>{getGameModeString(game.gameMode)}</Typography>
                <Typography>{game.winner === userId ? 'Victory' : 'Loss'} against {opponent}</Typography>
            </div>
            <div className={classes.header} style={{flexWrap: 'wrap', width: '100%', flexDirection: small ? "column" : "row"}}>
                <Typography style={{marginRight: 20}}>Score {playerStats.score} : {opponentStats.score}</Typography>
                <Typography
                    style={{marginRight: 20}}>Rebuttals {playerStats.rebuttals} : {opponentStats.rebuttals}</Typography>
                <Typography style={{marginRight: 20}}>Sinks {playerStats.sinks} : {opponentStats.sinks}</Typography>
                <Typography>Date {new Date(game.time).toDateString()}</Typography>

                {request.acceptanceRequestType !== 'PASSIVE' ? <div style={{flex: 1, display: "flex", justifyContent: small ? 'center' : "flex-end"}}>
                        <Button onClick={() => {
                            handleAccept(game, opponent)
                        }} style={{marginRight: 5}}>
                            Accept
                        </Button>
                        <Button variant={"outlined"} onClick={() => {
                            handleReject(game, opponent)
                        }}>
                            Reject
                        </Button></div> :
                    <Typography>Pending game</Typography>}
            </div>

        </div>)
    }

    const getMultipleRow = (game, request) => {
        return (<div key={request.id} className={styles.row} style={{flexDirection: "column"}}>
            <div className={classes.header} style={{flexDirection: small ? "column" : "row"}}>
            <BoldTyphography>{getGameTypeString(game.gameType)}</BoldTyphography>
            <Typography style={{marginLeft: 10, marginRight:10}}>{getGameModeString(game.gameMode)}</Typography>
            <Typography>{getWinnerString(game)} against {findTeamName(findTeamId(game))}</Typography>
            </div>
            <div className={classes.header} style={{flexWrap: 'wrap', width: '100%', flexDirection: small ? "column" : "row"}}>
            <Typography style={{marginRight: 20}}>Score {game.team1Score} : {game.team2Score}</Typography>
            <Typography>{new Date(game.time).toDateString()}</Typography>
            {request.acceptanceRequestType !== 'PASSIVE' ? <div style={{flex: 1, display: "flex", justifyContent: small ? 'center' : "flex-end"}}>
                            <Button onClick={() => {
                                handleAccept(game, findTeamName(findTeamId(game)))
                            }} style={{marginRight: 5}}>
                                Accept
                            </Button>
                            <Button variant={"outlined"} onClick={() => {
                                handleReject(game, findTeamName(findTeamId(game)))
                            }}>
                                Reject
                            </Button>
                </div> :
                <Typography>Pending game</Typography>}
            </div>
        </div>)
    }


    const getGameRow = (game, request) => {
        if (game.gameType === 'DOUBLES') {
            return getMultipleRow(game, request);
        } else {
            return getSinglesRow(game, request);
        }

    }

    return (
        <div>
            <PageHeader title={"Games Accepting"} icon={<CheckIcon fontSize={"large"}/>}/>

            <div style={{display: "flex", justifyContent: 'center'}}>

                <div style={{maxWidth: 800, flex: 1}}>
                    {!loading ? <div>
                            <div className={classes.standardBorder} style={{padding: 0}}>
                                <div className={styles.row} style={{flexDirection: small ? "column" : "row", justifyContent: "flex-start"}}>
                                    <Typography>Games</Typography>
                                </div>
                                {acceptanceRequests.map(request => {
                                        const game = getGame(request.gameToAccept)
                                        return getGameRow(game, request);
                                    }
                                )}
                                {acceptanceRequests.length === 0 && <div className={styles.row} style={{padding: 50}}>
                                    <Typography variant={"h5"}>
                                        No games to accept
                                    </Typography>
                                </div>}
                            </div>
                        </div> :
                        <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                            <LoadingComponent/>
                        </div>}

                </div>
            </div>

            <YesNoDialog onYes={onYes} onNo={onNo} question={question} open={open} setOpen={setOpen}/>

        </div>
    );
};

AcceptanceComponent.propTypes = {};

export default AcceptanceComponent;
