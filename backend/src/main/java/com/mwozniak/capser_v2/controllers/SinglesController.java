package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.service.SinglesGameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/singles")
public class SinglesController extends AbstractSinglesController {
        public SinglesController(SinglesGameService singlesGameService) {
        super(singlesGameService);
    }


    @Override
    protected AbstractGame createGameObject() {
        return new SinglesGame();
    }
}
