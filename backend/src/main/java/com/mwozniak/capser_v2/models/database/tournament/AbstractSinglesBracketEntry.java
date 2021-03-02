package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class AbstractSinglesBracketEntry extends BracketEntry{

    @OneToOne
    @Setter
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private User player1;

    @OneToOne
    @Setter
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private User player2;

    public abstract AbstractSinglesGame getGame();

    @Setter
    private boolean forfeited;

    @Setter
    private UUID forfeitedId;

    public void forfeitGame(User user) {
        forfeited = true;
        setFinal(true);
        if (user.equals(player1)) {
            forfeitedId = player1.getId();
        } else if (user.equals(player2)) {
            forfeitedId = player2.getId();
        } else {
            throw new IllegalStateException("User with id " + user.getId() + " not found in this game");
        }
    }

}
