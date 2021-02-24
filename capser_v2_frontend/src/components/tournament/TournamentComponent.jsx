import React, {useEffect, useState} from 'react';
import {useHistory, useParams} from "react-router-dom";
import {useTournamentData} from "../../data/TournamentData";
import {Button, Dialog, Typography, useTheme} from "@material-ui/core";
import SingleEliminationLadder from "./SingleEliminationLadder";
import mainStyles from "../../misc/styles/MainStyles";
import {useSelector} from "react-redux";
import AddSinglesGameComponent from "../pages/singles/AddSinglesGameComponent";
import {getInProgressString, getSeedTypeString, getTournamentTypeString} from "./TournamentsComponent";
import BoldTyphography from "../misc/BoldTyphography";
import TournamentsPlayers from "./TournamentsPlayers";
import YesNoDialog from "../misc/YesNoDialog";
import {useHasRole} from "../../utils/SecurityUtils";
import {getGameIcon} from "../game/GameComponent";
import {getGameTypeString} from "../../utils/Utils";

const TournamentComponent = () => {

    const {tournamentId, tournamentType} = useParams();
    const {
        tournament,
        postTournamentGame,
        savePlayers,
        seedTournament,
        deleteTournament
    } = useTournamentData(tournamentType, tournamentId);
    const {userId} = useSelector(state => state.auth);
    const classes = mainStyles();
    const [addGameOpen, setAddGameOpen] = useState(false);
    const [overrides, setOverrides] = useState({});
    const [dialogOpen, setDialogOpen] = useState(false);
    const [edited, setEdited] = useState(false);
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const theme = useTheme();
    const history = useHistory();
    const hasRole = useHasRole();
    const {roles} = useSelector(state => state.auth)


    const isOwner = () => {
        return tournament.owner === userId && hasRole('ADMIN');
    }

    const [players, setPlayers] = useState();
    useEffect(() => {
        if (tournament) {
            setPlayers(tournament.players.map(player => player.user));
        }
    }, [tournament]);

    const addPlayer = (obj) => {
        const copy = players.slice();
        if (players.find(player => player.id === obj.id)) {
            return;
        }
        copy.push(obj);
        setEdited(true);
        setPlayers(copy);
    }

    const removePlayer = (obj) => {
        const copy = players.slice();
        setPlayers(copy.filter(player => player.id !== obj));
        setEdited(true);
    }

    const openAddGameDialog = (id, player1, player2, player1Id, player2Id) => {
        setAddGameOpen(true);
        setOverrides({
            id,
            player1,
            player2,
            player1Id,
            player2Id
        })
    }

    const handleSave = (request) => {
        request.player1Stats.playerId = overrides.player1.id;
        request.player2Stats.playerId = overrides.player2.id;
        postTournamentGame(overrides.id, request);
        setAddGameOpen(false);
    }

    return (
        <>
            {tournament && <div style={{height: '500vh'}}>
                <div style={{display: "flex", justifyContent: 'center', flexDirection: "row", flexWrap: "wrap"}}>
                    <div style={{maxWidth: 800, flex: 1, padding: 0, minWidth: 300}}>
                        <div style={{padding: 10}} className={classes.standardBorder}>
                            <div className={classes.header}>
                                <Typography variant={"h4"} color={"primary"}
                                            style={{flex: 1}}>{tournament.tournamentName}</Typography>
                                <Typography>{new Date(tournament.date).toDateString()}</Typography>
                            </div>
                            <BoldTyphography
                                className={classes.header}>{getGameIcon(tournament.gameType)} {getGameTypeString(tournament.gameType)}</BoldTyphography>
                            <BoldTyphography>{tournament.size.split("_")[1]} players</BoldTyphography>
                            <BoldTyphography>{getTournamentTypeString(tournament.tournamentType)} tournament</BoldTyphography>
                            <Typography>{getSeedTypeString(tournament.seedType)}</Typography>
                            <Typography>{getInProgressString(tournament.seeded, tournament.finished)}</Typography>
                        </div>
                        {isOwner() && <div className={classes.standardBorder} style={{padding: 10}}>
                            <Typography variant={"h5"}>Actions</Typography>
                            <div>
                                {!edited && <div style={{padding: 5}}>
                                    {!tournament.seeded && isOwner() &&
                                    <Button onClick={() => setDialogOpen(true)}>Seed players and start</Button>}
                                </div>}
                                <div style={{padding: 5}}>
                                    <Button variant={"outlined"} onClick={() => setDeleteDialogOpen(true)}>Delete
                                        tournament</Button>
                                </div>
                            </div>
                        </div>}
                    </div>

                    <div style={{borderTop: `1px solid ${theme.palette.divider}`, padding: 0, flex: 1, minWidth: 300}}
                         className={classes.standardBorder}>
                        {players && <TournamentsPlayers players={players} addPlayer={addPlayer}
                                                        savePlayers={(ids) => {
                                                            savePlayers(ids)
                                                            setEdited(false);
                                                        }}
                                                        removePlayer={removePlayer}
                                                        adding={!tournament.seeded && isOwner()}
                                                        max={parseInt(tournament.size.split("_")[1])}
                        />}

                    </div>
                </div>

                {tournament.seeded && tournament.tournamentType === "SINGLE_ELIMINATION" &&
                <SingleEliminationLadder isOwner={isOwner()}
                                         bracketEntries={tournament.bracketEntries}
                                         lowestRound={tournament.size}
                                         openAddGameDialog={openAddGameDialog}
                                         winner={tournament.winner}
                />}
                {overrides.player1 && <Dialog open={addGameOpen}>
                    <div className={classes.standardBorder}
                         style={{margin: 0, maxWidth: 250, paddingLeft: 50, paddingRight: 50}}>
                        <AddSinglesGameComponent type={"singles"} showBorder={false} displayGameDataSection={false}
                                                 choosePlayers={false}
                                                 onCancel={() => {
                                                     setAddGameOpen(false)
                                                 }}
                                                 overridePlayer1Name={overrides.player1.username}
                                                 overridePlayer2Name={overrides.player2.username}
                                                 handleSaveExternal={handleSave}
                        />
                    </div>
                </Dialog>}
                <YesNoDialog onYes={() => {
                    seedTournament()
                }} onNo={() => {
                }} setOpen={setDialogOpen} open={dialogOpen}
                             question={"Are you sure you want to start the tournament?"}/>
                <YesNoDialog onYes={() => {
                    deleteTournament(() => {
                        history.push("/tournaments")

                    })
                }} onNo={() => {
                }} setOpen={setDeleteDialogOpen} open={deleteDialogOpen}
                             question={"Are you sure you want to DELETE the tournament?"}/>
            </div>}
        </>
    );
};

export default TournamentComponent;
