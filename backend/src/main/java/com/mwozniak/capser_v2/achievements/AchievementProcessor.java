package com.mwozniak.capser_v2.achievements;

import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;

import java.util.Date;
import java.util.UUID;

public interface AchievementProcessor {

    boolean checkConditions(User user, AbstractGame abstractGame);

    AchievementEntity createEntity(UUID player);

    Achievement getAchievement();

    default AchievementEntity buildAchievementEntity(UUID player) {
        return AchievementEntity.builder()
                .achievement(getAchievement())
                .userId(player)
                .gameType(GameType.EASY_CAPS)
                .dateAchieved(new Date())
                .build();
    }
}
