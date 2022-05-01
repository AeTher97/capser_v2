package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.controllers.GameController;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.game.AbstractGameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.UUID;

@Log4j2
public abstract class AbstractGameController implements GameController {

    protected final AbstractGameService abstractGameService;

    protected AbstractGameController(AbstractGameService abstractGameService) {
        this.abstractGameService = abstractGameService;
    }

    @Override
    @PreAuthorize("@accessVerificationBean.canAcceptGame(#gameId)")
    @PostMapping("/accept/{gameId}")
    public ResponseEntity<Object> acceptGame(@PathVariable @Valid UUID gameId) throws CapserException {
        log.info("Accepting game " + gameId.toString());
        abstractGameService.acceptGame(gameId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("@accessVerificationBean.canAcceptGame(#gameId)")
    @PostMapping("/reject/{gameId}")
    public ResponseEntity<Object> rejectGame(@PathVariable @Valid UUID gameId) throws CapserException {
        log.info("Rejecting game " + gameId.toString());
        abstractGameService.rejectGame(gameId);
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<Object> doAddGame( AbstractGame abstractGame) throws CapserException {
        log.info("Posting game");
        abstractGame.validateGame();
        abstractGame.calculateGameStats();
        abstractGameService.queueGame(abstractGame);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getGames(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam(required = false) UUID player) {
        log.info("Getting games list");
        return ResponseEntity.ok().body(abstractGameService.listAcceptedGames(PageRequest.of(pageNumber, pageSize, Sort.by("time").descending()), player));
    }

    @Override
    @GetMapping("/{gameId}")
    public ResponseEntity<Object> getGame(@PathVariable UUID gameId) throws CapserException {
        log.info("Getting game info " + gameId.toString());
        return ResponseEntity.ok().body(abstractGameService.findGame(gameId));
    }


    protected abstract AbstractGame createGameObject();

}
