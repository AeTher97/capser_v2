package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSoloGame;
import com.mwozniak.capser_v2.models.database.game.team.AbstractTeamGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.CompetitorTournamentStats;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class RoundRobinStrategy implements EliminationStrategy {

    @Override
    public void resolveAfterGame(Tournament tournament) {
        for (Competitor competitor : tournament.getCompetitorList()) {
            Optional<CompetitorTournamentStats> tournamentStatsOptional = tournament.getCompetitorTournamentStatsForId(competitor.getId());
            if (!tournamentStatsOptional.isPresent()) {
                continue;
            }
            CompetitorTournamentStats tournamentStats = tournamentStatsOptional.get();

            List<BracketEntry> competitorEntries = tournament.getBracketEntries()
                    .stream()
                    .filter(BracketEntry::isFinal)
                    .filter(bracketEntry -> bracketEntry.getCompetitor1().equals(competitor) || bracketEntry.getCompetitor2().equals(competitor))
                    .collect(Collectors.toList());


            CompetitorTournamentStats temp = new CompetitorTournamentStats();

            for (BracketEntry entry : competitorEntries) {
                if (entry.getGame().getWinner().equals(competitor.getId())) {
                    temp.setWins(temp.getWins() + 1);
                } else {
                    temp.setLosses(temp.getLosses() + 1);
                }

                if (entry.getGame() instanceof AbstractSoloGame) {
                    calculateStatsFromSinglesGame(competitor, temp, entry);
                } else if (entry.getGame() instanceof AbstractTeamGame) {
                    calculateStatsForTeamGame(competitor, temp, entry);
                }

            }
            tournamentStats.setWins(temp.wins);
            tournamentStats.setLosses(temp.losses);
            tournamentStats.setPoints(temp.points);
            tournamentStats.setPointsLost(temp.pointsLost);
            tournamentStats.setSinks(temp.sinks);
            tournamentStats.setSinksLost(temp.sinksLost);
        }
        tournament.getCompetitorTournamentStats().sort(CompetitorTournamentStats.Comparators.DRAW_RESOLVE_CONDITIONS);

    }

    private void calculateStatsForTeamGame(Competitor competitor, CompetitorTournamentStats temp, BracketEntry entry) {
        AbstractTeamGame abstractTeamGame = (AbstractTeamGame) entry.getGame();
        List<GamePlayerStats> gamePlayerStats;
        List<GamePlayerStats> opponentPlayerStats;
        if (abstractTeamGame.getTeam1DatabaseId().equals(competitor.getId())) {
            gamePlayerStats = abstractTeamGame.getTeam1Stats();
            opponentPlayerStats = abstractTeamGame.getTeam2Stats();
            temp.points += abstractTeamGame.getTeam1Score();
            temp.pointsLost += abstractTeamGame.getTeam2Score();
        } else {
            gamePlayerStats = abstractTeamGame.getTeam2Stats();
            opponentPlayerStats = abstractTeamGame.getTeam1Stats();
            temp.points += abstractTeamGame.getTeam2Score();
            temp.pointsLost += abstractTeamGame.getTeam1Score();
        }
        temp.sinks += abstractTeamGame.getTeamSinks(gamePlayerStats);
        temp.sinksLost += abstractTeamGame.getTeamSinks(opponentPlayerStats);
    }

    private void calculateStatsFromSinglesGame(Competitor competitor, CompetitorTournamentStats temp, BracketEntry entry) {
        AbstractSoloGame abstractSoloGame = (AbstractSoloGame) entry.getGame();

        GamePlayerStats gamePlayerStats;
        GamePlayerStats opponentPlayerStats;
        if (((AbstractSoloGame) entry.getGame()).getPlayer1().equals(competitor.getId())) {
            gamePlayerStats = abstractSoloGame.getPlayer1Stats();
            opponentPlayerStats = abstractSoloGame.getPlayer2Stats();
        } else {
            gamePlayerStats = abstractSoloGame.getPlayer2Stats();
            opponentPlayerStats = abstractSoloGame.getPlayer1Stats();
        }
        temp.points += gamePlayerStats.getScore();
        temp.pointsLost += opponentPlayerStats.getScore();

        temp.sinks += gamePlayerStats.getSinks();
        temp.sinksLost += opponentPlayerStats.getSinks();
    }


    @Override
    public void populateEntryList(Tournament tournament) {
        //No op we have to wait to seeding with populating entries.
    }

    @Override
    public void resolveByes(Tournament tournament) {
        //No byes except seeding ones.
    }

    @Override
    public void checkWinCondition(Tournament tournament) {
        AtomicBoolean finished = new AtomicBoolean(true);
        tournament.getBracketEntries().forEach(bracketEntry ->
        {
            if (!bracketEntry.isFinal()) {
                finished.set(false);
            }
        });
        tournament.setFinished(finished.get());
        if (finished.get()) {
            List<CompetitorTournamentStats> stats = tournament.getCompetitorTournamentStats();
            if (stats.isEmpty()) {
                return;
            }
            stats.sort(CompetitorTournamentStats.Comparators.DRAW_RESOLVE_CONDITIONS);
            tournament.setWinner(stats.get(0).getCompetitorId());
        }
    }
}
