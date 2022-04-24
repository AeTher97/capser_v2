package com.mwozniak.capser_v2.models.database.tournament.strategy.seeding;

import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.CompetitorTournamentStats;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomSeedStrategy extends SeedFromOutsideStrategy {

    @Override
    public void seedPlayers(Tournament<?> tournament) {

        List<CompetitorTournamentStats> competitorTournamentStats = tournament.getCompetitorTournamentStats();
        if (competitorTournamentStats == null) {
            competitorTournamentStats = new ArrayList<>();
            tournament.setCompetitorTournamentStats(competitorTournamentStats);
        }

        for (Competitor competitor : tournament.getCompetitorList()) {
            competitorTournamentStats.add(CompetitorTournamentStats.builder()
                    .competitorId(competitor.getId())
                    .build());
        }

        List<Competitor> competitors = tournament.getCompetitorList();
        Collections.shuffle(tournament.getCompetitorList());

        seedEntriesFromOutside(tournament, competitors);
    }

}
