package com.mwozniak.capser_v2.models.database.tournament.strategy.seeding;

import com.mwozniak.capser_v2.models.database.tournament.Tournament;

public interface SeedingStrategy {

    void seedPlayers(Tournament tournament);

}
