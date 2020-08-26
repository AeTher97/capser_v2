package com.mwozniak.capser_v2.models.database.game;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractGame {

    public AbstractGame(){
        time = new Date();
        accepted = false;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    private UUID id;

    @Setter
    @Getter
    private boolean accepted;

    @Setter
    @Getter
    private boolean nakedLap;


    @Getter
    private final Date time;

    @Setter
    @Getter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected List<GamePlayerStats> gamePlayerStats;


    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEventEntity> gameEventList;

    public abstract GameType getGameType();

    public abstract GameMode getGameMode();

    public abstract void calculateStats() throws GameValidationException;

    public abstract void validateGame() throws GameValidationException;


}
