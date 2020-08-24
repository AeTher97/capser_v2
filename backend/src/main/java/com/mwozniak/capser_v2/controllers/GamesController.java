package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.utils.Game;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


@RestController
@RequestMapping("/games")
public class GamesController {

    @PostMapping
    public ResponseEntity<Object> addGame(@RequestBody @Valid SinglesGameDto singlesGameDto) throws CapserException {
        Game.validateSinglesGame(singlesGameDto);

    }
}
