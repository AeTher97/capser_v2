import React, {useEffect, useState} from 'react';
import useAcceptanceFetch from "../../../data/Acceptance";
import PageHeader from "../../misc/PageHeader";
import {Divider, TableBody} from "@material-ui/core";
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
        return (<TableRow key={request.id}>
            <TableCell>{getGameTypeString(game.gameType)}</TableCell>
            <TableCell>{getGameModeString(game.gameMode)}</TableCell>
            <TableCell>{opponent}</TableCell>
            <TableCell>{game.winner === userId ? 'Victory' : 'Loss'}</TableCell>
            <TableCell>{playerStats.score} : {opponentStats.score}</TableCell>
            <TableCell>{playerStats.rebuttals} : {opponentStats.rebuttals}</TableCell>
            <TableCell>{playerStats.sinks} : {opponentStats.sinks}</TableCell>
            <TableCell>{new Date(game.time).toDateString()}</TableCell>

            {request.acceptanceRequestType !== 'PASSIVE' ? <TableCell>
                    <Grid container spacing={2}>
                        <Grid item>
                            <Button onClick={() => {
                                handleAccept(game, opponent)
                            }}>
                                Accept
                            </Button>
                        </Grid>
                        <Grid item>
                            <Button variant={"outlined"} onClick={() => {
                                handleReject(game, opponent)
                            }}>
                                Reject
                            </Button>
                        </Grid>
                    </Grid>
                </TableCell> :
                <TableCell>Pending game</TableCell>}

        </TableRow>)
    }

    const getMultipleRow = (game, request) => {
        return (<TableRow key={request.id}>
            <TableCell>{getGameTypeString(game.gameType)}</TableCell>
            <TableCell>{getGameModeString(game.gameMode)}</TableCell>
            <TableCell>{findTeamName(findTeamId(game))}</TableCell>
            <TableCell>{getWinnerString(game)}</TableCell>
            <TableCell>{game.team1Score} : {game.team2Score}</TableCell>
            <TableCell>-</TableCell>
            <TableCell>-</TableCell>
            <TableCell>{new Date(game.time).toDateString()}</TableCell>
            {request.acceptanceRequestType !== 'PASSIVE' ? <TableCell>
                    <Grid container spacing={2}>
                        <Grid item>
                            <Button onClick={() => {
                                handleAccept(game, findTeamName(findTeamId(game)))
                            }}>
                                Accept
                            </Button>
                        </Grid>
                        <Grid item>
                            <Button variant={"outlined"} onClick={() => {
                                handleReject(game, findTeamName(findTeamId(game)))
                            }}>
                                Reject
                            </Button>
                        </Grid>
                    </Grid>
                </TableCell> :
                <TableCell>Pending game</TableCell>}
        </TableRow>)
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


            <div className={[classes.paddedContent].join(' ')}>
                <Divider/>
                {!loading ? <Table style={{width: '100%'}}>
                        <TableHead>
                            <TableRow>
                                <TableCell>Game Type</TableCell>
                                <TableCell>Game Mode</TableCell>
                                <TableCell>Opponent</TableCell>
                                <TableCell>Result</TableCell>
                                <TableCell>Score</TableCell>
                                <TableCell>Rebuttals</TableCell>
                                <TableCell>Sinks</TableCell>
                                <TableCell>Time</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {acceptanceRequests.map(request => {
                                    const game = getGame(request.gameToAccept)
                                    return getGameRow(game, request);
                                }
                            )
                            })
                            }
                        </TableBody>
                    </Table> :
                    <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                        <LoadingComponent/>
                    </div>}
            </div>

            <YesNoDialog onYes={onYes} onNo={onNo} question={question} open={open} setOpen={setOpen}/>
        </div>
    );
};

AcceptanceComponent.propTypes = {};

export default AcceptanceComponent;
