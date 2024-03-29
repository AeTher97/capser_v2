package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/singles")
public class SinglesController extends AbstractSolosController {

    public SinglesController(SinglesGameService singlesGameService) {
        super(singlesGameService);
    }

    @Override
    protected AbstractGame createGameObject() {
        return new SoloGame();
    }
}
