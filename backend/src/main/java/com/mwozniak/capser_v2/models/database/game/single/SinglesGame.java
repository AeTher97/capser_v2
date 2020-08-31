package com.mwozniak.capser_v2.models.database.game.single;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Builder
@Getter
public class SinglesGame extends AbstractSinglesGame {


    @Override
    public GameType getGameType() {
        return GameType.SINGLES;
    }


    @Override
    public void updateUserPoints(User user, float pointsChange) {
        UserStats singlesStats = findCorrectStats(user);
        singlesStats.setPoints(singlesStats.getPoints() + pointsChange);
    }

    @Override
    public void calculateBeers(GamePlayerStats gamePlayerStats1, GamePlayerStats gamePlayerStats2) {
        {
            if (gamePlayerStats1.getScore() > 8) {
                gamePlayerStats2.setBeersDowned(3);
            } else if (gamePlayerStats1.getScore() > 4) {
                gamePlayerStats2.setBeersDowned(2);
            } else {
                gamePlayerStats2.setBeersDowned(1);
            }
        }
    }

    @Override
    public UserStats findCorrectStats(User user) {
        return user.getUserSinglesStats();
    }
}
;