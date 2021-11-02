package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractTournament<T extends AbstractGame> extends Tournament<T> {

    protected abstract void createCompetitorsArray();

    protected abstract List<Competitor> getCompetitorList();

    @Override
    public void doSeedPlayers() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION) || tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {
            if (seedType.equals(SeedType.RANDOM)) {
                int playersCount = getCompetitorList().size();
                int lastRowCoordinateStart;

                if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
                    lastRowCoordinateStart = BracketEntryType.getSingleEliminationCountAbove(size);
                } else {
                    lastRowCoordinateStart = BracketEntryType.getDoubleEliminationCountAbove(size, true);
                }

                List<Competitor> randomCompetitorList = getCompetitorList();
                Collections.shuffle(randomCompetitorList);

                int loopSize;
                if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
                    loopSize = size.getValue() / 2;
                } else {
                    loopSize = BracketEntryType.getDoubleEliminationCountInRow(size, true);
                }

                for (int i = 0; i < loopSize; i += 2) {
                    getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                    BracketEntry topEntry = getBracketEntry(lastRowCoordinateStart + i / 2);
                    int bottomEntryId;
                    if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
                        bottomEntryId = lastRowCoordinateStart + size.getValue() / 2 - 1 - i / 2;
                    } else {
                        bottomEntryId = lastRowCoordinateStart + BracketEntryType.getDoubleEliminationCountInRow(size, true) - 1 - i / 2;
                    }
                    BracketEntry bottomEntry = getBracketEntry(bottomEntryId);
                    if (i * 2 < playersCount) {
                        topEntry.setCompetitor1(randomCompetitorList.get(i * 2));
                    } else {
                        topEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 1 < playersCount) {
                        bottomEntry.setCompetitor2(randomCompetitorList.get(i * 2 + 1));
                    } else {
                        bottomEntry.setBye(true);
                        bottomEntry.setFinal(true);
                    }
                    if (i * 2 + 2 < playersCount) {
                        topEntry.setCompetitor2(randomCompetitorList.get(i * 2 + 2));
                    } else {
                        topEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 3 < playersCount) {
                        bottomEntry.setCompetitor1(randomCompetitorList.get(i * 2 + 3));
                    } else {
                        bottomEntry.setBye(true);
                        bottomEntry.setFinal(true);
                    }
                }
            } else if (seedType.equals(SeedType.ELO)) {

            }
        }
    }

    protected void resolveDoubleEliminationAfterGame() {
        BracketEntryType currentRow = size;
        while (!currentRow.equals(BracketEntryType.D_RO_1)) {
            getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);

            if (BracketEntryType.isPowerOf2(currentRow)) {
                int absoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(currentRow, true);
                int higherAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigherPowerOf2(currentRow), true);

                resolvePowerOfTwoLevel(currentRow, absoluteCoord, higherAbsoluteCoord);

                if (!currentRow.equals(size)) {
                    int bottomAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                    int bottomHigherAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(currentRow), false);

                    resolvePowerOfTwoLevel(currentRow, bottomAbsoluteCoord, bottomHigherAbsoluteCoord, !currentRow.equals(BracketEntryType.D_RO_2));
                }

            } else {
                int absoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                int higherAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(currentRow), false);

                if (currentRow.equals(BracketEntryType.getHigher(size))) {
                    int upperCoord = BracketEntryType.getDoubleEliminationCountAbove(size, true);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i++) {
                        BracketEntry upperEntry1 = getBracketEntry(upperCoord + i * 2);
                        BracketEntry upperEntry2 = getBracketEntry(upperCoord + i * 2 + 1);
                        BracketEntry lowerEntry = getBracketEntry(absoluteCoord + i);

                        if (upperEntry1.getGame() != null) {
                            lowerEntry.setCompetitor1(getLoser(upperEntry1));
                        }
                        if (upperEntry2.getGame() != null) {
                            lowerEntry.setCompetitor2(getLoser(upperEntry2));
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
        checkWinCondition();
    }

    private void resolvePowerOfTwoLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord) {
        resolvePowerOfTwoLevel(currentRow, absoluteCoord, higherAbsoluteCoord, true);
    }

    protected void threeObjectResolve(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry) {
        if (topEntry.getGame() != null) {
            if (higherEntry.getCompetitor1() == null) {
                higherEntry.setCompetitor1(getWinner(topEntry));
            }
        } else {
            if (topEntry.isForfeited()) {
                higherEntry.setCompetitor1(topEntry.getCompetitor1().getId().equals(topEntry.getForfeitedId()) ? topEntry.getCompetitor2() : topEntry.getCompetitor1());
            }
        }
        if (bottomEntry.getGame() != null) {
            if (higherEntry.getCompetitor2() == null) {
                higherEntry.setCompetitor2(getWinner(bottomEntry));
            }
        } else {
            if (bottomEntry.isForfeited()) {
                higherEntry.setCompetitor2(bottomEntry.getCompetitor1().getId().equals(bottomEntry.getForfeitedId()) ? bottomEntry.getCompetitor2() : bottomEntry.getCompetitor1());
            }
        }
    }


    private void resolvePowerOfTwoLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord, boolean passFromTopWhenForfeited) {
        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, true); i += 2) {
            BracketEntry topEntry = getBracketEntry(absoluteCoord + i);
            BracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
            if (currentRow.equals(BracketEntryType.D_RO_2)) {
                bottomEntry = getBracketEntry(1000);
            }
            BracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);

            if (higherEntry.isFinal()) {
                continue;
            }

            threeObjectResolve(topEntry, bottomEntry, higherEntry);
            threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.isBye(), bottomEntry.isBye(), passFromTopWhenForfeited);

        }
    }

    private void resolveOddLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord) {
        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
            BracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i);
            BracketEntry lowerEntry = getBracketEntry(absoluteCoord + i);
            BracketEntry upperBracketEntry = getBracketEntry(getUpperBracketCoord(higherAbsoluteCoord + i, currentRow));

            if (higherEntry.isFinal()) {
                continue;
            }

            threeObjectLosingResolve(upperBracketEntry, lowerEntry, higherEntry);
            threeObjectBye(upperBracketEntry, lowerEntry, higherEntry, upperBracketEntry.isBye(), lowerEntry.isBye(), false);

        }
    }

    protected void threeObjectLosingResolve(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry) {

        if (topEntry.getGame() != null) {
            if (higherEntry.getCompetitor1() == null) {
                higherEntry.setCompetitor1(getLoser(topEntry));
            }
        } else {
            if (topEntry.isForfeited() && bottomEntry.isFinal()) {
                higherEntry.setBye(true);
            }
        }

        if (bottomEntry.getGame() != null) {
            if (higherEntry.getCompetitor2() == null) {
                higherEntry.setCompetitor2(getWinner(bottomEntry));
            }
        } else {
            if (bottomEntry.isForfeited()) {
                higherEntry.setCompetitor2(bottomEntry.getCompetitor1().getId().equals(bottomEntry.getForfeitedId()) ? bottomEntry.getCompetitor2() : bottomEntry.getCompetitor1());
            }
        }
    }



    protected void resolveSingleEliminationAfterGame() {
        BracketEntryType currentRow = size;
        while (!currentRow.equals(BracketEntryType.RO_2)) {
            getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
            int absoluteCoord = BracketEntryType.getSingleEliminationCountAbove(currentRow);
            int higherAbsoluteCoord = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.getHigher(currentRow)) - BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.getHigher(currentRow)) - 1;
            for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                BracketEntry topEntry = getBracketEntry(absoluteCoord + i);
                BracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
                BracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);

                if (higherEntry.isFinal()) {
                    continue;
                }

                threeObjectResolve(topEntry, bottomEntry, higherEntry);
                threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.isBye(), bottomEntry.isBye(), true);

            }
            currentRow = BracketEntryType.getHigher(currentRow);

        }
        checkWinCondition();
    }

    protected abstract void checkWinCondition();

    protected void threeObjectBye(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry, boolean bye, boolean bye2, boolean copyFromTopOnBye) {
        threeObjectBye(topEntry, bottomEntry, higherEntry, bye, bye2, copyFromTopOnBye, false);
    }

    protected void threeObjectBye(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry, boolean bye, boolean bye2, boolean copyFromTopOnBye, boolean reversePlayers) {
        Competitor competitor1;
        Competitor competitor2 ;
        Competitor competitor12;
        Competitor competitor22 ;

        if (!reversePlayers) {
            competitor1 = topEntry.getCompetitor1();
            competitor2 = topEntry.getCompetitor2();
            competitor12 = bottomEntry.getCompetitor1();
            competitor22 = bottomEntry.getCompetitor2();
        } else {
            competitor1 = bottomEntry.getCompetitor1();
            competitor2 = bottomEntry.getCompetitor2();
            competitor12 = topEntry.getCompetitor1();
            competitor22 = topEntry.getCompetitor2();
        }
        if (bottomEntry.isBye() && topEntry.isBye()) {
            if (copyFromTopOnBye) {
                higherEntry.setCompetitor1(competitor1 == null ? competitor2 : competitor1);
            }
            higherEntry.setCompetitor2(competitor12 == null ? competitor22 : competitor12);
            if (higherEntry.getCompetitor1() == null || higherEntry.getCompetitor2() == null) {
                higherEntry.setBye(true);
                higherEntry.setFinal(true);
            }
        } else if (bye || bye2) {
            if (bye) {
                if (copyFromTopOnBye) {
                    higherEntry.setCompetitor1(competitor1 == null ? competitor2 : competitor1);
                }
                if (higherEntry.getCompetitor1() == null && higherEntry.getCompetitor2() != null) {
                    higherEntry.setFinal(true);
                    higherEntry.setBye(true);
                }
            } else {
                higherEntry.setCompetitor2(competitor12 == null ? competitor22 : competitor12);
                if (higherEntry.getCompetitor2() == null && higherEntry.getCompetitor1() != null) {
                    higherEntry.setFinal(true);
                    higherEntry.setBye(true);
                }
            }
        }
    }

    protected Competitor getWinner(BracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getCompetitor1().getId().equals(entry.getGame().getWinnerId()) ? entry.getCompetitor1() : entry.getCompetitor2();
        } else if (entry.isForfeited()) {
            return entry.getCompetitor1().getId().equals(entry.getForfeitedId()) ? entry.getCompetitor2() : entry.getCompetitor1();
        } else {
            return null;
        }
    }

    protected Competitor getLoser(BracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getCompetitor1().getId().equals(entry.getGame().getWinnerId()) ? entry.getCompetitor2() : entry.getCompetitor1();
        } else {
            return null;
        }
    }



    protected void resolveByesInARow(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord, int i) {
        BracketEntry topEntry = getBracketEntry(absoluteCoord + i);
        BracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
        topEntry.setBracketEntryType(currentRow);
        bottomEntry.setBracketEntryType(currentRow);
        BracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);
        threeObjectBye(bottomEntry, topEntry, higherEntry, topEntry.isBye(), bottomEntry.isBye(), true, true);
    }

    @Override
    protected void populateEntryList() {
        createCompetitorsArray();
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            populateSingleElimination();
        } else if (tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {
            populateDoubleElimination();
        }
    }

    private void populateSingleElimination() {
        List<BracketEntry> abstractBracketEntries = new ArrayList<>();
        int number = BracketEntryType.getSingleEliminationCountAboveAndEqual(size);
        for (int i = 0; i < number; i++) {
            BracketEntry abstractBracketEntry = createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        abstractBracketEntries.get(0).setBracketEntryType(BracketEntryType.RO_2);
        setBracketEntries(abstractBracketEntries);
    }

    @Override
    public void resolveByes() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            BracketEntryType currentRow = size;
            while (!currentRow.equals(BracketEntryType.RO_2)) {
                getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                int absoluteCoord = BracketEntryType.getSingleEliminationCountAbove(currentRow);
                int higherAbsoluteCoord = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.getHigher(currentRow)) - BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.getHigher(currentRow)) - 1;
                for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                    resolveByesInARow(currentRow, absoluteCoord, higherAbsoluteCoord, i);
                }
                currentRow = BracketEntryType.getHigher(currentRow);

            }
        } else if (tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {

            BracketEntryType currentRow = size;
            while (!currentRow.equals(BracketEntryType.D_RO_1)) {
                getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                if (BracketEntryType.isPowerOf2(currentRow)) {
                    if (!currentRow.equals(BracketEntryType.D_RO_2)) {
                        int absoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(currentRow, true);
                        int higherAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigherPowerOf2(currentRow), true);
                        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, true); i += 2) {
                            resolveByesInARow(currentRow, absoluteCoord, higherAbsoluteCoord, i);
                        }
                    }

                    if (!currentRow.equals(size)) {
                        int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
                            BracketEntry entry = getBracketEntry(lowerAbsoluteCoord + i);
                            entry.setBracketEntryType(currentRow);
                        }
                    }
                } else {
                    int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
                        BracketEntry entry = getBracketEntry(lowerAbsoluteCoord + i);
                        entry.setBracketEntryType(currentRow);
                    }
                }
                currentRow = BracketEntryType.getHigher(currentRow);

            }
        }

    }

    public static boolean isPowerOfFour(int num) {
        return (((num & (num - 1)) == 0)    // check whether num is a power of 2
                && ((num & 0xaaaaaaaa) == 0));  // make sure it's an even power of 2
    }

    @Override
    public void resolveAfterGame() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            resolveSingleEliminationAfterGame();
        } else if (tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {
            resolveDoubleEliminationAfterGame();
        }
    }



    private void populateDoubleElimination() {
        List<BracketEntry> abstractBracketEntries = new ArrayList<>();
        int highNumber = BracketEntryType.getDoubleEliminationCountAboveAndEqual(size, true);
        int lowNumber;
        lowNumber = 1000 + BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.getHigher(size), false);

        for (int i = 0; i < highNumber; i++) {
            BracketEntry abstractBracketEntry = createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        for (int i = 1000; i < lowNumber; i++) {
            BracketEntry abstractBracketEntry = createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        setBracketEntries(abstractBracketEntries);
        getBracketEntry(0).setBracketEntryType(BracketEntryType.D_RO_1);
        getBracketEntry(1).setBracketEntryType(BracketEntryType.D_RO_2);
    }

    protected BracketEntry getBracketEntry(int coord) {
        return getBracketEntries().stream().filter(entry -> entry.getCoordinate() == coord).findFirst().get();
    }

    protected int getUpperBracketCoord(int lowerBracketCoord, BracketEntryType row) {
        int upperAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getLower(row), true);

        if (isPowerOfFour(BracketEntryType.getNumberInName(BracketEntryType.getHigher(row)))) {
            int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(row, false);
            return upperAbsoluteCoord + lowerBracketCoord - lowerAbsoluteCoord;
        } else {
            int rowStart = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(row), false) - 1;
            int offset = lowerBracketCoord - rowStart;
            return upperAbsoluteCoord - offset;
        }
    }
}
