package com.mwozniak.capser_v2.models.database.game.single;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Arrays;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractSinglesGame extends AbstractGame {

    @Setter
    @Getter
    private UUID player1;
    @Setter
    @Getter
    private UUID player2;

    @Setter
    @Getter
    private UUID winner;

    @JsonIgnore
    public GamePlayerStats getPlayer1Stats() throws GameValidationException {
        return filterStats(getPlayer1());
    }

    @JsonIgnore
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

        if(player1Stats.getScore() > player1Stats.getSinks() || player2Stats.getScore() > player2Stats.getSinks()){
            throw new GameValidationException("Player cannot score more than they sunk");
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
            throw new GameValidationException("Game has to be played by two unique players");
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


    protected GamePlayerStats getOpponentStats(User user) {
        try {
            if (user.getId().equals(player1)) {
                return getPlayer2Stats();
            } else {
                return getPlayer1Stats();
            }
        } catch (GameValidationException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void fillCommonProperties(AbstractGameDto abstractGameDto) {
        SinglesGameDto singlesGameDto = (SinglesGameDto) abstractGameDto;
        setGameEventList(singlesGameDto.getGameEventList());
        setGamePlayerStats(Arrays.asList(
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
        setGameMode(singlesGameDto.getGameMode());
        setPlayer1(singlesGameDto.getPlayer1Stats().getPlayerId());
        setPlayer2(singlesGameDto.getPlayer2Stats().getPlayerId());
    }


    protected boolean isWinner(User user) {
        return winner.equals(user.getId());
    }

    public abstract void calculateBeers(GamePlayerStats gamePlayerStats1, GamePlayerStats gamePlayerStats2);

    @Override
    public int getSinksLost(User user) {
        return getOpponentStats(user).getSinks();
    }

    @Override
    public int getPointsLost(User user) {
        return getOpponentStats(user).getScore();
    }
}
