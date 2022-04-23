package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@MappedSuperclass
public abstract class AbstractSinglesTournament<T extends AbstractSinglesGame> extends Tournament<T> {


    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    protected List<UserBridge> players;


    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private UserBridge winner;

    @Override
    public List<Competitor> getCompetitorList() {
        return players.stream().map(UserBridge::getUser).collect(Collectors.toList());
    }


    @Override
    protected void checkWinCondition() {
        AbstractSinglesBracketEntry entry = (AbstractSinglesBracketEntry) getBracketEntry(0);
        if (entry.getCoordinate() == 0) {
            if (entry.getGame() != null) {
                setFinished(true);
                setWinner(entry.getPlayer1().getId().equals(entry.getGame().getWinner()) ? new UserBridge(entry.getPlayer1()) : new UserBridge(entry.getPlayer2()));
            } else if (entry.isForfeited()) {
                setFinished(true);
                setWinner(entry.getPlayer1().getId().equals(entry.getForfeitedId()) ? new UserBridge(entry.getPlayer2()) : new UserBridge(entry.getPlayer1()));
            }
        }
    }



    @Override
    public abstract List<? extends AbstractSinglesBracketEntry> getBracketEntries();

    @Override
    protected void createCompetitorsArray() {
        players = new ArrayList<>();
    }
}
