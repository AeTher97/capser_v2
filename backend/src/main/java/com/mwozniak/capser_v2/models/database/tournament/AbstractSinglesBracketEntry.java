package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.*;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
@Getter
public abstract class AbstractSinglesBracketEntry extends BracketEntry{

    @OneToOne
    @Setter
    @JsonIgnoreProperties(value = {"userSinglesStats","userEasyStats","userUnrankedStats","userDoublesStats","teams","lastSeen","lastGame","role"})
    private User player1;

    @OneToOne
    @Setter
    @JsonIgnoreProperties(value = {"userSinglesStats","userEasyStats","userUnrankedStats","userDoublesStats","teams","lastSeen","lastGame","role"})
    private User player2;

    public abstract AbstractSinglesGame getGame();

}
