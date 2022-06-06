import React, {useEffect, useState} from 'react';
import {useHistory, useParams} from "react-router-dom";
import {useTournamentData} from "../../data/TournamentData";
import {Button, Dialog, MenuItem, Select, Typography, useTheme} from "@material-ui/core";
import SingleEliminationLadder from "./SingleEliminationLadder";
import mainStyles from "../../misc/styles/MainStyles";
import {useSelector} from "react-redux";
import AddSinglesGameComponent from "../game/AddSinglesGameComponent";
import {
    getInProgressString,
    getSeedTypeString,
    getTournamentTypeString
} from "../../screens/tournament/TournamentsComponent";
import BoldTyphography from "../misc/BoldTyphography";
import TournamentsCompetitors from "./TournamentsCompetitors";
import YesNoDialog from "../dialogs/YesNoDialog";
import {useHasRole} from "../../utils/SecurityUtils";
import {getGameIcon} from "../game/details/GameComponent";
import {getGameTypeString} from "../../utils/Utils";
import DoubleEliminationLadder from "./DoubleEliminationLadder";
import AddDoublesGameComponent from "../game/AddDoublesGameComponent";
import RoundRobinLadder from "./RoundRobinLadder";

const TournamentComponent = () => {

        const {tournamentId, tournamentType} = useParams();
        const {
            tournament,
            postTournamentGame,
            savePlayers,
            seedTournament,
            deleteTournament,
            skipTournamentGame
        } = useTournamentData(tournamentType, tournamentId);
        const {userId} = useSelector(state => state.auth);
        const classes = mainStyles();
        const [addGameOpen, setAddGameOpen] = useState(false);
        const [skipGameOpen, setSkipGameOpen] = useState(false);
        const [overrides, setOverrides] = useState({});
        const [dialogOpen, setDialogOpen] = useState(false);
        const [edited, setEdited] = useState(false);
        const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
        const theme = useTheme();
        const history = useHistory();
        const hasRole = useHasRole();
        const [forfeitingPlayer, setForfeitingPlayer] = useState("e");


        const [highlighted, setHighlighted] = useState(null);

        const onHighlight = (id) => {
            setHighlighted(id);
        }

        const onHighlightEnd = () => {
            setHighlighted(null)
        }


        const isOwner = () => {
            return tournament.owner === userId && hasRole('ADMIN');
        }

        const [competitors, setCompetitors] = useState();
        useEffect(() => {
            if (tournament) {
                if (tournament.gameType === 'DOUBLES') {
                    setCompetitors(tournament.teams.map(teams => teams.team));
                } else {
                    setCompetitors(tournament.players.map(player => player.user));
                }
            }
        }, [tournament]);

        const addCompetitor = (obj) => {
            const copy = competitors.slice();
            if (!obj) {
                return;
            }
            if (competitors.find(player => player.id === obj.id)) {
                return;
            }
            copy.push(obj);
            setEdited(true);
            setCompetitors(copy);
        }

        const removeCompetitor = (obj) => {
            const copy = competitors.slice();
            setCompetitors(copy.filter(player => player.id !== obj));
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

        const openSkipDialog = (id, player1, player2) => {
            setSkipGameOpen(true);
            setOverrides({
                id,
                player1,
                player2,
            })
        }

        const handleSave = (request) => {
            request.player1Stats.playerId = overrides.player1.id;
            request.player2Stats.playerId = overrides.player2.id;
            postTournamentGame(overrides.id, request);
            setAddGameOpen(false);
        }

        const handleSaveDoubles = (request) => {
            console.log(request);
            postTournamentGame(overrides.id, request);
            setAddGameOpen(false);
        }

        const teams = tournament && tournament.gameType === 'DOUBLES';
        const playerMultiplier = teams ? 2 : 1;

        return (
            <>
                {tournament && <div style={{height: '500vh'}}>
                    <div style={{display: "flex", justifyContent: 'center', flexDirection: "row", flexWrap: "wrap"}}>
                        <div style={{maxWidth: 800, flex: 1, padding: 0, minWidth: 300, alignItems: "stretch"}}>
                            <div style={{padding: 10, height: 'calc(100% - 40px)'}} className={classes.standardBorder}>
                                <div className={classes.header} style={{alignItems: 'flex-start', marginTop: 5}}>
                                    <Typography variant={"h4"} color={"primary"}
                                                style={{flex: 1}}>{tournament.tournamentName}</Typography>
                                    <Typography>{new Date(tournament.date).toDateString()}</Typography>
                                </div>
                                <BoldTyphography
                                    className={classes.header}>{getGameIcon(tournament.gameType)} {getGameTypeString(tournament.gameType)}</BoldTyphography>
                                {tournament.tournamentType === "ROUND_ROBIN" ?
                                    <BoldTyphography>{tournament.players ? tournament.players.length : tournament.teams.length} players</BoldTyphography>
                                    :
                                    <BoldTyphography>{tournament.tournamentType === "DOUBLE_ELIMINATION" ? playerMultiplier * tournament.size.split("_")[2] : playerMultiplier * tournament.size.split("_")[1]} players</BoldTyphography>}
                                {teams &&
                                    <BoldTyphography>{tournament.tournamentType === "DOUBLE_ELIMINATION" ? tournament.size.split("_")[2] : tournament.size.split("_")[1]} teams</BoldTyphography>}
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

                        <div style={{
                            borderTop: `1px solid ${theme.palette.divider}`,
                            padding: 0,
                            flex: 1,
                            minWidth: 300,
                            marginLeft: 0
                        }}
                             className={classes.standardBorder}>
                            {competitors && <TournamentsCompetitors players={competitors} addPlayer={addCompetitor}
                                                                    savePlayers={(ids) => {
                                                                        savePlayers(ids)
                                                                        setEdited(false);
                                                                    }}
                                                                    teams={teams}
                                                                    onHighlight={onHighlight}
                                                                    onHighlightEnd={onHighlightEnd}
                                                                    highlighted={highlighted}
                                                                    removePlayer={removeCompetitor}
                                                                    adding={!tournament.seeded && isOwner()}
                                                                    max={parseInt(tournament.size.split("_")[1])}
                            />}

                        </div>
                    </div>

                    {tournament.seeded && tournament.tournamentType === "SINGLE_ELIMINATION" &&
                        <SingleEliminationLadder isOwner={isOwner()}
                                                 gameType={tournament.gameType}
                                                 bracketEntries={tournament.bracketEntries}
                                                 lowestRound={tournament.size}
                                                 openAddGameDialog={openAddGameDialog}
                                                 winner={tournament.winner}
                                                 openSkipDialog={openSkipDialog}
                                                 teams={teams}
                                                 onHighlight={onHighlight}
                                                 onHighlightEnd={onHighlightEnd}
                                                 highlighted={highlighted}
                        />}
                    {tournament.seeded && tournament.tournamentType === "DOUBLE_ELIMINATION" &&
                        <DoubleEliminationLadder isOwner={isOwner()}
                                                 gameType={tournament.gameType}
                                                 bracketEntries={tournament.bracketEntries}
                                                 lowestRound={tournament.size}
                                                 openAddGameDialog={openAddGameDialog}
                                                 winner={tournament.winner}
                                                 openSkipDialog={openSkipDialog}
                                                 teams={teams}
                                                 onHighlight={onHighlight}
                                                 onHighlightEnd={onHighlightEnd}
                                                 highlighted={highlighted}
                        />}
                    {tournament.seeded && tournament.tournamentType === "ROUND_ROBIN" &&
                        <RoundRobinLadder isOwner={isOwner()}
                                          playerCount={tournament.players ? tournament.players.length : tournament.teams.length}
                                          gameType={tournament.gameType}
                                          bracketEntries={tournament.bracketEntries}
                                          lowestRound={tournament.size}
                                          competitorTournamentStats={tournament.competitorTournamentStats}
                                          competitors={competitors}
                                          openAddGameDialog={openAddGameDialog}
                                          winner={tournament.winner}
                                          openSkipDialog={openSkipDialog}
                                          teams={teams}
                                          onHighlight={onHighlight}
                                          onHighlightEnd={onHighlightEnd}
                                          highlighted={highlighted}
                        />}
                    {overrides.player1 && <Dialog open={addGameOpen}>
                        <div className={classes.standardBorder}
                             style={{margin: 0, maxWidth: 250, paddingLeft: 50, paddingRight: 50}}>
                            {!teams &&
                                <AddSinglesGameComponent type={"singles"} showBorder={false}
                                                         displayGameDataSection={false}
                                                         choosePlayers={false}
                                                         onCancel={() => {
                                                             setAddGameOpen(false)
                                                         }}
                                                         overridePlayer1Name={overrides.player1.username}
                                                         overridePlayer2Name={overrides.player2.username}
                                                         handleSaveExternal={handleSave}
                                />}
                            {teams && <AddDoublesGameComponent
                                showBorder={false}
                                chooseTeams={false}
                                onCancel={() => {
                                    setAddGameOpen(false)
                                }}
                                externalSave={handleSaveDoubles}
                                overrideTeam1Name={overrides.player1.name}
                                overrideTeam2Name={overrides.player2.name}
                                team1={overrides.player1}
                                team2={overrides.player2}
                            />}
                        </div>
                    </Dialog>}

                    {overrides.player1 && <Dialog open={skipGameOpen}>
                        <div className={classes.standardBorder}
                             style={{margin: 0, maxWidth: 250, paddingLeft: 50, paddingRight: 50}}>
                            <Typography>Who skips the game?</Typography>
                            <div style={{padding: 15, display: "flex", justifyContent: "center"}}>
                                <Select value={forfeitingPlayer} onChange={e => setForfeitingPlayer(e.target.value)}
                                        style={{width: 150}}>
                                    <MenuItem value={"e"} disabled>Choose</MenuItem>
                                    <MenuItem
                                        value={overrides.player1.id}>{teams ? overrides.player1.name : overrides.player1.username}</MenuItem>
                                    <MenuItem
                                        value={overrides.player2.id}>{teams ? overrides.player2.name : overrides.player2.username}</MenuItem>

                                </Select>
                            </div>

                            <div style={{display: 'flex', justifyContent: 'center', marginTop: 0}}>
                                <Button onClick={() => {
                                    setForfeitingPlayer("e")
                                    setSkipGameOpen(false);
                                    skipTournamentGame(overrides.id, forfeitingPlayer);
                                }}>Skip</Button>
                                <Button variant={"outlined"} style={{marginLeft: 10}}
                                        onClick={() => setSkipGameOpen(false)}>Cancel</Button>
                            </div>
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
    }
;

export default TournamentComponent;
