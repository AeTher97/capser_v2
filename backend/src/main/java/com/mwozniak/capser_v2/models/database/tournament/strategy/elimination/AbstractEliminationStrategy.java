package com.mwozniak.capser_v2.models.database.tournament.strategy.elimination;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

public abstract class AbstractEliminationStrategy implements EliminationStrategy {

    protected final Tournament tournament;

    public AbstractEliminationStrategy(Tournament tournament) {
        this.tournament = tournament;
    }


    protected void threeObjectResolve(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry) {
        if (topEntry.getGame() != null) {
            if (higherEntry.getCompetitor1() == null) {
                higherEntry.setCompetitor1(tournament.getWinner(topEntry));
            }
        } else {
            if (topEntry.isForfeited()) {
                higherEntry.setCompetitor1(topEntry.getCompetitor1().getId().equals(topEntry.getForfeitedId()) ? topEntry.getCompetitor2() : topEntry.getCompetitor1());
            }
        }
        if (bottomEntry.getGame() != null) {
            if (higherEntry.getCompetitor2() == null) {
                higherEntry.setCompetitor2(tournament.getWinner(bottomEntry));
            }
        } else {
            if (bottomEntry.isForfeited()) {
                higherEntry.setCompetitor2(bottomEntry.getCompetitor1().getId().equals(bottomEntry.getForfeitedId()) ? bottomEntry.getCompetitor2() : bottomEntry.getCompetitor1());
            }
        }
    }


    protected void threeObjectLosingResolve(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry) {

        if (topEntry.getGame() != null) {
            if (higherEntry.getCompetitor1() == null) {
                higherEntry.setCompetitor1(tournament.getLoser(topEntry));
            }
        } else {
            if (topEntry.isForfeited() && bottomEntry.isFinal()) {
                higherEntry.setBye(true);
            }
        }

        if (bottomEntry.getGame() != null) {
            if (higherEntry.getCompetitor2() == null) {
                higherEntry.setCompetitor2(tournament.getWinner(bottomEntry));
            }
        } else {
            if (bottomEntry.isForfeited()) {
                higherEntry.setCompetitor2(bottomEntry.getCompetitor1().getId().equals(bottomEntry.getForfeitedId()) ? bottomEntry.getCompetitor2() : bottomEntry.getCompetitor1());
            }
        }
    }


    protected void threeObjectBye(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry, boolean bye, boolean bye2, boolean copyFromTopOnBye) {
        threeObjectBye(topEntry, bottomEntry, higherEntry, bye, bye2, copyFromTopOnBye, false);
    }

    protected void threeObjectBye(BracketEntry topEntry, BracketEntry bottomEntry, BracketEntry higherEntry, boolean bye, boolean bye2, boolean copyFromTopOnBye, boolean reversePlayers) {
        Competitor competitor1;
        Competitor competitor2;
        Competitor competitor12;
        Competitor competitor22;

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


    protected void resolveByesInARow(BracketEntryType currentRow, int absoluteCoord, int higherAbsoluteCoord, int i) {
        BracketEntry topEntry = tournament.getBracketEntry(absoluteCoord + i);
        BracketEntry bottomEntry = tournament.getBracketEntry(absoluteCoord + i + 1);
        BracketEntry higherEntry = tournament.getBracketEntry(higherAbsoluteCoord + i / 2);
        threeObjectBye(bottomEntry, topEntry, higherEntry, topEntry.isBye(), bottomEntry.isBye(), true, true);
    }


    protected int getUpperBracketCoord(int lowerBracketCoord, BracketEntryType row) {
        int upperAbsoluteCoord = BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getLower(row), true);

        if (Tournament.isPowerOfFour(BracketEntryType.getNumberInName(BracketEntryType.getHigher(row)))) {
            int lowerAbsoluteCoord = 1000 + BracketEntryType.getDoubleEliminationCountAbove(row, false);
            return upperAbsoluteCoord + lowerBracketCoord - lowerAbsoluteCoord;
        } else {
            int rowStart = 1000 + BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.getHigher(row), false) - 1;
            int offset = lowerBracketCoord - rowStart;
            return upperAbsoluteCoord - offset;
        }
    }
}
