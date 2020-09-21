package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.UUID;

public interface GameController {

    ResponseEntity<Object> doAddGame(AbstractGame abstractGame) throws CapserException;

    ResponseEntity<Object> acceptGame(@PathVariable @Valid UUID gameId) throws CapserException;

    ResponseEntity<Object> rejectGame(@PathVariable @Valid UUID gameId) throws CapserException;

    ResponseEntity<Object> getGames(@RequestParam int pageSize, @RequestParam int pageNumber) throws CapserException;

}
