package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GamemodeInvalidException;
import com.mwozniak.capser_v2.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameById {

    private final List<GameService> gameServiceList;

    public GameById(DoublesService doublesService, EasyCapsGameService easyCapsGameService, SinglesGameService singlesGameService, UnrankedGameService unrankedGameService){
        gameServiceList = new ArrayList<>();
        gameServiceList.add(doublesService);
        gameServiceList.add(easyCapsGameService);
        gameServiceList.add(singlesGameService);
        gameServiceList.add(unrankedGameService);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Object> getGame(@PathVariable UUID gameId, @RequestParam GameType gameType) throws CapserException {
        Optional<GameService> gameServiceOptional = gameServiceList.stream().filter(gameService -> gameService.getGameType().equals(gameType)).findAny();
        if(gameServiceOptional.isPresent()){
            return ResponseEntity.ok(gameServiceOptional.get().findGame(gameId));
        } else {
            throw new GamemodeInvalidException("No such gamemode");
        }
    }
}
