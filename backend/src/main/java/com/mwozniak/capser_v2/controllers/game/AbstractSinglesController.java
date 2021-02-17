package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.game.AbstractGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public abstract class AbstractSinglesController extends AbstractGameController {

    public AbstractSinglesController(AbstractGameService abstractGameService) {
        super(abstractGameService);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.isPresentInSinglesGame(#singlesGameDto)")
    public ResponseEntity<Object> addGame(@Valid @RequestBody SinglesGameDto singlesGameDto) throws CapserException {
        AbstractGame abstractGame = createGameObject();
        abstractGame.fillCommonProperties(singlesGameDto);
        return doAddGame(abstractGame);
    }
}
