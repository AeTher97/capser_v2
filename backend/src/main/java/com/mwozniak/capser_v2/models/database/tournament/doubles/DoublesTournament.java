package com.mwozniak.capser_v2.models.database.tournament.doubles;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import com.mwozniak.capser_v2.models.database.tournament.AbstractTournament;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.TeamBridge;
import com.mwozniak.capser_v2.models.database.tournament.UserBridge;
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
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class DoublesTournament extends AbstractTournament<DoublesGame> {

    public DoublesTournament() {

    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<DoublesBracketEntry> bracketEntries;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    protected List<TeamBridge> teams;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties(value = {"userSinglesStats", "userEasyStats", "userUnrankedStats", "userDoublesStats", "teams", "lastSeen", "lastGame", "role"})
    private TeamBridge winner;

    @Override
    protected void createCompetitorsArray() {
        teams = new ArrayList<>();
    }

    @Override
    protected List<Competitor> getCompetitorList() {
        return teams.stream().map(TeamBridge::getTeam).collect(Collectors.toList());
    }

    @Override
    protected void checkWinCondition() {
        DoublesBracketEntry entry = (DoublesBracketEntry) getBracketEntry(0);
        if (entry.getCoordinate() == 0) {
            if (entry.getGame() != null) {
                setFinished(true);
                setWinner(entry.getTeam1().getId().equals(entry.getGame().getWinner()) ? new TeamBridge(entry.getTeam1()) : new TeamBridge(entry.getTeam2()));
            } else if (entry.isForfeited()) {
                setFinished(true);
                setWinner(entry.getTeam2().getId().equals(entry.getForfeitedId()) ? new TeamBridge(entry.getTeam2()) : new TeamBridge(entry.getTeam1()));
            }
        }
    }


    @Override
    public GameType getGameType() {
        return GameType.DOUBLES;
    }

    @Override
    public List<? extends BracketEntry> getBracketEntries() {
        return bracketEntries;
    }

    @Override
    public List<UserBridge> getPlayers() {
        return null;
    }

    public List<TeamBridge> getTeams() {
        return teams;
    }

    @Override
    protected void setBracketEntries(List<BracketEntry> entries) {
        bracketEntries = (List<DoublesBracketEntry>) (List<?>) entries;
    }

    @Override
    protected DoublesBracketEntry createBracketEntry() {
        return new DoublesBracketEntry();
    }
}
