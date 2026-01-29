import React, {useEffect, useState} from 'react';
import {useHistory, useParams} from "react-router-dom";
import {useTournamentData} from "../../data/TournamentData";
import {Button, Dialog, MenuItem, Select, Typography, useTheme} from "@material-ui/core";
import SingleEliminationLadder from "./SingleEliminationLadder";
import mainStyles from "../../misc/styles/MainStyles";
import {useDispatch, useSelector} from "react-redux";
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
import {DndProvider} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import {showError} from "../../redux/actions/alertActions";
import TesterDoubleEliminationLadder from "./TesterDoubleEliminationLadder";

const TournamentComponent = () => {

    const tournament = {"id":null,"tournamentType":"DOUBLE_ELIMINATION","size":"D_RO_16","seedType":"RANDOM","tournamentName":"Test","owner":null,"date":1769720679278,"bracketEntries":[{"id":null,"coordinate":0,"bracketEntryType":"D_RO_1","forfeited":false,"forfeitedId":null,"team1":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679330,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["ed7f4090-1cd4-4544-bc04-470cbf527948"]},"team2":{"playerList":["4451983e-6948-40ea-a509-e1b057042a7c"]},"team1DatabaseId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","team2DatabaseId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","team1Score":0,"team2Score":0,"winnerId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","gameType":"DOUBLES","allPlayers":["ed7f4090-1cd4-4544-bc04-470cbf527948","4451983e-6948-40ea-a509-e1b057042a7c"],"winner":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370"},"final":true,"bye":false},{"id":null,"coordinate":1,"bracketEntryType":"D_RO_2","forfeited":false,"forfeitedId":null,"team1":{"id":"4bc297c6-af20-43c4-9375-331c9a3237a8","name":"a0","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679325,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["d49c7a8f-5dbc-46f1-9773-debbc3ec04f4"]},"team2":{"playerList":["ab4c3866-ad72-4b6b-a3b0-a3f8670e56d6"]},"team1DatabaseId":"4bc297c6-af20-43c4-9375-331c9a3237a8","team2DatabaseId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","team1Score":0,"team2Score":0,"winnerId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","gameType":"DOUBLES","allPlayers":["d49c7a8f-5dbc-46f1-9773-debbc3ec04f4","ab4c3866-ad72-4b6b-a3b0-a3f8670e56d6"],"winner":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370"},"final":true,"bye":false},{"id":null,"coordinate":2,"bracketEntryType":"D_RO_4","forfeited":false,"forfeitedId":null,"team1":{"id":"4bc297c6-af20-43c4-9375-331c9a3237a8","name":"a0","active":false,"playerList":null,"doublesStats":null},"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":3,"bracketEntryType":"D_RO_4","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true},{"id":null,"coordinate":4,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":{"id":"4bc297c6-af20-43c4-9375-331c9a3237a8","name":"a0","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679323,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["f56f1a26-edeb-4935-b3cf-b855758e54ac"]},"team2":{"playerList":["78c57b42-f4aa-497f-861d-fb66138c23f9"]},"team1DatabaseId":"4bc297c6-af20-43c4-9375-331c9a3237a8","team2DatabaseId":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","team1Score":0,"team2Score":0,"winnerId":"4bc297c6-af20-43c4-9375-331c9a3237a8","gameType":"DOUBLES","allPlayers":["f56f1a26-edeb-4935-b3cf-b855758e54ac","78c57b42-f4aa-497f-861d-fb66138c23f9"],"winner":"4bc297c6-af20-43c4-9375-331c9a3237a8"},"final":true,"bye":false},{"id":null,"coordinate":5,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":6,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":7,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true},{"id":null,"coordinate":8,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":{"id":"4bc297c6-af20-43c4-9375-331c9a3237a8","name":"a0","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679314,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["f811f4e4-8362-44bd-817a-ee6aaf908f01"]},"team2":{"playerList":["59ce3c8c-cb37-4194-883b-2632cfe63730"]},"team1DatabaseId":"4bc297c6-af20-43c4-9375-331c9a3237a8","team2DatabaseId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","team1Score":0,"team2Score":0,"winnerId":"4bc297c6-af20-43c4-9375-331c9a3237a8","gameType":"DOUBLES","allPlayers":["f811f4e4-8362-44bd-817a-ee6aaf908f01","59ce3c8c-cb37-4194-883b-2632cfe63730"],"winner":"4bc297c6-af20-43c4-9375-331c9a3237a8"},"final":true,"bye":false},{"id":null,"coordinate":9,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null},"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":10,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":11,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":12,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":13,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":14,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":15,"bracketEntryType":"D_RO_16","forfeited":false,"forfeitedId":null,"team1":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","name":"a1","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679320,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["ae4cfa22-37f4-43b4-9544-dc64d3e44491"]},"team2":{"playerList":["97f9cd20-e7c9-4e21-9de7-a256dd4f1f26"]},"team1DatabaseId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","team2DatabaseId":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","team1Score":0,"team2Score":0,"winnerId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","gameType":"DOUBLES","allPlayers":["ae4cfa22-37f4-43b4-9544-dc64d3e44491","97f9cd20-e7c9-4e21-9de7-a256dd4f1f26"],"winner":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370"},"final":true,"bye":false},{"id":null,"coordinate":1000,"bracketEntryType":"D_RO_2","forfeited":false,"forfeitedId":null,"team1":{"id":"4bc297c6-af20-43c4-9375-331c9a3237a8","name":"a0","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679329,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["df053f27-a4c7-42e8-b3f2-ade89b18aeb5"]},"team2":{"playerList":["ea45f50f-54aa-4b4f-98a9-ebb5a777f80e"]},"team1DatabaseId":"4bc297c6-af20-43c4-9375-331c9a3237a8","team2DatabaseId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","team1Score":0,"team2Score":0,"winnerId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","gameType":"DOUBLES","allPlayers":["df053f27-a4c7-42e8-b3f2-ade89b18aeb5","ea45f50f-54aa-4b4f-98a9-ebb5a777f80e"],"winner":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07"},"final":true,"bye":false},{"id":null,"coordinate":1001,"bracketEntryType":"D_RO_3","forfeited":false,"forfeitedId":null,"team1":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679327,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["6f27f9d2-f30f-49ca-8cac-b19d726e5570"]},"team2":{"playerList":["f2b796fe-860a-4b1c-a5ae-4053ccb7fe08"]},"team1DatabaseId":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","team2DatabaseId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","team1Score":0,"team2Score":0,"winnerId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","gameType":"DOUBLES","allPlayers":["6f27f9d2-f30f-49ca-8cac-b19d726e5570","f2b796fe-860a-4b1c-a5ae-4053ccb7fe08"],"winner":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07"},"final":true,"bye":false},{"id":null,"coordinate":1002,"bracketEntryType":"D_RO_4","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true},{"id":null,"coordinate":1003,"bracketEntryType":"D_RO_4","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true},{"id":null,"coordinate":1004,"bracketEntryType":"D_RO_6","forfeited":false,"forfeitedId":null,"team1":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null},"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":1005,"bracketEntryType":"D_RO_6","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true},{"id":null,"coordinate":1006,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null},"team2":{"id":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","name":"a1","active":false,"playerList":null,"doublesStats":null},"game":{"id":null,"accepted":false,"nakedLap":false,"team1Name":null,"team2Name":null,"gameMode":"SUDDEN_DEATH","time":1769720679324,"gamePlayerStats":[],"gameEventList":null,"team1":{"playerList":["3b50951a-40f2-4ada-aebf-57a1515e394c"]},"team2":{"playerList":["49936dd4-078b-497f-ad5d-f825651763b7"]},"team1DatabaseId":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","team2DatabaseId":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","team1Score":0,"team2Score":0,"winnerId":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","gameType":"DOUBLES","allPlayers":["3b50951a-40f2-4ada-aebf-57a1515e394c","49936dd4-078b-497f-ad5d-f825651763b7"],"winner":"9b2d79d9-467d-4f8b-bcf0-5547bf853524"},"final":true,"bye":false},{"id":null,"coordinate":1007,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":1008,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":1009,"bracketEntryType":"D_RO_8","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true},{"id":null,"coordinate":1010,"bracketEntryType":"D_RO_12","forfeited":false,"forfeitedId":null,"team1":{"id":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","name":"a1","active":false,"playerList":null,"doublesStats":null},"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":1011,"bracketEntryType":"D_RO_12","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":1012,"bracketEntryType":"D_RO_12","forfeited":false,"forfeitedId":null,"team1":null,"team2":null,"game":null,"final":true,"bye":true},{"id":null,"coordinate":1013,"bracketEntryType":"D_RO_12","forfeited":false,"forfeitedId":null,"team1":null,"team2":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null},"game":null,"final":true,"bye":true}],"teams":[{"team":{"id":"4bc297c6-af20-43c4-9375-331c9a3237a8","name":"a0","active":false,"playerList":null,"doublesStats":null}},{"team":{"id":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","name":"a1","active":false,"playerList":null,"doublesStats":null}},{"team":{"id":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","name":"a2","active":false,"playerList":null,"doublesStats":null}},{"team":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null}},{"team":{"id":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","name":"a4","active":false,"playerList":null,"doublesStats":null}}],"winner":{"team":{"id":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","name":"a3","active":false,"playerList":null,"doublesStats":null}},"players":null,"gameType":"DOUBLES","seeded":true,"finished":true,"competitorTournamentStats":[{"competitorId":"4bc297c6-af20-43c4-9375-331c9a3237a8","wins":0,"losses":0,"points":0,"pointsLost":0,"sinks":0,"sinksLost":0},{"competitorId":"b2eefea4-99a7-4db1-a585-0d9b83c316ef","wins":0,"losses":0,"points":0,"pointsLost":0,"sinks":0,"sinksLost":0},{"competitorId":"9b8e97d8-0710-4ec3-8b1e-3d1aad287c07","wins":0,"losses":0,"points":0,"pointsLost":0,"sinks":0,"sinksLost":0},{"competitorId":"b645d9ca-f7d1-44bb-abd7-1d47d08b9370","wins":0,"losses":0,"points":0,"pointsLost":0,"sinks":0,"sinksLost":0},{"competitorId":"9b2d79d9-467d-4f8b-bcf0-5547bf853524","wins":0,"losses":0,"points":0,"pointsLost":0,"sinks":0,"sinksLost":0}]}

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
            return true;
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

        const teams = tournament && tournament.gameType === 'DOUBLES';
        const playerMultiplier = teams ? 2 : 1;

        return (
            <DndProvider backend={HTML5Backend}>
                {tournament && <div style={{height: '500vh'}}>
                    <div style={{display: "flex", justifyContent: 'center', flexDirection: "row", flexWrap: "wrap"}}>
                        <div style={{
                            maxWidth: 800, flex: 1, padding: 0, minWidth: 300, alignItems: "stretch",
                            justifyContent: 'stretch', display: 'flex', flexDirection: 'column'
                        }}>
                            <div style={{padding: 10, flex: 1}} className={classes.standardBorder}>
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
                            {isOwner() && <div className={classes.standardBorder} style={{padding: 10, marginTop: 0}}>
                                <Typography variant={"h5"}>Actions</Typography>
                                <div>
                                    {!edited && <div style={{padding: 5}}>
                                        {!tournament.seeded && isOwner() &&
                                            <Button onClick={() => setDialogOpen(true)}>
                                                {tournament.seedType !== 'PICKED' ? 'Seed players and start' : 'Start tournament'}
                                            </Button>}
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

                    {(tournament.seeded || tournament.seedType === "PICKED") && tournament.tournamentType === "SINGLE_ELIMINATION" &&
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
                                                 started={tournament.seeded}
                        />}
                    {(tournament.seeded || tournament.seedType === "PICKED") && tournament.tournamentType === "DOUBLE_ELIMINATION" &&
                        <TesterDoubleEliminationLadder isOwner={isOwner()}
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
                                                 started={tournament.seeded}
                        />}
                    {(tournament.seeded || tournament.seedType === "PICKED") && tournament.tournamentType === "ROUND_ROBIN" &&
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
                                          started={tournament.seeded}
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
                                />}
                            {teams && <AddDoublesGameComponent
                                showBorder={false}
                                chooseTeams={false}
                                onCancel={() => {
                                    setAddGameOpen(false)
                                }}
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
                                <Button variant={"outlined"} style={{marginLeft: 10}}
                                        onClick={() => setSkipGameOpen(false)}>Cancel</Button>
                            </div>
                        </div>
                    </Dialog>}
                    <YesNoDialog onYes={() => {
                    }} onNo={() => {
                    }} setOpen={setDialogOpen} open={dialogOpen}
                                 question={"Are you sure you want to start the tournament?"}/>
                    <YesNoDialog onYes={() => {
                    }} onNo={() => {
                    }} setOpen={setDeleteDialogOpen} open={deleteDialogOpen}
                                 question={"Are you sure you want to DELETE the tournament?"}/>
                </div>}
            </DndProvider>
        );
    }
;

export default TournamentComponent;
