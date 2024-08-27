package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.service.AggregateGameService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class AggregateGameController {

    private final AggregateGameService aggregateGameService;

    public AggregateGameController(AggregateGameService aggregateGameService) {
        this.aggregateGameService = aggregateGameService;
    }

    @GetMapping("/user/{userId}")
    public Page<AbstractGame> getUserGames(@PathVariable @Valid UUID userId, @RequestParam(required = false) int pageNumber) {
        return aggregateGameService.getUserGamesWithTypeAndOpponent(userId, pageNumber);
    }

    @GetMapping("/user/{userId}/{gameType}")
    public Page<? extends AbstractGame> getUserGamesWithOpponent(@PathVariable @Valid UUID userId,
                                                                 @PathVariable @Valid GameType gameType,
                                                                 @RequestParam(required = false) int pageNumber,
                                                                 @RequestParam(required = false) UUID opponentId) {
        if (opponentId == null) {
            return aggregateGameService.getUserGamesWithType(userId, gameType, pageNumber);
        } else {
            return aggregateGameService.getUserGamesWithTypeAndOpponent(userId, opponentId, gameType, pageNumber);
        }

    }
}
