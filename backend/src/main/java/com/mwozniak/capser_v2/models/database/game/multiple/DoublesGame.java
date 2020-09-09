package com.mwozniak.capser_v2.models.database.game.multiple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Builder
@Getter
public class DoublesGame extends AbstractMultipleGame {

    public DoublesGame(){

    }

    @Override
    public GameType getGameType() {
        return GameType.DOUBLES;
    }

    @Override
    public void continueValidation() throws GameValidationException {

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
    }

    @Override
    @JsonIgnore
    public UserStats findCorrectStats(User user) {
        return user.getUserDoublesStats();
    }


}
