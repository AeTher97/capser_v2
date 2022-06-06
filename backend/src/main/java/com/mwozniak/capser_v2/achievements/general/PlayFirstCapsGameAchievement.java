package com.mwozniak.capser_v2.achievements.general;

import com.mwozniak.capser_v2.achievements.AchievementProcessor;
import com.mwozniak.capser_v2.achievements.GeneralAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;

import java.util.UUID;

@GeneralAchievement
public class PlayFirstCapsGameAchievement implements AchievementProcessor {
    @Override
    public boolean checkConditions(User user, AbstractGame abstractGame) {
        return user.getUserEasyStats().getGamesPlayed() > 0 ||
                user.getUserSinglesStats().getGamesPlayed() > 0 ||
                user.getUserDoublesStats().getGamesPlayed() > 0 ||
                user.getUserUnrankedStats().getGamesPlayed() > 0;
    }

    @Override
    public AchievementEntity createEntity(UUID player) {
        return buildAchievementEntity(player);
    }

    @Override
    public Achievement getAchievement() {
        return Achievement.PLAY_FIRST_GAME;
    }
}
