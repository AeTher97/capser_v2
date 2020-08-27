package com.mwozniak.capser_v2.models.database.game;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import com.mwozniak.capser_v2.models.exception.UpdateStatsException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractSinglesGame extends AbstractGame {


    @Setter
    @Getter
    private GameMode gameMode;

    @Setter
    @Getter
    private UUID player1;
    @Setter
    @Getter
    private UUID player2;

    @Setter
    @Getter
    private UUID winner;

    public GamePlayerStats getPlayer1Stats() throws GameValidationException {
        return filterStats(getPlayer1());
    }

    public GamePlayerStats getPlayer2Stats() throws GameValidationException {
        return filterStats(getPlayer2());
    }


    @Override
    public void validateGame() throws GameValidationException {
        GamePlayerStats player1Stats = getPlayer1Stats();
        GamePlayerStats player2Stats = getPlayer2Stats();

        if (player1Stats.getScore() == player2Stats.getScore()) {
            throw new GameValidationException("Game cannot end in a draw");
        }

        if (player1Stats.getScore() < 11 && player2Stats.getScore() < 11) {
            throw new GameValidationException("Game must end with one of the players obtaining 11 points");
        }


        if (getGameMode().equals(GameMode.OVERTIME) && Math.abs(player1Stats.getScore() - player2Stats.getScore()) != 2) {
            throw new GameValidationException("Overtime game must finish with 2 points advantage");
        } else if (getGameMode().equals(GameMode.SUDDEN_DEATH) && (player1Stats.getScore() != 11 && player2Stats.getScore() != 11)) {
            throw new GameValidationException("Sudden death game must finish with 11 points");
        }

        if (getPlayer1().equals(getPlayer2())) {
            throw new GameValidationException("Game has to be played by two players");
        }
    }


    @Override
    public void calculateGameStats() throws GameValidationException {
        GamePlayerStats gamePlayerStats1 = getPlayer1Stats();
        GamePlayerStats gamePlayerStats2 = getPlayer2Stats();

        if (gamePlayerStats1.getScore() > gamePlayerStats2.getScore()) {
            setWinner(getPlayer1());
        } else {
            setWinner(getPlayer2());
        }

        if (gamePlayerStats1.getScore() == 0) {
            setNakedLap(true);
            gamePlayerStats1.setNakedLap(true);
        }

        if (gamePlayerStats2.getScore() == 0) {
            setNakedLap(true);
            gamePlayerStats2.setNakedLap(true);
        }

        if (gamePlayerStats1.getScore() != 0 && gamePlayerStats1.getSinks() != 0) {
            gamePlayerStats2.setRebuttals(gamePlayerStats1.getSinks() - gamePlayerStats1.getScore());
        }

        if (gamePlayerStats2.getScore() != 0 && gamePlayerStats2.getSinks() != 0) {
            gamePlayerStats1.setRebuttals(gamePlayerStats2.getSinks() - gamePlayerStats2.getScore());
        }

        calculateBeers(gamePlayerStats1, gamePlayerStats2);
        calculateBeers(gamePlayerStats2, gamePlayerStats1);

    }

    @Override
    public void calculatePlayerStats(User user ) throws UpdateStatsException {
        try {

            UserStats singlesStats = findCorrectStats(user);
            GamePlayerStats gamePlayerStats = filterStats(user.getId());

            GamePlayerStats opponentStats = null;
            if (user.getId().equals(player1)) {
                opponentStats = getPlayer2Stats();
            } else if (user.getId().equals(player2)) {
                opponentStats = getPlayer1Stats();
            } else {
                throw new UpdateStatsException("Cannot find opponent stats");
            }

            singlesStats.setGamesPlayed(singlesStats.getGamesPlayed() + 1);
            if (winner.equals(user.getId())) {
                singlesStats.setGamesWon(singlesStats.getGamesWon() + 1);
            } else {
                singlesStats.setGamesLost(singlesStats.getGamesLost() + 1);
            }

            singlesStats.setBeersDowned(singlesStats.getBeersDowned() + gamePlayerStats.getBeersDowned());

            singlesStats.setTotalPointsMade(singlesStats.getTotalPointsMade() + gamePlayerStats.getScore());
            singlesStats.setTotalPointsLost(singlesStats.getTotalPointsLost() + opponentStats.getScore());

            singlesStats.setTotalSinksMade(singlesStats.getTotalSinksMade() + gamePlayerStats.getSinks());
            singlesStats.setTotalSinksLost(singlesStats.getTotalSinksLost() + opponentStats.getSinks());

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

    @Override
    public void updateUserPoints(User user, float pointsChange){
        UserStats singlesStats = user.getUserSinglesStats();
        singlesStats.setPoints(singlesStats.getPoints() + pointsChange);
    }

    public void calculateBeers(GamePlayerStats gamePlayerStats1, GamePlayerStats gamePlayerStats2) {
        if (gamePlayerStats1.getScore() > 8) {
            gamePlayerStats2.setBeersDowned(3);
        } else if (gamePlayerStats1.getScore() > 4) {
            gamePlayerStats2.setBeersDowned(2);
        } else {
            gamePlayerStats2.setBeersDowned(1);
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


    public static void fillCommonProperties(AbstractSinglesGame singlesGame, SinglesGameDto singlesGameDto) {
        singlesGame.setGameEventList(singlesGameDto.getGameEventList());
        singlesGame.setGamePlayerStats(Arrays.asList(
                GamePlayerStats.builder()
                        .score(singlesGameDto.getPlayer1Stats().getScore())
                        .sinks(singlesGameDto.getPlayer1Stats().getSinks())
                        .playerId(singlesGameDto.getPlayer1Stats().getPlayerId())
                        .build(),
                GamePlayerStats.builder()
                        .score(singlesGameDto.getPlayer2Stats().getScore())
                        .sinks(singlesGameDto.getPlayer2Stats().getSinks())
                        .playerId(singlesGameDto.getPlayer2Stats().getPlayerId())
                        .build()));
        singlesGame.setGameMode(singlesGameDto.getGameMode());
        singlesGame.setPlayer1(singlesGameDto.getPlayer1Stats().getPlayerId());
        singlesGame.setPlayer2(singlesGameDto.getPlayer2Stats().getPlayerId());
    }

    protected abstract UserStats findCorrectStats(User user);

}
