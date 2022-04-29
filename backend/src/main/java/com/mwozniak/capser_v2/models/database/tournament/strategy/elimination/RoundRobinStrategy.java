package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.database.game.team.AbstractTeamGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.CompetitorTournamentStats;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.exception.GameValidationException;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class RoundRobinStrategy implements EliminationStrategy {

    @Override
    public void resolveAfterGame(Tournament<?> tournament) {
        for (Competitor competitor : tournament.getCompetitorList()) {
            CompetitorTournamentStats tournamentStats = tournament.getCompetitorTournamentStats().stream()
                    .filter(competitorTournamentStats -> competitor.getId().equals(competitorTournamentStats.getCompetitorId())).findAny().get();

            List<BracketEntry> competitorEntries = tournament.getBracketEntries()
                    .stream()
                    .filter(bracketEntry -> {
                        if (bracketEntry.getCompetitor1() != null && bracketEntry.getCompetitor2() != null) {
                            return bracketEntry.getCompetitor1().equals(competitor) || bracketEntry.getCompetitor2().equals(competitor);
                        } else if (bracketEntry.getCompetitor1() != null) {
                            return bracketEntry.getCompetitor1().equals(competitor);
                        } else if (bracketEntry.getCompetitor2() != null) {
                            return bracketEntry.getCompetitor2().equals(competitor);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            int wins = 0;
            int losses = 0;
            int points = 0;
            int pointsLost = 0;
            int sinks = 0;
            int sinksLost = 0;
            for (BracketEntry entry : competitorEntries) {
                if (entry.isFinal()) {
                    if (entry.getGame().getWinnerId().equals(competitor.getId())) {
                        wins++;
                    } else {
                        losses++;
                    }
                    if (entry.getGame() instanceof AbstractSinglesGame) {
                        AbstractSinglesGame abstractSinglesGame = (AbstractSinglesGame) entry.getGame();

                        GamePlayerStats gamePlayerStats;
                        GamePlayerStats opponentPlayerStats;
                        try {
                            if (((AbstractSinglesGame) entry.getGame()).getPlayer1().equals(competitor.getId())) {
                                gamePlayerStats = abstractSinglesGame.getPlayer1Stats();
                                opponentPlayerStats = abstractSinglesGame.getPlayer2Stats();
                            } else {
                                gamePlayerStats = abstractSinglesGame.getPlayer2Stats();
                                opponentPlayerStats = abstractSinglesGame.getPlayer1Stats();
                            }
                            points += gamePlayerStats.getScore();
                            pointsLost += opponentPlayerStats.getScore();
                            sinks += gamePlayerStats.getSinks();
                            sinksLost += opponentPlayerStats.getSinks();
                        } catch (GameValidationException e) {
                            e.printStackTrace();
                        }


                    } else if (entry.getGame() instanceof AbstractTeamGame) {
                        AbstractTeamGame abstractTeamGame = (AbstractTeamGame) entry.getGame();
                        List<GamePlayerStats> gamePlayerStats;
                        List<GamePlayerStats> opponentPlayerStats;
                        if (abstractTeamGame.getTeam1DatabaseId().equals(competitor.getId())) {
                            gamePlayerStats = abstractTeamGame.getTeam1Stats();
                            opponentPlayerStats = abstractTeamGame.getTeam2Stats();
                            points += abstractTeamGame.getTeam1Score();
                            pointsLost += abstractTeamGame.getTeam2Score();
                        } else {
                            gamePlayerStats = abstractTeamGame.getTeam2Stats();
                            opponentPlayerStats = abstractTeamGame.getTeam1Stats();
                            points += abstractTeamGame.getTeam2Score();
                            pointsLost += abstractTeamGame.getTeam1Score();
                        }
                        sinks += abstractTeamGame.getTeamSinks(gamePlayerStats);
                        sinksLost += abstractTeamGame.getTeamSinks(opponentPlayerStats);
                    }
                }
            }
            tournamentStats.setWins(wins);
            tournamentStats.setLosses(losses);
            tournamentStats.setPoints(points);
            tournamentStats.setPointsLost(pointsLost);
            tournamentStats.setSinks(sinks);
            tournamentStats.setSinksLost(sinksLost);
        }
        tournament.getCompetitorTournamentStats().sort(CompetitorTournamentStats.Comparators.DRAW_RESOLVE_CONDITIONS);

    }

    @Override
    public void populateEntryList(Tournament<?> tournament) {
        //No op we have to wait to seeding with populating entries.
    }

    @Override
    public void resolveByes(Tournament<?> tournament) {
        //No byes except seeding ones.
    }

    @Override
    public void checkWinCondition(Tournament<?> tournament) {
        AtomicBoolean finished = new AtomicBoolean(true);
        tournament.getBracketEntries().forEach(bracketEntry ->
        {
            if (!bracketEntry.isFinal()) {
                finished.set(false);
            }
        });
        tournament.setFinished(finished.get());
        // #TODO Set winner
    }
}
