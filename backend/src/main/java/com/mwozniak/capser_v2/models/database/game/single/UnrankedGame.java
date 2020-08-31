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
public class UnrankedGame  extends AbstractSinglesGame {


    @Override
    public void calculateBeers(GamePlayerStats gamePlayerStats1, GamePlayerStats gamePlayerStats2) {
        if(gamePlayerStats1.getScore() > 0) {
            gamePlayerStats2.setBeersDowned(1.5f);
        } else {
            gamePlayerStats2.setBeersDowned(0);
        }
    }

    @Override
    public UserStats findCorrectStats(User user) {
        return user.getUserUnrankedStats();
    }

    @Override
    public GameType getGameType() {
        return GameType.UNRANKED;
    }

    @Override
    public void updateUserPoints(User user, float pointsChange) {
        // no points bruh
    }
}
