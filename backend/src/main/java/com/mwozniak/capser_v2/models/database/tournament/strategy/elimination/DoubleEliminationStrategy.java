package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.ArrayList;
import java.util.List;

public class DoubleEliminationStrategy extends AbstractEliminationStrategy {


    public DoubleEliminationStrategy(Tournament<?> tournament) {
        super(tournament);
    }

    private void resolveOddLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord) {
        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
            BracketEntry higherEntry = tournament.getBracketEntry(higherAbsoluteCoord + i);
            BracketEntry lowerEntry = tournament.getBracketEntry(absoluteCoord + i);
            BracketEntry upperBracketEntry = tournament.getBracketEntry(getUpperBracketCoord(higherAbsoluteCoord + i, currentRow));

            if (higherEntry.isFinal()) {
                continue;
            }

            threeObjectLosingResolve(upperBracketEntry, lowerEntry, higherEntry);
            threeObjectBye(upperBracketEntry, lowerEntry, higherEntry, upperBracketEntry.isBye(), lowerEntry.isBye(), false);

        }
    }


    private void resolvePowerOfTwoLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord, boolean passFromTopWhenForfeited) {
        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, true); i += 2) {
            BracketEntry topEntry = tournament.getBracketEntry(absoluteCoord + i);
            BracketEntry bottomEntry = tournament.getBracketEntry(absoluteCoord + i + 1);
            if (currentRow.equals(BracketEntryType.D_RO_2)) {
                bottomEntry = tournament.getBracketEntry(1000);
            }
            BracketEntry higherEntry = tournament.getBracketEntry(higherAbsoluteCoord + i / 2);

            if (higherEntry.isFinal()) {
                continue;
            }

            threeObjectResolve(topEntry, bottomEntry, higherEntry);
            threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.isBye(), bottomEntry.isBye(), passFromTopWhenForfeited);

        }
    }

    private void resolvePowerOfTwoLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord) {
        resolvePowerOfTwoLevel(currentRow, absoluteCoord, higherAbsoluteCoord, true);
    }

    @Override
    public void resolveAfterGame(Tournament<?> tournament) {
        BracketEntryType currentRow = tournament.getSize();
        while (!currentRow.equals(BracketEntryType.D_RO_1)) {
            tournament.getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);

            if (BracketEntryType.isPowerOf2(currentRow)) {
                int absoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(currentRow, true);
                int higherAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigherPowerOf2(currentRow), true);

                resolvePowerOfTwoLevel(currentRow, absoluteCoord, higherAbsoluteCoord);

                if (!currentRow.equals(tournament.getSize())) {
                    int bottomAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                    int bottomHigherAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(currentRow), false);

                    resolvePowerOfTwoLevel(currentRow, bottomAbsoluteCoord, bottomHigherAbsoluteCoord, !currentRow.equals(BracketEntryType.D_RO_2));
                }

            } else {
                int absoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                int higherAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(currentRow), false);

                if (currentRow.equals(BracketEntryType.getHigher(tournament.getSize()))) {
                    int upperCoord = BracketEntryType.getDoubleEliminationCountAbove(tournament.getSize(), true);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i++) {
                        BracketEntry upperEntry1 = tournament.getBracketEntry(upperCoord + i * 2);
                        BracketEntry upperEntry2 = tournament.getBracketEntry(upperCoord + i * 2 + 1);
                        BracketEntry lowerEntry = tournament.getBracketEntry(absoluteCoord + i);

                        if (upperEntry1.getGame() != null) {
                            lowerEntry.setCompetitor1(tournament.getLoser(upperEntry1));
                        }
                        if (upperEntry2.getGame() != null) {
                            lowerEntry.setCompetitor2(tournament.getLoser(upperEntry2));
                        }
                        if (upperEntry1.isFinal() && upperEntry2.isFinal()) {
                            if (upperEntry1.isBye() || upperEntry2.isBye() || upperEntry2.isForfeited() || upperEntry1.isForfeited())
                                lowerEntry.setBye(true);
                            lowerEntry.setFinal(true);
                        }

                    }
                }
                resolveOddLevel(currentRow, absoluteCoord, higherAbsoluteCoord);

            }


            currentRow = BracketEntryType.getHigher(currentRow);

        }
    }

    @Override
    public void populateEntryList(Tournament<?> tournament) {
        List<BracketEntry> abstractBracketEntries = new ArrayList<>();
        int highNumber = BracketEntryType.getDoubleEliminationCountAboveAndEqual(tournament.getSize(), true);
        int lowNumber;
        lowNumber = 1000 + BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.getHigher(tournament.getSize()), false);

        for (int i = 0; i < highNumber; i++) {
            BracketEntry abstractBracketEntry = tournament.createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        for (int i = 1000; i < lowNumber; i++) {
            BracketEntry abstractBracketEntry = tournament.createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        tournament.setBracketEntries(abstractBracketEntries);
        tournament.getBracketEntry(0).setBracketEntryType(BracketEntryType.D_RO_1);
        tournament.getBracketEntry(1).setBracketEntryType(BracketEntryType.D_RO_2);
    }

    @Override
    public void resolveByes(Tournament<?> tournament) {
        BracketEntryType currentRow = tournament.getSize();
        while (!currentRow.equals(BracketEntryType.D_RO_1)) {
            tournament.getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
            if (BracketEntryType.isPowerOf2(currentRow)) {
                if (!currentRow.equals(BracketEntryType.D_RO_2)) {
                    int absoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(currentRow, true);
                    int higherAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigherPowerOf2(currentRow), true);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, true); i += 2) {
                        resolveByesInARow(currentRow, absoluteCoord, higherAbsoluteCoord, i);
                    }
                }

                if (!currentRow.equals(tournament.getSize())) {
                    int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
                        BracketEntry entry = tournament.getBracketEntry(lowerAbsoluteCoord + i);
                        entry.setBracketEntryType(currentRow);
                    }
                }
            } else {
                int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
                    BracketEntry entry = tournament.getBracketEntry(lowerAbsoluteCoord + i);
                    entry.setBracketEntryType(currentRow);
                }
            }
            currentRow = BracketEntryType.getHigher(currentRow);

        }
    }
}
