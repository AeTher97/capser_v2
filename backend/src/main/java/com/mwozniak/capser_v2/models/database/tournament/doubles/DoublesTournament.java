package com.mwozniak.capser_v2.models.database.tournament.doubles;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class DoublesTournament extends Tournament {

    public DoublesTournament() {

    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<DoublesBracketEntry> bracketEntries;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    protected List<TeamBridge> teams;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TeamBridge winner;

    @Override
    protected void createCompetitorsArray() {
        teams = new ArrayList<>();
    }

    @Override
    public List<Competitor> getCompetitorList() {
        return teams.stream().map(TeamBridge::getTeam).collect(Collectors.toList());
    }

    @Override
    public void setWinner(UUID id) {
        winner = teams.stream().filter(team -> team.getTeam().getId().equals(id)).findFirst().get();

    }


    @Override
    public GameType getGameType() {
        return GameType.DOUBLES;
    }

    @Override
    public List<BracketEntry> getBracketEntries() {
        return  (List<BracketEntry>) (List<?>) bracketEntries;
    }

    @Override
    public List<UserBridge> getPlayers() {
        return null;
    }

    public List<TeamBridge> getTeams() {
        return teams;
    }

    @Override
    public void setBracketEntries(List<BracketEntry> entries) {
        bracketEntries = (List<DoublesBracketEntry>) (List<?>) entries;
    }

    @Override
    public DoublesBracketEntry createBracketEntry() {
        return new DoublesBracketEntry();
    }
}
