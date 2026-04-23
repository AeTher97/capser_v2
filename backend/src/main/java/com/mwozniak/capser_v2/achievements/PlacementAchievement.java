package com.mwozniak.capser_v2.achievements;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;

import java.util.UUID;

public abstract class PlacementAchievement implements AchievementProcessor {

    protected boolean checkPlacementConditions(UserStats userStats) {
        return userStats.getGamesPlayed() > 0;
    }

    protected abstract GameType getGameType();

    @Override
    public AchievementEntity createEntity(User user, GameType gameType) {
        return buildAchievementEntity(user.getId(), gameType);
    }

}
