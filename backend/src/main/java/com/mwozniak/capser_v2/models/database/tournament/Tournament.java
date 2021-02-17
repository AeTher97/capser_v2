package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public Tournament(){
       date = new Date();
    }

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

    protected abstract GameType getGameType();

    protected abstract void populateEntryList();

    public final void seedPlayers(){
        if(isSeeded){
            throw new IllegalStateException("Tournament already seeded");
        }
        isSeeded = true;
        doSeedPlayers();
        resolveByes();
    };

    public abstract void doSeedPlayers();

    public abstract void resolveByes();

    public abstract void resolveAfterGame();


    public void setSize(BracketEntryType bracketEntryType){
        size = bracketEntryType;
        populateEntryList();
    }

    protected int getCoordinatesIdsBelow(BracketEntryType bracketEntryType){
        int number =0;
        for(BracketEntryType entry : BracketEntryType.values()){
            if(entry.getValue() > bracketEntryType.getValue()){
                number += entry.getValue()/2;
            }
        }
        return number;
    }

    protected int getCoordinatesIdsBelowAndEqual(BracketEntryType bracketEntryType){
        int number =0;
        for(BracketEntryType entry : BracketEntryType.values()){
            if(entry.getValue() >= bracketEntryType.getValue()){
                number += entry.getValue()/2;
            }
        }
        return number;
    }

    protected int getCoordinatesIdsAbove(BracketEntryType bracketEntryType){
        int number =0;
        for(BracketEntryType entry : BracketEntryType.values()){
            if(entry.getValue() < bracketEntryType.getValue()){
                number += entry.getValue()/2;
            }
        }
        return number;
    }

    protected int getCoordinatesIdsAboveAndEqual(BracketEntryType bracketEntryType){
        int number =0;
        for(BracketEntryType entry : BracketEntryType.values()){
            if(entry.getValue() <= bracketEntryType.getValue()){
                number += entry.getValue()/2;
            }
        }
        return number;
    }

    public static class Comparators {

        public static final Comparator<Tournament<?>> DATE = Comparator.comparing(Tournament::getDate);
    }

}
