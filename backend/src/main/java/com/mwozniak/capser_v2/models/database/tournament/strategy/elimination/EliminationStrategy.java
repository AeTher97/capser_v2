package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.models.database.tournament.Tournament;

public interface EliminationStrategy {

    void resolveAfterGame(Tournament<?> tournament);

    void populateEntryList(Tournament<?> tournament);

    void resolveByes(Tournament<?> tournament);

    void checkWinCondition(Tournament<?> tournament);
}

