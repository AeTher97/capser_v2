package com.mwozniak.capser_v2.models.database.tournament.strategy.seeding;

import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.CompetitorTournamentStats;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.List;
import java.util.UUID;

public class RoundRobinSeedingStrategy implements SeedingStrategy {

    @Override
    public void seedPlayers(Tournament tournament) {
        int playerCount = tournament.getCompetitorList().size();
        boolean evenPlayerCount = playerCount % 2 == 0;
        int numberOfRounds = evenPlayerCount ? playerCount - 1 : playerCount;
        int numberOfGamesInARound = (playerCount + (evenPlayerCount ? 0 : 1)) / 2;

        populateEntryList(tournament, numberOfRounds, numberOfGamesInARound);
        seedWithCircleMethod(tournament, numberOfRounds, numberOfGamesInARound, evenPlayerCount);

        List<CompetitorTournamentStats> competitorTournamentStats = tournament.getCompetitorTournamentStats();

        for (Competitor competitor : tournament.getCompetitorList()) {
            competitorTournamentStats.add(CompetitorTournamentStats.builder()
                    .competitorId(competitor.getId())
                    .build());
        }

    }

    public void seedWithCircleMethod(Tournament tournament, int numberOfRounds, int gamesInRound, boolean evenPlayerCount) {
        List<Competitor> competitors = tournament.getCompetitorList();
        //Make player number equal to put a game with bye in
        if (!evenPlayerCount) {
            competitors.add(new Competitor() {
                @Override
                public UUID getId() {
                    return null;
                }
            });
        }

        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < competitors.size() / 2; j++) {
                BracketEntry bracketEntry = tournament.getBracketEntry(1000 * i + j);
                Competitor competitor1 = competitors.get(j);
                Competitor competitor2 = competitors.get(competitors.size() - 1 - j);

                if (competitor1.getId() != null) {
                    bracketEntry.setCompetitor1(competitor1);
                }

                if (competitor2.getId() != null) {
                    bracketEntry.setCompetitor2(competitor2);
                }

                if (competitor1.getId() == null || competitor2.getId() == null) {
                    bracketEntry.setBye(true);
                }
            }

            Competitor lastCompetitor = competitors.get(competitors.size() - 1);
            competitors.remove(lastCompetitor);
            competitors.add(1, lastCompetitor);
        }


    }

    private void populateEntryList(Tournament tournament, int numberOfRounds, int gamesInRound) {
        List<BracketEntry> bracketEntryList = tournament.getBracketEntries();


        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < gamesInRound; j++) {
                BracketEntry bracketEntry = tournament.createBracketEntry();
                bracketEntry.setCoordinate(1000 * i + j);
                bracketEntryList.add(bracketEntry);
            }
        }

        tournament.setBracketEntries(bracketEntryList);
    }
}
