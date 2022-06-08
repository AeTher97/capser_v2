package com.mwozniak.capser_v2.achievements.general.rebuttals;

import com.mwozniak.capser_v2.achievements.AchievementProcessor;
import com.mwozniak.capser_v2.achievements.EasyAchievement;
import com.mwozniak.capser_v2.achievements.SinglesAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.enums.GameEvent;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;

import java.util.List;
import java.util.UUID;

@SinglesAchievement
@EasyAchievement
public class RebutOnLastChanceAchievement implements AchievementProcessor {
    @Override
    public boolean checkConditions(User user, AbstractGame abstractGame) {
        List<GameEventEntity> gameEventEntityList = abstractGame.getGameEventList();
        if (gameEventEntityList == null) {
            return false;
        }

        int currentOpponentScore = 0;

        for (GameEventEntity eventEntity : gameEventEntityList) {
            if (eventEntity.getGameEvent().equals(GameEvent.POINT) && !eventEntity.getUserId().equals(user.getId())) {
                currentOpponentScore++;
            }
            if (eventEntity.getGameEvent().equals(GameEvent.REBUTTAL) && eventEntity.getUserId().equals(user.getId()) && currentOpponentScore == 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AchievementEntity createEntity(UUID player) {
        return buildAchievementEntity(player);
    }

    @Override
    public Achievement getAchievement() {
        return Achievement.REBUT_ON_LAST_CHANCE;
    }
}
