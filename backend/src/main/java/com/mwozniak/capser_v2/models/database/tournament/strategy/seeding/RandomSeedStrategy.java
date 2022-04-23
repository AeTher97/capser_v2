package com.mwozniak.capser_v2.models.database.tournament.strategy.seeding;

import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.Collections;
import java.util.List;

public class RandomSeedStrategy extends SeedFromOutsideStrategy {

    @Override
    public void seedPlayers(Tournament<?> tournament) {

        List<Competitor> competitors = tournament.getCompetitorList();
        Collections.shuffle(tournament.getCompetitorList());

        seedEntriesFromOutside(tournament, competitors);
    }

}
