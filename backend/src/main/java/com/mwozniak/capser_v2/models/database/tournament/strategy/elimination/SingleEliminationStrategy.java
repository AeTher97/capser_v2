package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.ArrayList;
import java.util.List;

public class SingleEliminationStrategy extends FinalGameEliminationStrategy {

    public SingleEliminationStrategy(Tournament<?> tournament) {
        super(tournament);
    }

    @Override
    public void resolveAfterGame(Tournament<?> tournament) {
        BracketEntryType currentRow = tournament.getSize();
        while (!currentRow.equals(BracketEntryType.RO_2)) {
            tournament.getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
            int absoluteCoord = BracketEntryType.getSingleEliminationCountAbove(currentRow);
            int higherAbsoluteCoord = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.getHigher(currentRow)) - BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.getHigher(currentRow)) - 1;
            for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                BracketEntry topEntry = tournament.getBracketEntry(absoluteCoord + i);
                BracketEntry bottomEntry = tournament.getBracketEntry(absoluteCoord + i + 1);
                BracketEntry higherEntry = tournament.getBracketEntry(higherAbsoluteCoord + i / 2);

                if (higherEntry.isFinal()) {
                    continue;
                }

                threeObjectResolve(topEntry, bottomEntry, higherEntry);
                threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.isBye(), bottomEntry.isBye(), true);

            }
            currentRow = BracketEntryType.getHigher(currentRow);

        }
    }

    @Override
    public void populateEntryList(Tournament<?> tournament) {
        List<BracketEntry> abstractBracketEntries = new ArrayList<>();
        int number = BracketEntryType.getSingleEliminationCountAboveAndEqual(tournament.getSize());
        for (int i = 0; i < number; i++) {
            BracketEntry abstractBracketEntry = tournament.createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        abstractBracketEntries.get(0).setBracketEntryType(BracketEntryType.RO_2);
        tournament.setBracketEntries(abstractBracketEntries);
    }

    @Override
    public void resolveByes(Tournament<?> tournament) {
        BracketEntryType currentRow = tournament.getSize();
        while (!currentRow.equals(BracketEntryType.RO_2)) {
            tournament.getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
            int absoluteCoord = BracketEntryType.getSingleEliminationCountAbove(currentRow);
            int higherAbsoluteCoord = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.getHigher(currentRow)) - BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.getHigher(currentRow)) - 1;
            for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                resolveByesInARow(currentRow, absoluteCoord, higherAbsoluteCoord, i);
            }
            currentRow = BracketEntryType.getHigher(currentRow);

        }
    }

}
