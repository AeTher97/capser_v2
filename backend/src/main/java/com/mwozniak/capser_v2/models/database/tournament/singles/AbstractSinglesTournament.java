package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@MappedSuperclass
public abstract class AbstractSinglesTournament extends Tournament {


    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    protected List<UserBridge> players;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private UserBridge winner;

    @Override
    public List<Competitor> getCompetitorList() {
        return players.stream().map(UserBridge::getUser).collect(Collectors.toList());
    }


    @Override
    public void setWinner(UUID id) {
        winner = players.stream().filter(player -> player.getUser().getId().equals(id)).findFirst().get();
    }

    @Override
    public abstract List<BracketEntry> getBracketEntries();

    @Override
    protected void createCompetitorsArray() {
        players = new ArrayList<>();
    }
}
