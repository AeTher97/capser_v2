package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.AbstractGameService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

public abstract class AbstractGameController implements GameController {

    protected final AbstractGameService abstractGameService;

    public AbstractGameController(AbstractGameService abstractGameService) {
        this.abstractGameService = abstractGameService;
    }


    @Override
    @PostMapping("/accept/{gameId}")
    public ResponseEntity<Object> acceptGame(@PathVariable UUID gameId) throws CapserException {
        abstractGameService.acceptGame(gameId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Object> doAddGame(AbstractGame abstractGame) throws CapserException {
        abstractGame.validateGame();
        abstractGame.calculateGameStats();
        abstractGameService.queueGame(abstractGame);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getGames(@RequestParam int pageSize, @RequestParam int pageNumber) {
        return ResponseEntity.ok().body(abstractGameService.listGames(PageRequest.of(pageNumber, pageSize)));
    }


    protected abstract AbstractGame createGameObject();

}
