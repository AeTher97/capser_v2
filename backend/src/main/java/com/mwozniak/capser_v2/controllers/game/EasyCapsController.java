package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/easy")
public class EasyCapsController extends AbstractSolosController {

    public EasyCapsController(EasyCapsGameService easyCapsGameService) {
        super(easyCapsGameService);
    }

    @Override
    protected AbstractGame createGameObject() {
        return new EasyCapsGame();
    }
}
