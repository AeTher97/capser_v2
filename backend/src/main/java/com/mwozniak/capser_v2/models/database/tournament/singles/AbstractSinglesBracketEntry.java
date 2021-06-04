package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
@Getter
public abstract class AbstractSinglesBracketEntry extends BracketEntry {

    @OneToOne
    @Setter
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private User player1;

    @OneToOne
    @Setter
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private User player2;

    public abstract AbstractSinglesGame getGame();



    @Override
    public void setCompetitor1(Competitor competitor) {
        setPlayer1((User)competitor);
    }

    @Override
    public void setCompetitor2(Competitor competitor) {
        setPlayer2((User)competitor);
    }

    @Override
    public Competitor getCompetitor1() {
        return player1;
    }

    @Override
    public Competitor getCompetitor2() {
        return player2;
    }
}
