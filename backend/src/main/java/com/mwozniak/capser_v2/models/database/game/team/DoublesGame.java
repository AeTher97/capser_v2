package com.mwozniak.capser_v2.models.database.game.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
public class DoublesGame extends AbstractTeamGame {

    public DoublesGame() {

    }

    @Override
    public GameType getGameType() {
        return GameType.DOUBLES;
    }

    @Override
    public void playerNumberSpecificValidation() throws GameValidationException {

        if (getTeam1().getPlayerList().size() != 2 || getTeam2().getPlayerList().size() != 2) {
            throw new GameValidationException("Doubles game has to have 4 players 2 in each team");
        }

        if (gamePlayerStats.size() != 4) {
            throw new GameValidationException("Game needs to have 4 player stats objects");
        }


        if (gamePlayerStats.stream().map(GamePlayerStats::getPlayerId).distinct().count() != 4) {
            throw new GameValidationException("Game has to be played by 4 distinct players");
        }

    }


    @Override
    public void updateUserPoints(User user, float pointsChange) {
        UserStats singlesStats = findCorrectStats(user);
        singlesStats.setPoints(singlesStats.getPoints() + pointsChange);
        getPlayerStats(user.getId()).setPointsChange(pointsChange);
    }

    @Override
    @JsonIgnore
    public UserStats findCorrectStats(User user) {
        return user.getUserDoublesStats();
    }

    @Override
    public List<UUID> getPlayers() {
        List<UUID> players = new ArrayList<>();
        players.addAll(getTeam1().getPlayerList());
        players.addAll(getTeam2().getPlayerList());
        return players;
    }


}
