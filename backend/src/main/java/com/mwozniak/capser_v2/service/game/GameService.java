package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GameService {

    AbstractGame findGame(UUID id) throws CapserException;

    List<AbstractGame> listGames();

    Page<AbstractGame> listGames(Pageable pageable);

    Page<AbstractGame> listAcceptedGames(Pageable pageable);

    Page<AbstractGame> listAcceptedGames(Pageable pageable, UUID player);

    void queueGame(AbstractGame abstractGame) throws CapserException;

    UUID queueGame(AbstractGame abstractGame, boolean notify) throws CapserException;

    AbstractGame acceptGame(UUID gameId, boolean notify) throws CapserException;

    void acceptGame(UUID gameId) throws CapserException;

    int acceptOverdueGames();

    void rejectGame(UUID gameId) throws CapserException;

    AcceptanceRequestType getAcceptanceRequestType();

    GameType getGameType();
}
