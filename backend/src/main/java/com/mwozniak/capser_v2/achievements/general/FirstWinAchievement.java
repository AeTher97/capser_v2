package com.mwozniak.capser_v2.achievements.general;

import com.mwozniak.capser_v2.achievements.AchievementProcessor;
import com.mwozniak.capser_v2.achievements.GeneralAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;

import java.util.UUID;

@GeneralAchievement
public class FirstWinAchievement implements AchievementProcessor {
    @Override
    public boolean checkConditions(User user, AbstractGame abstractGame) {
        return user.getUserEasyStats().getGamesWon() > 0 ||
                user.getUserSinglesStats().getGamesWon() > 0 ||
                user.getUserDoublesStats().getGamesWon() > 0;
    }

    @Override
    public AchievementEntity createEntity(UUID player) {
        return buildAchievementEntity(player);
    }

    @Override
    public Achievement getAchievement() {
        return Achievement.WIN_FIRST_GAME;
    }
}
