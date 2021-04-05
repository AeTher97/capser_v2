package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@MappedSuperclass
public abstract class AbstractSinglesTournament<T extends AbstractSinglesGame> extends Tournament<T> {


    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    protected List<UserBridge> players;


    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private UserBridge winner;

    @Override
    protected void populateEntryList() {
        players = new ArrayList<>();
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            populateSingleElimination();
        } else if (tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {
            populateDoubleElimination();
        }
    }

    private void populateSingleElimination() {
        List<AbstractSinglesBracketEntry> abstractBracketEntries = new ArrayList<>();
        int number = BracketEntryType.getSingleEliminationCountAboveAndEqual(size);
        for (int i = 0; i < number; i++) {
            AbstractSinglesBracketEntry abstractBracketEntry = createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        abstractBracketEntries.get(0).setBracketEntryType(BracketEntryType.RO_2);
        setBracketEntries(abstractBracketEntries);
    }

    private void populateDoubleElimination() {
        List<AbstractSinglesBracketEntry> abstractBracketEntries = new ArrayList<>();
        int highNumber = BracketEntryType.getDoubleEliminationCountAboveAndEqual(size, true);
        int lowNumber;
        lowNumber = 1000 + BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.getHigher(size), false);

        for (int i = 0; i < highNumber; i++) {
            AbstractSinglesBracketEntry abstractBracketEntry = createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        for (int i = 1000; i < lowNumber; i++) {
            AbstractSinglesBracketEntry abstractBracketEntry = createBracketEntry();
            abstractBracketEntry.setCoordinate(i);
            abstractBracketEntries.add(abstractBracketEntry);
        }
        setBracketEntries(abstractBracketEntries);
        getBracketEntry(0).setBracketEntryType(BracketEntryType.D_RO_1);
        getBracketEntry(1).setBracketEntryType(BracketEntryType.D_RO_2);
    }

    @Override
    public void doSeedPlayers() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION) || tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {
            if (seedType.equals(SeedType.RANDOM)) {
                int playersCount = players.size();
                int lastRowCoordinateStart;

                if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
                    lastRowCoordinateStart = BracketEntryType.getSingleEliminationCountAbove(size);
                } else {
                    lastRowCoordinateStart = BracketEntryType.getDoubleEliminationCountAbove(size, true);
                }

                List<User> randomPlayerList = players.stream().map(UserBridge::getUser).collect(Collectors.toList());
                Collections.shuffle(randomPlayerList);

                int loopSize;
                if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
                    loopSize = size.getValue() / 2;
                } else {
                    loopSize = BracketEntryType.getDoubleEliminationCountInRow(size, true);
                }

                for (int i = 0; i < loopSize; i += 2) {
                    getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                    AbstractSinglesBracketEntry topEntry = getBracketEntry(lastRowCoordinateStart + i / 2);
                    int bottomEntryId;
                    if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
                        bottomEntryId = lastRowCoordinateStart + size.getValue() / 2 - 1 - i / 2;
                    } else {
                        bottomEntryId = lastRowCoordinateStart + BracketEntryType.getDoubleEliminationCountInRow(size, true) - 1 - i / 2;
                    }
                    AbstractSinglesBracketEntry bottomEntry = getBracketEntry(bottomEntryId);
                    if (i * 2 < playersCount) {
                        topEntry.setPlayer1(randomPlayerList.get(i * 2));
                    } else {
                        topEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 1 < playersCount) {
                        bottomEntry.setPlayer2(randomPlayerList.get(i * 2 + 1));
                    } else {
                        bottomEntry.setBye(true);
                        bottomEntry.setFinal(true);
                    }
                    if (i * 2 + 2 < playersCount) {
                        topEntry.setPlayer2(randomPlayerList.get(i * 2 + 2));
                    } else {
                        topEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 3 < playersCount) {
                        bottomEntry.setPlayer1(randomPlayerList.get(i * 2 + 3));
                    } else {
                        bottomEntry.setBye(true);
                        bottomEntry.setFinal(true);
                    }
                }
            } else if (seedType.equals(SeedType.ELO)) {

            }
        }
    }

    @Override
    public void resolveAfterGame() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            resolveSingleEliminationAfterGame();
        } else if (tournamentType.equals(TournamentType.DOUBLE_ELIMINATION)) {
            resolveDoubleEliminationAfterGame();
        }
    }

    private void resolveSingleEliminationAfterGame() {
        BracketEntryType currentRow = size;
        while (!currentRow.equals(BracketEntryType.RO_2)) {
            getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
            int absoluteCoord = BracketEntryType.getSingleEliminationCountAbove(currentRow);
            int higherAbsoluteCoord = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.getHigher(currentRow)) - BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.getHigher(currentRow)) - 1;
            for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                AbstractSinglesBracketEntry topEntry = getBracketEntry(absoluteCoord + i);
                AbstractSinglesBracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
                AbstractSinglesBracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);

                if (higherEntry.isFinal()) {
                    continue;
                }

                threeObjectResolve(topEntry, bottomEntry, higherEntry);
                threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.getPlayer1(), topEntry.getPlayer2(), bottomEntry.getPlayer1(), bottomEntry.getPlayer2(), topEntry.isBye(), bottomEntry.isBye(),true);

            }
            currentRow = BracketEntryType.getHigher(currentRow);

        }
        checkWinCondition();
    }

    private void resolveDoubleEliminationAfterGame() {
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

                    resolvePowerOfTwoLevel(currentRow, bottomAbsoluteCoord, bottomHigherAbsoluteCoord);
                }

            } else {
                int absoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                int higherAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(currentRow), false);

                if (currentRow.equals(BracketEntryType.getHigher(size))) {
                    int upperCoord = BracketEntryType.getDoubleEliminationCountAbove(size, true);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i++) {
                        AbstractSinglesBracketEntry upperEntry1 = getBracketEntry(upperCoord + i * 2);
                        AbstractSinglesBracketEntry upperEntry2 = getBracketEntry(upperCoord + i * 2 + 1);
                        AbstractSinglesBracketEntry lowerEntry = getBracketEntry(absoluteCoord + i);

                        if (upperEntry1.getGame() != null) {
                            lowerEntry.setPlayer1(getLoser(upperEntry1));
                        }
                        if (upperEntry2.getGame() != null) {
                            lowerEntry.setPlayer2(getLoser(upperEntry2));
                        }
                        if (upperEntry1.isFinal() && upperEntry2.isFinal()) {
                            if (upperEntry1.isBye() || upperEntry2.isBye())
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

    private int getUpperBracketCoord(int lowerBracketCoord, BracketEntryType row) {
        int upperAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getLower(row), true);
        int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(row, false);
        return upperAbsoluteCoord + lowerBracketCoord - lowerAbsoluteCoord;
    }

    private void resolvePowerOfTwoLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord) {
        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, true); i += 2) {
            AbstractSinglesBracketEntry topEntry = getBracketEntry(absoluteCoord + i);
            AbstractSinglesBracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
            if (currentRow.equals(BracketEntryType.D_RO_2)) {
                bottomEntry = getBracketEntry(1000);
            }
            AbstractSinglesBracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);

            if (higherEntry.isFinal()) {
                continue;
            }

            threeObjectResolve(topEntry, bottomEntry, higherEntry);
            threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.getPlayer1(), topEntry.getPlayer2(), bottomEntry.getPlayer1(), bottomEntry.getPlayer2(), topEntry.isBye(), bottomEntry.isBye(),true);

        }
    }

    private void resolveOddLevel(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord) {
        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
            AbstractSinglesBracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i);
            AbstractSinglesBracketEntry lowerEntry = getBracketEntry(absoluteCoord + i);
            AbstractSinglesBracketEntry upperBracketEntry = getBracketEntry(getUpperBracketCoord(higherAbsoluteCoord + i, currentRow));

            if (higherEntry.isFinal()) {
                continue;
            }

            threeObjectLosingResolve(upperBracketEntry, lowerEntry, higherEntry);
            threeObjectBye(upperBracketEntry, lowerEntry, higherEntry, upperBracketEntry.getPlayer1(), upperBracketEntry.getPlayer2(), lowerEntry.getPlayer1(), lowerEntry.getPlayer2(), upperBracketEntry.isBye(), lowerEntry.isBye(),false);

        }
    }

    private void checkWinCondition() {
        AbstractSinglesBracketEntry entry = getBracketEntry(0);
        if (entry.getCoordinate() == 0) {
            if (entry.getGame() != null) {
                setFinished(true);
                setWinner(entry.getPlayer1().getId().equals(entry.getGame().getWinner()) ? new UserBridge(entry.getPlayer1()) : new UserBridge(entry.getPlayer2()));
            } else if (entry.isForfeited()) {
                setFinished(true);
                setWinner(entry.getPlayer1().getId().equals(entry.getForfeitedId()) ? new UserBridge(entry.getPlayer2()) : new UserBridge(entry.getPlayer1()));
            }
        }
    }

    private void threeObjectResolve(AbstractSinglesBracketEntry topEntry, AbstractSinglesBracketEntry bottomEntry, AbstractSinglesBracketEntry higherEntry) {


        if (topEntry.getGame() != null) {
            if (higherEntry.getPlayer1() == null) {
                higherEntry.setPlayer1(getWinner(topEntry));
            }
        } else {
            if (topEntry.isForfeited()) {
                higherEntry.setPlayer1(topEntry.getPlayer1().getId().equals(topEntry.getForfeitedId()) ? topEntry.getPlayer2() : topEntry.getPlayer1());
            }
        }

        if (bottomEntry.getGame() != null) {
            if (higherEntry.getPlayer2() == null) {
                higherEntry.setPlayer2(getWinner(bottomEntry));
            }
        } else {
            if (bottomEntry.isForfeited()) {
                higherEntry.setPlayer2(bottomEntry.getPlayer1().getId().equals(bottomEntry.getForfeitedId()) ? bottomEntry.getPlayer2() : bottomEntry.getPlayer1());
            }
        }
    }

    private void threeObjectLosingResolve(AbstractSinglesBracketEntry topEntry, AbstractSinglesBracketEntry bottomEntry, AbstractSinglesBracketEntry higherEntry) {

        if (topEntry.getGame() != null) {
            if (higherEntry.getPlayer1() == null) {
                higherEntry.setPlayer1(getLoser(topEntry));
            }
        }

        if (bottomEntry.getGame() != null) {
            if (higherEntry.getPlayer2() == null) {
                higherEntry.setPlayer2(getLoser(bottomEntry));
            }
        }
    }

    private User getWinner(AbstractSinglesBracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getPlayer1().getId().equals(entry.getGame().getWinner()) ? entry.getPlayer1() : entry.getPlayer2();
        } else if (entry.isForfeited()) {
            return entry.getPlayer1().getId().equals(entry.getForfeitedId()) ? entry.getPlayer2() : entry.getPlayer1();
        } else {
            return null;
        }
    }

    private User getLoser(AbstractSinglesBracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getPlayer1().getId().equals(entry.getGame().getWinner()) ? entry.getPlayer2() : entry.getPlayer1();
        } else {
            return null;
        }
    }

    private void threeObjectBye(AbstractSinglesBracketEntry topEntry, AbstractSinglesBracketEntry bottomEntry, AbstractSinglesBracketEntry higherEntry, User player1, User player2, User player12, User player22, boolean bye, boolean bye2, boolean copyFromTopOnBye) {
        if (bottomEntry.isBye() && topEntry.isBye()) {
            if(copyFromTopOnBye) {
                higherEntry.setPlayer1(player1 == null ? player2 : player1);
            }
            higherEntry.setPlayer2(player12 == null ? player22 : player12);
            if (higherEntry.getPlayer1() == null || higherEntry.getPlayer2() == null) {
                higherEntry.setBye(true);
                higherEntry.setFinal(true);
            }
        } else if (bye || bye2) {
            if (bye) {
                if(copyFromTopOnBye) {
                    higherEntry.setPlayer1(player1 == null ? player2 : player1);
                }
                if (higherEntry.getPlayer1() == null && higherEntry.getPlayer2() != null) {
                    higherEntry.setFinal(true);
                    higherEntry.setBye(true);
                }
            } else {
                higherEntry.setPlayer2(player12 == null ? player22 : player12);
                if (higherEntry.getPlayer2() == null && higherEntry.getPlayer1() != null) {
                    higherEntry.setFinal(true);
                    higherEntry.setBye(true);
                }
            }
        }
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
                    AbstractSinglesBracketEntry topEntry = getBracketEntry(absoluteCoord + i);
                    AbstractSinglesBracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
                    topEntry.setBracketEntryType(currentRow);
                    bottomEntry.setBracketEntryType(currentRow);
                    AbstractSinglesBracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);

                    threeObjectBye(bottomEntry, topEntry, higherEntry, topEntry.getPlayer1(), topEntry.getPlayer2(), bottomEntry.getPlayer1(), bottomEntry.getPlayer2(), topEntry.isBye(), bottomEntry.isBye(),true);
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
                            AbstractSinglesBracketEntry topEntry = getBracketEntry(absoluteCoord + i);
                            AbstractSinglesBracketEntry bottomEntry = getBracketEntry(absoluteCoord + i + 1);
                            topEntry.setBracketEntryType(currentRow);
                            bottomEntry.setBracketEntryType(currentRow);
                            AbstractSinglesBracketEntry higherEntry = getBracketEntry(higherAbsoluteCoord + i / 2);

                            threeObjectBye(bottomEntry, topEntry, higherEntry, topEntry.getPlayer1(), topEntry.getPlayer2(), bottomEntry.getPlayer1(), bottomEntry.getPlayer2(), topEntry.isBye(), bottomEntry.isBye(),true);
                        }
                    }

                    if (!currentRow.equals(size)) {
                        int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                        for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
                            AbstractSinglesBracketEntry entry = getBracketEntry(lowerAbsoluteCoord + i);
                            entry.setBracketEntryType(currentRow);
                        }
                    }
                } else {
                    int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(currentRow, false);
                    for (int i = 0; i < BracketEntryType.getDoubleEliminationCountInRow(currentRow, false); i += 1) {
                        AbstractSinglesBracketEntry entry = getBracketEntry(lowerAbsoluteCoord + i);
                        entry.setBracketEntryType(currentRow);
                    }
                }
                currentRow = BracketEntryType.getHigher(currentRow);

            }
        }

    }


    private AbstractSinglesBracketEntry getBracketEntry(int coord) {
        return getBracketEntries().stream().filter(entry -> entry.getCoordinate() == coord).findFirst().get();
    }

    public abstract List<? extends AbstractSinglesBracketEntry> getBracketEntries();

    protected abstract void setBracketEntries(List<AbstractSinglesBracketEntry> entries);

    protected abstract AbstractSinglesBracketEntry createBracketEntry();
}
