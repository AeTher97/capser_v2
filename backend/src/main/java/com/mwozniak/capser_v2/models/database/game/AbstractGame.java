package com.mwozniak.capser_v2.models.database.game;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import com.mwozniak.capser_v2.models.exception.UpdateStatsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@AllArgsConstructor
public abstract class AbstractGame {

    public AbstractGame(){
        time = new Date();
        accepted = false;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    private UUID id;

    @Setter
    @Getter
    private boolean accepted;

    @Setter
    @Getter
    private boolean nakedLap;


    @Setter
    @Getter
    private GameMode gameMode;

    @Getter
    private final Date time;

    @Setter
    @Getter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected List<GamePlayerStats> gamePlayerStats;


    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEventEntity> gameEventList;

    public abstract GameType getGameType();

    public abstract void calculateGameStats() throws GameValidationException;

    public abstract void validateGame() throws GameValidationException;

    public void calculatePlayerStats(User user) throws UpdateStatsException {
        try {

            UserStats singlesStats = findCorrectStats(user);
            GamePlayerStats gamePlayerStats = filterStats(user.getId());

            singlesStats.setGamesPlayed(singlesStats.getGamesPlayed() + 1);
            if (isWinner(user)) {
                singlesStats.setGamesWon(singlesStats.getGamesWon() + 1);
            } else {
                singlesStats.setGamesLost(singlesStats.getGamesLost() + 1);
            }

            singlesStats.setBeersDowned(singlesStats.getBeersDowned() + gamePlayerStats.getBeersDowned());

            singlesStats.setTotalPointsMade(singlesStats.getTotalPointsMade() + gamePlayerStats.getScore());
            singlesStats.setTotalPointsLost(singlesStats.getTotalPointsLost() + getPointsLost(user));

            singlesStats.setTotalSinksMade(singlesStats.getTotalSinksMade() + gamePlayerStats.getSinks());
            singlesStats.setTotalSinksLost(singlesStats.getTotalSinksLost() + getSinksLost(user));

            if(gamePlayerStats.isNakedLap()){
                singlesStats.setNakedLaps(singlesStats.getNakedLaps() + 1);
            }


            singlesStats.setTotalRebuttals(singlesStats.getTotalRebuttals() + gamePlayerStats.getRebuttals());


            singlesStats.setAvgRebuttals((float) singlesStats.getTotalRebuttals()/singlesStats.getGamesPlayed());
            singlesStats.setWinLossRatio((float) singlesStats.getGamesWon()/singlesStats.getGamesPlayed());
            singlesStats.setSinksMadeLostRatio((float) singlesStats.getTotalPointsMade()/singlesStats.getTotalSinksLost());
            singlesStats.setPointsMadeLostRatio((float) singlesStats.getTotalPointsMade()/singlesStats.getTotalPointsLost());


        } catch (GameValidationException e) {
            throw new UpdateStatsException("Invalid user id while updating stats", e);
        }
    }

    protected GamePlayerStats filterStats(UUID id) throws GameValidationException {
        Optional<GamePlayerStats> gamePlayerStatsOptional = gamePlayerStats.stream().filter(stats -> stats.getPlayerId().equals(id)).findFirst();
        if (gamePlayerStatsOptional.isPresent()) {
            return gamePlayerStatsOptional.get();
        } else {
            throw new GameValidationException("Cannot find player stats");
        }
    }


    public abstract void updateUserPoints(User user, float pointsChange);

    public abstract void fillCommonProperties(AbstractGameDto abstractGameDto);

    public abstract UserStats findCorrectStats(User user);

    public abstract int getSinksLost(User user);

    public abstract int getPointsLost(User user);

    protected abstract boolean isWinner(User user);

}
