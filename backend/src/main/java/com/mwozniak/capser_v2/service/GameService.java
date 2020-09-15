package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GameService {

    AbstractGame findGame(UUID id)  throws CapserException;

    List<AbstractGame> listGames();

    Page<AbstractGame> listGames(Pageable pageable);

    Page<AbstractGame> listAcceptedGames(Pageable pageable);

    void queueGame(AbstractGame abstractGame) throws CapserException;

    void acceptGame(UUID gameId)  throws CapserException;

    void rejectGame(UUID gameId)  throws CapserException;

    AcceptanceRequestType getAcceptanceRequestType();

    GameType getGameType();
}
