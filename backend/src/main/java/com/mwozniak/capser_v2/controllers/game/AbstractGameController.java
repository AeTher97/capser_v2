package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.controllers.GameController;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.AbstractGameService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

public abstract class AbstractGameController implements GameController {

    protected final AbstractGameService abstractGameService;

    public AbstractGameController(AbstractGameService abstractGameService) {
        this.abstractGameService = abstractGameService;
    }


    @Override
    @PreAuthorize("@accessVerificationBean.canAcceptGame(#gameId)")
    @PostMapping("/accept/{gameId}")
    public ResponseEntity<Object> acceptGame(@PathVariable @Valid UUID gameId) throws CapserException {
        abstractGameService.acceptGame(gameId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("@accessVerificationBean.canAcceptGame(#gameId)")
    @PostMapping("/reject/{gameId}")
    public ResponseEntity<Object> rejectGame(@PathVariable @Valid UUID gameId) throws CapserException {
        abstractGameService.rejectGame(gameId);
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<Object> doAddGame( AbstractGame abstractGame) throws CapserException {
        abstractGame.validateGame();
        abstractGame.calculateGameStats();
        abstractGameService.queueGame(abstractGame);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getGames(@RequestParam int pageSize, @RequestParam int pageNumber) {
        return ResponseEntity.ok().body(abstractGameService.listAcceptedGames(PageRequest.of(pageNumber, pageSize, Sort.by("time").descending())));
    }


    protected abstract AbstractGame createGameObject();

}
