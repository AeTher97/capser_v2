package com.mwozniak.capser_v2.models.database.game;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import com.mwozniak.capser_v2.models.exception.UpdateStatsException;

import java.util.List;
import java.util.UUID;

public interface Game {

    UUID getId();

    GameType getGameType();

    void calculateStatsOfAllPlayers() throws GameValidationException;

    void validate() throws GameValidationException;

    void setAccepted();

    List<UUID> getAllPlayers();

    UserStats findCorrectStats(User user);

    int getSinksLost(User user);

    int getPointsLost(User user);

    void updateUserPoints(User user, float pointsChange) throws GameValidationException;

    void fillCommonProperties(AbstractGameDto abstractGameDto);

    void calculatePlayerStats(User user) throws UpdateStatsException;

    boolean isWinner(User user);

    UUID getWinner();

}
