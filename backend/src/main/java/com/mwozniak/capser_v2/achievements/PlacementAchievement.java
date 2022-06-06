package com.mwozniak.capser_v2.achievements;

import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.UserStats;

import java.util.UUID;

public abstract class PlacementAchievement implements AchievementProcessor {

    protected boolean checkPlacementConditions(UserStats userStats) {
        return userStats.getGamesPlayed() > 0;
    }

    @Override
    public AchievementEntity createEntity(UUID player) {
        return buildAchievementEntity(player);
    }

}
