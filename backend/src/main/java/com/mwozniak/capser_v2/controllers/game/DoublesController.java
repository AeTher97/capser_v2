package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import com.mwozniak.capser_v2.service.game.DoublesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doubles")
public class DoublesController extends AbstractMultipleGameController {

    public DoublesController(DoublesService doublesService) {
        super(doublesService);
    }

    @Override
    protected AbstractGame createGameObject() {
        return new DoublesGame();
    }
}
