package com.mwozniak.capser_v2.models.database.tournament.strategy.seeding;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.Iterator;
import java.util.List;

public abstract class SeedFromOutsideStrategy implements SeedingStrategy {

    public void seedEntriesFromOutside(Tournament tournament, List<Competitor> playersInSeedingOrder) {

        BracketEntryType size = tournament.getSize();
        int lastRowCoordinateStart;

        if (tournament.getTournamentType().equals(TournamentType.SINGLE_ELIMINATION)) {
            lastRowCoordinateStart = BracketEntryType.getSingleEliminationCountAbove(size);
        } else {
            lastRowCoordinateStart = BracketEntryType.getDoubleEliminationCountAbove(size, true);
        }

        int loopSize;
        if (tournament.getTournamentType().equals(TournamentType.SINGLE_ELIMINATION)) {
            loopSize = size.getValue() / 2;
        } else {
            loopSize = BracketEntryType.getDoubleEliminationCountInRow(size, true);
        }

        Iterator<Competitor> competitorIterator = playersInSeedingOrder.listIterator();

        for (int i = 0; i < loopSize; i += 2) {
            tournament.getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
            BracketEntry topEntry = tournament.getBracketEntry(lastRowCoordinateStart + i / 2);
            int bottomEntryId;
            if (tournament.getTournamentType().equals(TournamentType.SINGLE_ELIMINATION)) {
                bottomEntryId = lastRowCoordinateStart + size.getValue() / 2 - 1 - i / 2;
            } else {
                bottomEntryId = lastRowCoordinateStart + BracketEntryType.getDoubleEliminationCountInRow(size, true) - 1 - i / 2;
            }
            BracketEntry bottomEntry = tournament.getBracketEntry(bottomEntryId);

            fillBottomAndTopEntry(topEntry, bottomEntry, competitorIterator);
        }
    }

    private void fillBottomAndTopEntry(BracketEntry topEntry, BracketEntry bottomEntry, Iterator<Competitor> competitorIterator) {
        if (competitorIterator.hasNext()) {
            topEntry.setCompetitor1(competitorIterator.next());
        } else {
            topEntry.setBye(true);
            topEntry.setFinal(true);
        }
        if (competitorIterator.hasNext()) {
            bottomEntry.setCompetitor2(competitorIterator.next());
        } else {
            bottomEntry.setBye(true);
            bottomEntry.setFinal(true);
        }
        if (competitorIterator.hasNext()) {
            topEntry.setCompetitor2(competitorIterator.next());
        } else {
            topEntry.setBye(true);
            topEntry.setFinal(true);
        }
        if (competitorIterator.hasNext()) {
            bottomEntry.setCompetitor1(competitorIterator.next());
        } else {
            bottomEntry.setBye(true);
            bottomEntry.setFinal(true);
        }
    }
}
