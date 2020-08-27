package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.SinglesGameService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Pageable;
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
        singlesGame.calculateGameStats();
        singlesGameService.queueGame(singlesGame);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{gameId}")
    public ResponseEntity<Object> acceptGame(@PathVariable UUID gameId) throws CapserException {
        singlesGameService.acceptGame(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getGames(@RequestParam int pageSize, @RequestParam int pageNumber){
        return ResponseEntity.ok().body(singlesGameService.listGames(PageRequest.of(pageNumber,pageSize)));
    }

}
