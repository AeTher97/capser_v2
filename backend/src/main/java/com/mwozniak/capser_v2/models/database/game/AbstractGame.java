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
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
@AllArgsConstructor
public abstract class AbstractGame {

    protected AbstractGame() {
        time = new Date();
        accepted = false;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    @Setter
    private UUID id;

    @Getter
    private boolean accepted;

    @Setter
    @Getter
    private boolean nakedLap;

    @Getter
    @Setter
    private String team1Name;

    @Getter
    @Setter
    private String team2Name;


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
    @Getter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEventEntity> gameEventList;

    public abstract GameType getGameType();

    public abstract void calculateGameStats() throws GameValidationException;

    public abstract void validateGame() throws GameValidationException;

    public void calculatePlayerStats(User user) throws UpdateStatsException {
        try {

            UserStats userStats = findCorrectStats(user);
            GamePlayerStats playerStats = findStats(user.getId());

            userStats.setGamesPlayed(userStats.getGamesPlayed() + 1);
            if (isWinner(user)) {
                userStats.setGamesWon(userStats.getGamesWon() + 1);
            } else {
                userStats.setGamesLost(userStats.getGamesLost() + 1);
            }

            if (playerStats.getSinks() > 0 || (playerStats.getScore() == 0 && playerStats.getSinks() == 0)) {
                userStats.setGamesLoggedSinks(userStats.getGamesLoggedSinks() + 1);
            }

            userStats.setBeersDowned(userStats.getBeersDowned() + playerStats.getBeersDowned());

            userStats.setTotalPointsMade(userStats.getTotalPointsMade() + playerStats.getScore());
            userStats.setTotalPointsLost(userStats.getTotalPointsLost() + getPointsLost(user));

            userStats.setTotalSinksMade(userStats.getTotalSinksMade() + playerStats.getSinks());
            userStats.setTotalSinksLost(userStats.getTotalSinksLost() + getSinksLost(user));

            if (playerStats.isNakedLap()) {
                userStats.setNakedLaps(userStats.getNakedLaps() + 1);
            }


            userStats.setTotalRebuttals(userStats.getTotalRebuttals() + playerStats.getRebuttals());


            userStats.setAvgRebuttals(userStats.getGamesLoggedSinks() == 0 ? userStats.getTotalRebuttals() :(float) userStats.getTotalRebuttals() / userStats.getGamesLoggedSinks());
            userStats.setWinLossRatio(userStats.getGamesLost() == 0 ? userStats.getGamesWon() : (float) userStats.getGamesWon() / userStats.getGamesLost());
            userStats.setSinksMadeLostRatio(userStats.getTotalSinksLost() == 0 ? userStats.getTotalSinksMade() :(float) userStats.getTotalSinksMade() / userStats.getTotalSinksLost());
            userStats.setPointsMadeLostRatio(userStats.getTotalPointsLost() == 0 ? userStats.getTotalPointsMade() :(float) userStats.getTotalPointsMade() / userStats.getTotalPointsLost());
            user.setLastGame(new Date());

        } catch (GameValidationException e) {
            throw new UpdateStatsException("Invalid user id while updating stats", e);
        }
    }

    protected GamePlayerStats findStats(UUID id) throws GameValidationException {
        Optional<GamePlayerStats> gamePlayerStatsOptional = gamePlayerStats.stream().filter(stats -> stats.getPlayerId().equals(id)).findFirst();
        if (gamePlayerStatsOptional.isPresent()) {
            return gamePlayerStatsOptional.get();
        } else {
            throw new GameValidationException("Cannot find player stats");
        }
    }


    public abstract void updateUserPoints(User user, float pointsChange) throws GameValidationException;

    public abstract void fillCommonProperties(AbstractGameDto abstractGameDto);

    public abstract UserStats findCorrectStats(User user);

    public abstract int getSinksLost(User user);

    public abstract int getPointsLost(User user);

    protected abstract boolean isWinner(User user);

    public abstract List<UUID> getPlayers();

    public static class Comparators {

        private Comparators() {

        }

        public static final Comparator<AbstractGame> DATE = Comparator.comparing(AbstractGame::getTime);
    }

    public abstract UUID getWinnerId();

    public void setAccepted() {
        accepted = true;
    }

}
