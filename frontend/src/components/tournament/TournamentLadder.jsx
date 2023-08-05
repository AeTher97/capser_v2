import React from 'react';
import {useParams} from "react-router-dom";
import {useTournamentData} from "../../data/TournamentData";
import SingleEliminationLadder from "./SingleEliminationLadder";
import DoubleEliminationLadder from "./DoubleEliminationLadder";
import RoundRobinLadder from "./RoundRobinLadder";
import {HTML5Backend} from "react-dnd-html5-backend";
import {DndProvider} from "react-dnd";

const TournamentLadder = props => {

    const {tournamentId, tournamentType} = useParams();
    const {
        tournament,
        loading
    } = useTournamentData(tournamentType, tournamentId, true);


    if (!loading && tournament) {
        return (
            <DndProvider backend={HTML5Backend}>
                {(tournament.seeded || tournament.seedType === "PICKED") && tournament.tournamentType === "SINGLE_ELIMINATION" &&
                    <SingleEliminationLadder gameType={tournament.gameType}
                                             bracketEntries={tournament.bracketEntries}
                                             lowestRound={tournament.size}
                                             winner={tournament.winner}
                                             started={tournament.seeded}
                    />}
                {(tournament.seeded || tournament.seedType === "PICKED") && tournament.tournamentType === "DOUBLE_ELIMINATION" &&
                    <DoubleEliminationLadder gameType={tournament.gameType}
                                             bracketEntries={tournament.bracketEntries}
                                             lowestRound={tournament.size}
                                             winner={tournament.winner}
                                             started={tournament.seeded}
                    />}
                {(tournament.seeded || tournament.seedType === "PICKED") && tournament.tournamentType === "ROUND_ROBIN" &&
                    <RoundRobinLadder
                        playerCount={tournament.players ? tournament.players.length : tournament.teams.length}
                        gameType={tournament.gameType}
                        bracketEntries={tournament.bracketEntries}
                        lowestRound={tournament.size}
                        competitorTournamentStats={tournament.competitorTournamentStats}
                        winner={tournament.winner}
                        started={tournament.seeded}
                    />}
            </DndProvider>
        );
    } else {
        return ""
    }

}

TournamentLadder.propTypes = {};

export default TournamentLadder;