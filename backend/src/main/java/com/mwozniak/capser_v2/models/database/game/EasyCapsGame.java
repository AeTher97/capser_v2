package com.mwozniak.capser_v2.models.database.game;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EasyCapsGame extends AbstractSinglesGame {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Override
    public GameType getGameType() {
        return GameType.EASY_CAPS;
    }

    @Override
    public void calculateBeers(GamePlayerStats gamePlayerStats1, GamePlayerStats gamePlayerStats2) {
        gamePlayerStats2.setBeersDowned(1.5f);
    }

    @Override
    protected UserStats findCorrectStats(User user) {
        return user.getUserEasyStats();
    }

    @Override
    public void updateUserPoints(User user, float pointsChange) {
        // do nothing
    }
}
