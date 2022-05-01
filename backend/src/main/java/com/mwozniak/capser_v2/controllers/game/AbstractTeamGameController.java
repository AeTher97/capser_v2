package com.mwozniak.capser_v2.controllers.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.dto.TeamGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.service.game.AbstractGameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Log4j2
public abstract class AbstractTeamGameController extends AbstractGameController {

    public AbstractTeamGameController(AbstractGameService abstractGameService) {
        super(abstractGameService);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.isPresentInGameTeamGame(#teamGameDto)")
    public ResponseEntity<Object> addGame(@Valid @RequestBody TeamGameDto teamGameDto) throws CapserException {
        AbstractGame abstractGame = createGameObject();
        abstractGame.fillCommonProperties(teamGameDto);
        return doAddGame(abstractGame);
    }


}
