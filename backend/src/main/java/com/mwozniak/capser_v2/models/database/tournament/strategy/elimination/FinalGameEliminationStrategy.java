package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;

public abstract class FinalGameEliminationStrategy extends AbstractEliminationStrategy{

    public FinalGameEliminationStrategy(Tournament tournament) {
        super(tournament);
    }

    @Override
    public void checkWinCondition(Tournament tournament) {
        BracketEntry entry = tournament.getBracketEntry(0);
        if (entry instanceof AbstractSinglesBracketEntry) {
            AbstractSinglesBracketEntry abstractSinglesBracketEntry = (AbstractSinglesBracketEntry) entry;
            if (entry.getCoordinate() == 0) {
                if (entry.getGame() != null) {
                    tournament.setFinished(true);
                    tournament.setWinner(abstractSinglesBracketEntry.getGame().getWinner());
                } else if (entry.isForfeited()) {
                    tournament.setFinished(true);
                    if (abstractSinglesBracketEntry.getPlayer1().getId().equals(entry.getForfeitedId())) {
                        tournament.setWinner(abstractSinglesBracketEntry.getPlayer2().getId());
                    } else {
                        tournament.setWinner(abstractSinglesBracketEntry.getPlayer1().getId());
                    }
                }
            }
        } else {
            DoublesBracketEntry doublesBracketEntry = (DoublesBracketEntry) entry;
            if (entry.getCoordinate() == 0) {
                if (entry.getGame() != null) {
                    tournament.setFinished(true);
                    tournament.setWinner(doublesBracketEntry.getGame().getWinner());
                } else if (entry.isForfeited()) {
                    tournament.setFinished(true);
                    if (doublesBracketEntry.getTeam1().getId().equals(entry.getForfeitedId())) {
                        tournament.setWinner(doublesBracketEntry.getTeam2().getId());
                    } else {
                        tournament.setWinner(doublesBracketEntry.getTeam1().getId());
                    }
                }
            }
        }
    }
}
