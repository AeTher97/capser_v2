package com.mwozniak.capser_v2.achievements.general.rebuttals;

import com.mwozniak.capser_v2.achievements.AchievementProcessor;
import com.mwozniak.capser_v2.enums.GameEvent;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;

import java.util.List;

public abstract class RebuttalsInARowAchievement implements AchievementProcessor {

    @Override
    public boolean checkConditions(User user, AbstractGame abstractGame) {
        return checkRebuttalsInARow(user, abstractGame);
    }

    protected boolean checkRebuttalsInARow(User user, AbstractGame abstractGame) {
        List<GameEventEntity> gameEvents = abstractGame.getGameEventList();

        int rebuttalsInARow = 0;
        for (GameEventEntity gameEventEntity : gameEvents) {
            if (gameEventEntity.getGameEvent().equals(GameEvent.REBUTTAL)) {
                rebuttalsInARow++;
                if (rebuttalsInARow == getNumberInARow()) {
                    return true;
                }
            } else {
                rebuttalsInARow = 0;
            }
        }
        return false;
    }

    protected abstract int getNumberInARow();
}
