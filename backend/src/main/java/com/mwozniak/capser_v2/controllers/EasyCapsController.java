package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.game.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.EasyCapsGameService;
import com.mwozniak.capser_v2.service.SinglesGameService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/easy")
public class EasyCapsController {

    private final EasyCapsGameService easyCapsGameService;

    public EasyCapsController(EasyCapsGameService easyCapsGameService) {
        this.easyCapsGameService = easyCapsGameService;
    }

    @PostMapping
    public ResponseEntity<Object> addSinglesGame(@RequestBody @Valid SinglesGameDto singlesGameDto) throws CapserException {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        EasyCapsGame.fillCommonProperties(easyCapsGame,singlesGameDto);
        easyCapsGame.validateGame();
        easyCapsGame.calculateGameStats();
        easyCapsGameService.queueGame(easyCapsGame);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{gameId}")
    public ResponseEntity<Object> acceptGame(@PathVariable UUID gameId) throws CapserException {
        easyCapsGameService.acceptGame(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getGames(@RequestParam int pageSize, @RequestParam int pageNumber){
        return ResponseEntity.ok().body(easyCapsGameService.listGames(PageRequest.of(pageNumber,pageSize)));
    }


}
