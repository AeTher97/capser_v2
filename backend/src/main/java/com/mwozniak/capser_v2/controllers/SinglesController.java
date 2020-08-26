package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.SinglesGameService;
import com.mwozniak.capser_v2.utils.GameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/singles")
public class SinglesController {

    private final SinglesGameService singlesGameService;

    public SinglesController(SinglesGameService singlesGameService) {
        this.singlesGameService = singlesGameService;
    }

    @PostMapping
    public ResponseEntity<Object> addSinglesGame(@RequestBody @Valid SinglesGameDto singlesGameDto) throws CapserException {
        SinglesGame singlesGame = new SinglesGame();
        SinglesGame.fillCommonProperties(singlesGame,singlesGameDto);
        singlesGame.validateGame();
        singlesGame.calculateStats();
        singlesGameService.queueGame(singlesGame);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{gameId}")
    public ResponseEntity<Object> acceptGame(@PathVariable UUID gameId) throws CapserException {
        singlesGameService.acceptGame(gameId);
        return ResponseEntity.ok().build();
    }

}
