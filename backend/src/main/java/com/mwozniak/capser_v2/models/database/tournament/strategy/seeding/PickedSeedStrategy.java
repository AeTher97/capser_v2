package com.mwozniak.capser_v2.models.database.tournament.strategy.seeding;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.CompetitorTournamentStats;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;

import java.util.ArrayList;
import java.util.List;

public class PickedSeedStrategy implements SeedingStrategy {

    @Override
    public void seedPlayers(Tournament tournament) {
        if (tournament.getCompetitorTournamentStats() == null) {
            tournament.setCompetitorTournamentStats(new ArrayList<>());
        }

        List<CompetitorTournamentStats> competitorTournamentStats = tournament.getCompetitorTournamentStats();

        for (Competitor competitor : tournament.getCompetitorList()) {
            competitorTournamentStats.add(CompetitorTournamentStats.builder()
                    .competitorId(competitor.getId())
                    .build());
        }

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

            setFinalAndByeOnEntries(topEntry, bottomEntry);
        }
    }


    private void setFinalAndByeOnEntries(BracketEntry topEntry, BracketEntry bottomEntry) {
        setFinalAndByeForEntry(topEntry);
        setFinalAndByeForEntry(bottomEntry);
    }

    private void setFinalAndByeForEntry(BracketEntry bracketEntry) {
        bracketEntry.setFinal(true);
        if (bracketEntry.getCompetitor1() == null || bracketEntry.getCompetitor2() == null) {
            bracketEntry.setBye(true);
        }
    }


}
