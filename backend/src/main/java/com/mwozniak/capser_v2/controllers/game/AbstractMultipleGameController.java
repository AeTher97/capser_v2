package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.dto.MultipleGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.AbstractGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public abstract class AbstractMultipleGameController extends AbstractGameController {

    public AbstractMultipleGameController(AbstractGameService abstractGameService) {
        super(abstractGameService);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.isPresentInGameTeamGame(#multipleGameDto)")
    public ResponseEntity<Object> addGame(@Valid @RequestBody MultipleGameDto multipleGameDto) throws CapserException {
        AbstractGame abstractGame = createGameObject();
        abstractGame.fillCommonProperties(multipleGameDto);
        return doAddGame(abstractGame);
    }


}
