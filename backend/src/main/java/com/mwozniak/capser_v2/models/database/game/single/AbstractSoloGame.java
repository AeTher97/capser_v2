package com.mwozniak.capser_v2.models.database.game.single;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.dto.SoloGameDto;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractSoloGame extends AbstractGame {

    @Setter
    @Getter
    private UUID player1;
    @Setter
    @Getter
    private UUID player2;

    @Setter
    private UUID winner;

    protected AbstractSoloGame() {
        super();
    }

    @JsonIgnore
    public GamePlayerStats getPlayer1Stats() {
        try {
            return findStats(getPlayer1());
        } catch (GameValidationException e) {
            // should never happen for this case
            return null;
        }
    }

    @JsonIgnore
    public GamePlayerStats getPlayer2Stats() {
        try {
            return findStats(getPlayer2());
        } catch (GameValidationException e) {
            // should never happen in this case
            return null;
        }
    }


    @Override
    public void validate() throws GameValidationException {
        GamePlayerStats player1Stats = getPlayer1Stats();
        GamePlayerStats player2Stats = getPlayer2Stats();

        if (player1Stats.getScore() == player2Stats.getScore()) {
            throw new GameValidationException("Game cannot end in a draw");
        }

        if (player1Stats.getSinks() != 0 || player2Stats.getSinks() != 0) {
            if (player1Stats.getScore() > player1Stats.getSinks() || player2Stats.getScore() > player2Stats.getSinks()) {
                throw new GameValidationException("Player cannot score more than they sunk");
            }
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
    public void calculateStatsOfAllPlayers() throws GameValidationException {
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
        if (user.getId().equals(player1)) {
            return getPlayer2Stats();
        } else {
            return getPlayer1Stats();
        }
    }


    @Override
    public void fillCommonProperties(AbstractGameDto abstractGameDto) {
        SoloGameDto soloGameDto = (SoloGameDto) abstractGameDto;
        setGameEventList(abstractGameDto.getGameEvents());
        setGamePlayerStats(new ArrayList<>(Arrays.asList(
                GamePlayerStats.builder()
                        .score(soloGameDto.getPlayer1Stats().getScore())
                        .sinks(soloGameDto.getPlayer1Stats().getSinks())
                        .playerId(soloGameDto.getPlayer1Stats().getPlayerId())
                        .build(),
                GamePlayerStats.builder()
                        .score(soloGameDto.getPlayer2Stats().getScore())
                        .sinks(soloGameDto.getPlayer2Stats().getSinks())
                        .playerId(soloGameDto.getPlayer2Stats().getPlayerId())
                        .build())));
        setGameMode(soloGameDto.getGameMode());
        setPlayer1(soloGameDto.getPlayer1Stats().getPlayerId());
        setPlayer2(soloGameDto.getPlayer2Stats().getPlayerId());
    }

    @Override
    public List<UUID> getAllPlayers() {
        return Arrays.asList(player1, player2);
    }

    public boolean isWinner(User user) {
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

    @Override
    public UUID getWinner() {
        return winner;
    }
}
