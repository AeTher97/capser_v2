package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.service.game.UnrankedGameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unranked")
public class UnrankedGameController extends AbstractSolosController {

    public UnrankedGameController(UnrankedGameService unrankedGameService) {
        super(unrankedGameService);
    }

    @Override
    protected AbstractGame createGameObject() {
        return new UnrankedGame();
    }
}
