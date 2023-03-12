package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.Game;
import com.mwozniak.capser_v2.models.exception.CapserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GameService<T extends Game> {

    T saveGame(T game);

    void removeGame(T game);

    T findGame(UUID id) throws CapserException;

    List<T> listGames();

    Page<T> listGames(Pageable pageable);

    Page<T> listAcceptedGames(Pageable pageable);

    Page<T> listAcceptedGames(Pageable pageable, UUID player);

    void updateEloAndStats(T game) throws CapserException;

    void queueGame(T game) throws CapserException;

    void queueGame(T game, boolean notify) throws CapserException;

    void acceptGame(UUID gameId, boolean notify) throws CapserException;

    void acceptGame(UUID gameId) throws CapserException;

    int acceptOverdueGames();

    void rejectGame(UUID gameId) throws CapserException;

    AcceptanceRequestType getAcceptanceRequestType();

    GameType getGameType();
}
