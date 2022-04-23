package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.DoubleEliminationStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.EliminationStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.elimination.SingleEliminationStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.seeding.RandomSeedStrategy;
import com.mwozniak.capser_v2.models.database.tournament.strategy.seeding.SeedingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
@AllArgsConstructor
@Getter
public abstract class Tournament<T extends AbstractGame> {


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


    public Tournament() {
        date = new Date();
    }

    public abstract GameType getGameType();

    public static boolean isPowerOfFour(int num) {
        return (((num & (num - 1)) == 0)    // check whether num is a power of 2
                && ((num & 0xaaaaaaaa) == 0));  // make sure it's an even power of 2
    }

    protected abstract void createCompetitorsArray();

    public abstract List<? extends BracketEntry> getBracketEntries();

    public abstract void setBracketEntries(List<BracketEntry> entries);

    public abstract List<UserBridge> getPlayers();

    protected abstract void checkWinCondition();

    public abstract BracketEntry createBracketEntry();

    public abstract List<Competitor> getCompetitorList();

    @JsonIgnore
    public SeedingStrategy getSeedingStrategy() {
        // #TODO more strategies
        return new RandomSeedStrategy();
    }

    @JsonIgnore
    public EliminationStrategy getEliminationStrategy() {
        switch (tournamentType) {
            case DOUBLE_ELIMINATION:
                return new DoubleEliminationStrategy(this);
            case SINGLE_ELIMINATION:
                return new SingleEliminationStrategy(this);
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

        checkWinCondition();
    }

    ;


    public void setSize(BracketEntryType bracketEntryType) {
        size = bracketEntryType;
        populateEntryList();
    }

    public final void seedPlayers() {
        if (isSeeded) {
            throw new IllegalStateException("Tournament already seeded");
        }
        isSeeded = true;
        getSeedingStrategy().seedPlayers(this);
        resolveByes();
    }

    public BracketEntry getBracketEntry(int coord) {
        return getBracketEntries().stream().filter(entry -> entry.getCoordinate() == coord).findFirst().get();
    }

    public Competitor getWinner(BracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getCompetitor1().getId().equals(entry.getGame().getWinnerId()) ? entry.getCompetitor1() : entry.getCompetitor2();
        } else if (entry.isForfeited()) {
            return entry.getCompetitor1().getId().equals(entry.getForfeitedId()) ? entry.getCompetitor2() : entry.getCompetitor1();
        } else {
            return null;
        }
    }

    public Competitor getLoser(BracketEntry entry) {
        if (entry.getGame() != null) {
            return entry.getCompetitor1().getId().equals(entry.getGame().getWinnerId()) ? entry.getCompetitor2() : entry.getCompetitor1();
        } else {
            return null;
        }
    }

    public static class Comparators {

        public static final Comparator<Tournament<?>> DATE = Comparator.comparing(Tournament::getDate);
    }

}
