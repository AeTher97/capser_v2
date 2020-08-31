package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.service.UnrankedGameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unranked")
public class UnrankedGameController extends AbstractSinglesController {

    public UnrankedGameController(UnrankedGameService unrankedGameService) {
        super(unrankedGameService);
    }

    @Override
    protected AbstractGame createGameObject() {
        return new UnrankedGame();
    }
}
