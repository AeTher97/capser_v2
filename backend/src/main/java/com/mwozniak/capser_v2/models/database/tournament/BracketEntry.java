package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.UUID;

@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class BracketEntry {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    private UUID id;

    @Setter
    private int coordinate;

    @Setter
    private boolean isBye;

    @Setter
    private boolean isFinal;

    @Setter
    @Enumerated(EnumType.STRING)
    private BracketEntryType bracketEntryType;


    @Setter
    private boolean forfeited;

    @Setter
    private UUID forfeitedId;


    public static class Comparators {

        public static final Comparator<BracketEntry> COORDINATE = Comparator.comparing(BracketEntry::getCoordinate);
    }

    public void forfeitGame(Competitor competitor) {
        forfeited = true;
        setFinal(true);
        if (competitor.equals(getCompetitor1())) {
            forfeitedId = getCompetitor1().getId();
        } else if (competitor.equals(getCompetitor2())) {
            forfeitedId = getCompetitor2().getId();
        } else {
            throw new IllegalStateException("Competitor with id " + competitor.getId() + " not found in this game");
        }
    }

    public abstract void setCompetitor1(Competitor competitor1);
    public abstract void setCompetitor2(Competitor competitor2);
    @JsonIgnore
    public abstract Competitor getCompetitor1();
    @JsonIgnore
    public abstract Competitor getCompetitor2();
    public abstract AbstractGame getGame();
    public boolean isForfeited(){
        return forfeited;
    }
    public UUID getForfeitedId(){
        return forfeitedId;
    }
}
