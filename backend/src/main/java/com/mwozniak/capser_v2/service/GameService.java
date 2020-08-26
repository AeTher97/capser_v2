package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.exception.CapserException;

import java.util.UUID;

public interface GameService {

    AbstractGame findGame(UUID id)  throws CapserException;

    void queueGame(AbstractGame abstractGame) throws CapserException;

    void acceptGame(UUID gameId)  throws CapserException;
}
