package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.DoubleEliminationStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.EliminationStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.RoundRobinStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.SingleEliminationStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.seeding.PickedSeedStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.seeding.RandomSeedStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.seeding.RoundRobinSeedingStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.seeding.SeedingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
@AllArgsConstructor
@Getter
public abstract class Tournament {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Enumerated(EnumType.STRING)
    @Setter
    protected TournamentType tournamentType;

    @Enumerated(EnumType.STRING)
    @Getter
    protected BracketEntryType size;

    @Enumerated(EnumType.STRING)
    @Setter
    protected SeedType seedType;

    @Setter
    protected String tournamentName;

    @Setter
    protected boolean isFinished;

    @Setter
    protected boolean isSeeded;

    @Setter
    private UUID owner;

    private Date date;

    @OneToMany
    @Setter
    @Getter
    @Cascade(CascadeType.ALL)
    @JsonIgnore
    private List<CompetitorTournamentStats> competitorTournamentStats;


    protected Tournament() {
        date = new Date();
    }

    public abstract GameType getGameType();

    protected abstract void createCompetitorsArray();

    public static boolean isPowerOfFour(int num) {
        return (((num & (num - 1)) == 0)    // check whether num is a power of 2
                && ((num & 0xaaaaaaaa) == 0));  // make sure it's an even power of 2
    }

    public abstract void setBracketEntries(List<BracketEntry> entries);

    public abstract List<UserBridge> getPlayers();

    public abstract BracketEntry createBracketEntry();

    public abstract List<BracketEntry> getBracketEntries();

    @JsonIgnore
    public abstract List<Competitor> getCompetitorList();

    public abstract void setWinner(UUID id);

    @JsonProperty("competitorTournamentStats")
    public List<CompetitorTournamentStats> competitorStats() {
        if (competitorTournamentStats != null) {
            competitorTournamentStats.sort(CompetitorTournamentStats.Comparators.DRAW_RESOLVE_CONDITIONS);

        }
        return competitorTournamentStats;
    }

    @JsonIgnore
    public SeedingStrategy getSeedingStrategy() {
        if (seedType.equals(SeedType.ROUND_ROBIN_CIRCLE)) {
            return new RoundRobinSeedingStrategy();
        } else if (seedType.equals(SeedType.PICKED)) {
            return new PickedSeedStrategy();
        } else {
            return new RandomSeedStrategy();
        }
    }

    @JsonIgnore
    public EliminationStrategy getEliminationStrategy() {
        switch (tournamentType) {
            case DOUBLE_ELIMINATION:
                return new DoubleEliminationStrategy(this);
            case SINGLE_ELIMINATION:
                return new SingleEliminationStrategy(this);
            case ROUND_ROBIN:
                return new RoundRobinStrategy();
        }
        return null;
    }

    protected void populateEntryList() {
        createCompetitorsArray();
        getEliminationStrategy().populateEntryList(this);
    }

    public void resolveByes() {
        getEliminationStrategy().resolveByes(this);
    }

    public void resolveAfterGame() {
        getEliminationStrategy().resolveAfterGame(this);
        getEliminationStrategy().checkWinCondition(this);
    }


    public void setSize(BracketEntryType bracketEntryType) {
        size = bracketEntryType;
        populateEntryList();
    }

    public final void seedPlayers() {
        if (isSeeded) {
            throw new IllegalStateException("Tournament already seeded");
        }
        getSeedingStrategy().seedPlayers(this);
        isSeeded = true;
        resolveByes();
    }

    public void setSeeds(List<? extends BracketEntry> bracketEntries) {
        bracketEntries.forEach(bracketEntry -> {
            BracketEntry entryToModify = getBracketEntries().stream().filter(entry -> entry.getCoordinate() == bracketEntry.getCoordinate()).findFirst().get();
            if (bracketEntry.getCompetitor1() != null) {
                Competitor competitor1 = getCompetitorList().stream().filter(competitor -> competitor.getId().equals(bracketEntry.getCompetitor1().getId())).findFirst().get();
                entryToModify.setCompetitor1(competitor1);
            }
            if (bracketEntry.getCompetitor2() != null) {
                Competitor competitor2 = getCompetitorList().stream().filter(competitor -> competitor.getId().equals(bracketEntry.getCompetitor2().getId())).findFirst().get();
                entryToModify.setCompetitor2(competitor2);
            }
        });
    }

    public Optional<CompetitorTournamentStats> getCompetitorTournamentStatsForId(UUID id) {
        return getCompetitorTournamentStats().stream()
                .filter(competitorTournamentStats -> id.equals(competitorTournamentStats.getCompetitorId())).findAny();
    }


    public BracketEntry getBracketEntry(int coord) {
        return getBracketEntries().stream().filter(entry -> entry.getCoordinate() == coord).findFirst().get();
    }

    public Competitor getWinner(BracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getCompetitor1().getId().equals(entry.getGame().getWinner()) ? entry.getCompetitor1() : entry.getCompetitor2();
        } else if (entry.isForfeited()) {
            return entry.getCompetitor1().getId().equals(entry.getForfeitedId()) ? entry.getCompetitor2() : entry.getCompetitor1();
        } else {
            return null;
        }
    }

    public Competitor getLoser(BracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getCompetitor1().getId().equals(entry.getGame().getWinner()) ? entry.getCompetitor2() : entry.getCompetitor1();
        } else {
            return null;
        }
    }

    public static class Comparators {

        public static final Comparator<Tournament> DATE = Comparator.comparing(Tournament::getDate);

        private Comparators() {
        }
    }

}
