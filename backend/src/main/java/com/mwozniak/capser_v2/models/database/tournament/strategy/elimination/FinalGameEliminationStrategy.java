package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;

public abstract class FinalGameEliminationStrategy extends AbstractEliminationStrategy{

    public FinalGameEliminationStrategy(Tournament<?> tournament) {
        super(tournament);
    }

    @Override
    public void checkWinCondition(Tournament<?> tournament) {
        AbstractSinglesBracketEntry entry = (AbstractSinglesBracketEntry) tournament.getBracketEntry(0);
        if (entry.getCoordinate() == 0) {
            if (entry.getGame() != null) {
                tournament.setFinished(true);
                tournament.setWinner(entry.getGame().getWinner());
            } else if (entry.isForfeited()) {
                tournament.setFinished(true);
                if (entry.getPlayer1().getId().equals(entry.getForfeitedId())) {
                    tournament.setWinner(entry.getPlayer2().getId());
                } else {
                    tournament.setWinner(entry.getPlayer1().getId());
                }
            }
        }
    }
}
