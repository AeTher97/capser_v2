package com.mwozniak.capser_v2.utils;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.dto.PlayerStatsDto;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Game {

    public static void validateSinglesGame(SinglesGameDto singlesGameDto) throws CapserException {
        PlayerStatsDto player1Stats = singlesGameDto.getPlayer1Stats();
        PlayerStatsDto player2Stats = singlesGameDto.getPlayer2Stats();

        if (player1Stats.getScore() == player1Stats.getScore()) {
            throw new GameValidationException("Game cannot end in a draw");
        }

        if (player1Stats.getScore() < 11 && player2Stats.getScore() < 11) {
            throw new GameValidationException("Game must end with one of the players obtaining 11 points");
        }


        if (singlesGameDto.getGameMode().equals(GameMode.OVERTIME) && Math.abs(player1Stats.getScore() - player2Stats.getScore()) != 2) {
            throw new GameValidationException( "Overtime game must finish with 2 points advantage");
        } else if (singlesGameDto.getGameMode().equals(GameMode.SUDDEN_DEATH) && (player1Stats.getScore() != 11 && player2Stats.getScore() != 11)) {
            throw new GameValidationException("Sudden death game must finish with 11 points");
        }

        if (singlesGameDto.getPlayer1().equals(singlesGameDto.getPlayer2())) {
            throw new GameValidationException( "Game has to be played by two players");
        }
    }
}
